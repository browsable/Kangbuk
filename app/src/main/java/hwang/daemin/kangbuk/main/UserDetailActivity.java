/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hwang.daemin.kangbuk.main;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.GlideUtil;
import hwang.daemin.kangbuk.firebase.FirebaseUtil;
import hwang.daemin.kangbuk.fragments.picture.NewPostUploadTaskFragment;
import pub.devrel.easypermissions.EasyPermissions;

public class UserDetailActivity extends BaseActivity implements
        EasyPermissions.PermissionCallbacks,
        NewPostUploadTaskFragment.TaskCallbacks{
    private final String TAG = "UserDetailActivity";
    private static final int THUMBNAIL_MAX_DIMENSION = 480;
    private static final int FULL_SIZE_MAX_DIMENSION = 960;
    private CircleImageView ivProfile;
    private static final int REQUEST_IMAGE_PIC = 1;
    private static final int REQUEST_IMAGE_CROP = 2;
    private static final int REQUEST_IMAGE_UPLOAD = 3;
    public static final String TAG_TASK_FRAGMENT = "newPostUploadTaskFragment";
    private String currentUserId;
    private String mFilePath;
    private Uri mFileUri = null;
    private Bitmap mResizedBitmap;
    private Bitmap mThumbnail;
    private NewPostUploadTaskFragment mTaskFragment;
    private static final int RC_CAMERA_PERMISSIONS = 102;

    private static final String[] cameraPerms = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        currentUserId = FirebaseUtil.getCurrentUserId();
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(FirebaseUtil.getCurrentUserName());
        ivProfile = (CircleImageView) findViewById(R.id.ivProfile);
        GlideUtil.loadProfileIcon(FirebaseUtil.getCurrentUser().getPhotoUrl().toString(), ivProfile);

        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (NewPostUploadTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        // create the fragment and data the first time
        if (mTaskFragment == null) {
            // add the fragment
            mTaskFragment = new NewPostUploadTaskFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
        Bitmap selectedBitmap = mTaskFragment.getSelectedBitmap();
        Bitmap thumbnail = mTaskFragment.getThumbnail();
        if (selectedBitmap != null) {
            ivProfile.setImageBitmap(selectedBitmap);
            mResizedBitmap = selectedBitmap;
        }
        if (thumbnail != null) {
            mThumbnail = thumbnail;
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && !EasyPermissions.hasPermissions(UserDetailActivity.this, cameraPerms)) {
                    EasyPermissions.requestPermissions(this,
                            "사진 업로드를 위해 저장소에 접근합니다",
                            RC_CAMERA_PERMISSIONS, cameraPerms);
                    return;
                }
                if(currentUserId!=null) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE_PIC);
                }else{
                    Toast.makeText(UserDetailActivity.this,getString(R.string.error_user_not_signed_in), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_PIC:
                    mFileUri = data.getData();
                case REQUEST_IMAGE_CROP:
                    cropImage(mFileUri);
                    break;
                case REQUEST_IMAGE_UPLOAD:
                    showProgressDialog();
                    mTaskFragment.resizeBitmap(mFileUri, THUMBNAIL_MAX_DIMENSION);
                    mTaskFragment.resizeBitmap(mFileUri, FULL_SIZE_MAX_DIMENSION);
                    break;
            }
        }
    }

    private void cropImage(Uri mFileUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri of image
        cropIntent.setDataAndType(mFileUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, REQUEST_IMAGE_UPLOAD);

    }

    @Override
    protected void onDestroy() {
        // store the data in the fragment
        if (mResizedBitmap != null) {
            mTaskFragment.setSelectedBitmap(mResizedBitmap);
        }
        if (mThumbnail != null) {
            mTaskFragment.setThumbnail(mThumbnail);
        }
        super.onDestroy();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
    @Override
    public void onBitmapResized(Bitmap resizedBitmap, int mMaxDimension) {
        if (resizedBitmap == null) {
            Log.e(TAG, "Couldn't resize bitmap in background task.");
            Toast.makeText(getApplicationContext(), "Couldn't resize bitmap.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (mMaxDimension == THUMBNAIL_MAX_DIMENSION) {
            mThumbnail = resizedBitmap;
        } else if (mMaxDimension == FULL_SIZE_MAX_DIMENSION) {
            mResizedBitmap = resizedBitmap;
            ivProfile.setImageBitmap(mResizedBitmap);
        }

        if (mThumbnail != null && mResizedBitmap != null) {
            ivProfile.setEnabled(true);
            StorageReference fullSizeRef = FirebaseUtil.getStoreFullProfileRef().child(currentUserId);
            StorageReference thumbnailRef = FirebaseUtil.getStoreThumbProfileRef().child(currentUserId);
            mTaskFragment.uploadPost(mResizedBitmap, fullSizeRef, mThumbnail, thumbnailRef, "profile.jpg","");
        }
    }

    @Override
    public void onPostUploaded(final String error) {
        UserDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivProfile.setEnabled(true);
                hideProgressDialog();
                if (error != null) {
                    Toast.makeText(UserDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
