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

package hwang.daemin.kangbuk.fragments.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.User;
import hwang.daemin.kangbuk.firebase.fUtil;

public class NewPicUploadTaskFragment extends Fragment {
    private static final String TAG = "NewPostTaskFragment";

    public interface TaskCallbacks {
        void onBitmapResized(Bitmap bitmap, int mMaxDimension);
        void onPhotoUploaded(String error,String fullURL,String thumbURL);
    }

    private Context mApplicationContext;
    private TaskCallbacks mCallbacks;
    private Bitmap selectedBitmap;
    private Bitmap thumbnail;

    public NewPicUploadTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain this fragment across config changes.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof TaskCallbacks) {
            mCallbacks = (TaskCallbacks) getActivity();
        } else {
            throw new RuntimeException(getActivity().toString()
                    + " must implement TaskCallbacks");
        }
        mApplicationContext = getActivity().getApplicationContext();
    }

    public void setSelectedBitmap(Bitmap bitmap) {
        this.selectedBitmap = bitmap;
    }

    public Bitmap getSelectedBitmap() {
        return selectedBitmap;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void resizeBitmapWithPath(String path, int maxDimension) {
        LoadResizedBitmapTask task = new LoadResizedBitmapTask(path, maxDimension);
        task.execute();
    }
    public void uploadPhoto(Bitmap full, StorageReference fullRef, Bitmap thumbnail, StorageReference thumbRef, String inFileName) {
        UploadPhotoTask uploadTask = new UploadPhotoTask(full,fullRef,thumbnail,thumbRef,inFileName);
        uploadTask.execute();
    }
    public void uploadProfile(Bitmap full, StorageReference fullRef, Bitmap thumbnail, StorageReference thumbRef, String inFileName) {
        UploadProfileTask uploadTask = new UploadProfileTask(full,fullRef,thumbnail,thumbRef,inFileName);
        uploadTask.execute();
    }

    class UploadPhotoTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> fullReference;
        private StorageReference fullSizeRef;
        private WeakReference<Bitmap> thumbReference;
        private StorageReference thumbSizeRef;

        public UploadPhotoTask(Bitmap full, StorageReference fullRef, Bitmap thumbnail, StorageReference thumbRef, String inFileName) {
            fullReference = new WeakReference<>(full);
            fullSizeRef = fullRef.child(inFileName);
            thumbReference = new WeakReference<>(thumbnail);
            thumbSizeRef = thumbRef.child(inFileName);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap fullSize = fullReference.get();
            if (fullSize == null) {
                return null;
            }
            ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
            fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
            byte[] bytes = fullSizeStream.toByteArray();
            fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                    Bitmap thumbnail = thumbReference.get();
                    if (thumbnail == null) {
                        return;
                    }
                    ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, thumbnailStream);
                    thumbSizeRef.putBytes(thumbnailStream.toByteArray())
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final Uri thumbnailUrl = taskSnapshot.getDownloadUrl();
                                    mCallbacks.onPhotoUploaded(null,fullSizeUrl.toString(), thumbnailUrl.toString());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mCallbacks.onPhotoUploaded(mApplicationContext.getString(
                                    R.string.error_upload_task_create),null,null);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mCallbacks.onPhotoUploaded(mApplicationContext.getString(
                            R.string.error_upload_task_create),null, null);
                }
            });
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }
    }
    class UploadProfileTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> bitmapReference;
        private WeakReference<Bitmap> thumbnailReference;
        private StorageReference fullSizeRef;
        private StorageReference thumbnailRef;

        public UploadProfileTask(Bitmap bitmap, StorageReference fullRef, Bitmap thumbnail, StorageReference thumbRef,
                                 String inFileName) {
            bitmapReference = new WeakReference<>(bitmap);
            thumbnailReference = new WeakReference<>(thumbnail);
            fullSizeRef = fullRef.child(inFileName);
            thumbnailRef = thumbRef.child(inFileName);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap fullSize = bitmapReference.get();
            final Bitmap thumbnail = thumbnailReference.get();
            if (fullSize == null || thumbnail == null) {
                return null;
            }
            ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
            fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
            byte[] bytes = fullSizeStream.toByteArray();
            fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                    ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 70, thumbnailStream);
                    thumbnailRef.putBytes(thumbnailStream.toByteArray())
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    final Uri thumbnailUrl = taskSnapshot.getDownloadUrl();
                                    if (fUtil.firebaseUser == null) {
                                        mCallbacks.onPhotoUploaded(mApplicationContext.getString(
                                                R.string.error_user_not_signed_in),null,null);
                                        return;
                                    }
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(thumbnailUrl)
                                            .build();

                                    fUtil.firebaseUser.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                }
                                            });
                                    fUtil.getUserRef().child(fUtil.firebaseUser.getUid())
                                            .setValue(new User(fUtil.firebaseUser.getDisplayName(),fullSizeUrl.toString(),thumbnailUrl.toString()));
                                    mCallbacks.onPhotoUploaded(null,null,null);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mCallbacks.onPhotoUploaded(mApplicationContext.getString(
                                    R.string.error_user_not_signed_in),null,null);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mCallbacks.onPhotoUploaded(mApplicationContext.getString(
                            R.string.error_user_not_signed_in),null,null);
                }
            });
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }
    }
    class LoadResizedBitmapTask extends AsyncTask<Void, Void, Bitmap> {
        private int mMaxDimension;
        private String filePath;

        public LoadResizedBitmapTask(String filePath, int maxDimension) {
            mMaxDimension = maxDimension;
            this.filePath =filePath;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Void... params) {
                if (filePath != null) {
                    // TODO: Currently making these very small to investigate modulefood bug.
                    // Implement thumbnail + fullsize later.
                    Bitmap adjustedBitmap = null;
                    try {
                        Bitmap bitmap = decodeSampledBitmapFromPath(filePath, mMaxDimension, mMaxDimension);
                        ExifInterface exif = new ExifInterface(filePath);
                        int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                        int exifDegree = exifOrientationToDegrees(rotation);
                        Matrix matrix = new Matrix();
                        if (rotation != 0f) {
                            matrix.preRotate(exifDegree);
                        }
                        adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "Can't find file to resize: " + e.getMessage());

                    } catch (IOException e) {
                        Log.e(TAG, "Error occurred during resize: " + e.getMessage());
                    }
                    return adjustedBitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mCallbacks.onBitmapResized(bitmap, mMaxDimension);
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromPath(String filePath, int reqWidth, int reqHeight)
            throws IOException {
        File file = new File(filePath);
        InputStream stream = new BufferedInputStream(new FileInputStream(file));
        /*InputStream stream = new BufferedInputStream(
                mApplicationContext.getContentResolver().openInputStream(fileUri));*/
        stream.mark(stream.available());
        BitmapFactory.Options options = new BitmapFactory.Options();
        // First decode with inJustDecodeBounds=true to check dimensions
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(stream, null, options);
        stream.reset();
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        BitmapFactory.decodeStream(stream, null, options);
        // Decode bitmap with inSampleSize set
        stream.reset();
        return BitmapFactory.decodeStream(stream, null, options);
    }

    private static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}

