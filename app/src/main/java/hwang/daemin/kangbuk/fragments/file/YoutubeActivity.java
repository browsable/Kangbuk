package hwang.daemin.kangbuk.fragments.file;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.main.MainActivity;

/**
 * Created by user on 2016-06-11.
 */
public class YoutubeActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    LinkedList<YoutubeVideo> videoList;
    private YoutubeVideoAdapter adapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        // Get the intent that started this activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        My.INFO.backKeyName ="YoutubeActivity";
        fab = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.youtube_recycler_view); //
        videoList = new LinkedList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new YoutubeVideoAdapter(YoutubeActivity.this, videoList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(YoutubeActivity.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new VerticalItemDecorator(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(YoutubeActivity.this, YoutubeInfoActivity.class));
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                adapter.notifyDataSetChanged();
            }
        });
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
        FirebaseDatabase.getInstance().getReference().child("youtube").orderByKey().limitToFirst(50).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot yVideo: dataSnapshot.getChildren()){
                    videoList.addFirst(yVideo.getValue(YoutubeVideo.class));
                }
                if(adapter==null) adapter = new YoutubeVideoAdapter(YoutubeActivity.this, videoList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent i = new Intent(YoutubeActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return super.onSupportNavigateUp();
    }
}
