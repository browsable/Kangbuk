package hwang.daemin.kangbuk.fragments.file;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.main.MainActivity;

/**
 * Created by user on 2016-06-11.
 */
public class YoutubeActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    List<YoutubeVideo> videoList;
    private YoutubeVideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        // Get the intent that started this activity
        My.INFO.backKeyName ="YoutubeFragment";
        recyclerView = (RecyclerView) findViewById(R.id.youtube_recycler_view); //
        videoList = new ArrayList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new YoutubeVideoAdapter(YoutubeActivity.this, videoList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(YoutubeActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new VerticalItemDecorator(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getData();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }
        // Button launches NewPostActivity
    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            Log.i("test",sharedText);
            String videoId = sharedText.split("e\\/")[1];
            Log.i("test",videoId);
            videoList.add(new YoutubeVideo(videoId, "hi"));
            adapter.notifyDataSetChanged();
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.releaseLoaders();
            adapter = null;
        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (adapter != null) {
            adapter.releaseLoaders();
            adapter = null;
        }
    }

    public void getData(){
        videoList.add(new YoutubeVideo("mb9ovXBsoQc", "유기성 목사 - 무엇을 사랑하는가에 따라 인생은 결정된다"));
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
