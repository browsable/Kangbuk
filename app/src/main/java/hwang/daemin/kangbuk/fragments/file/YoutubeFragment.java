package hwang.daemin.kangbuk.fragments.file;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;


/**
 * Created by user on 2016-06-14.
 */
public class YoutubeFragment extends Fragment {

    protected RecyclerView recyclerView;
    LinkedList<YoutubeVideo> videoList;
    private YoutubeVideoAdapter adapter;
    private FloatingActionButton fab;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_youtube,container,false);
        My.INFO.backKeyName ="YoutubeFragment";
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_youtube));
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.youtube_recycler_view); //
        videoList = new LinkedList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new YoutubeVideoAdapter(getActivity(), videoList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new VerticalItemDecorator(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), YoutubeInfoActivity.class));
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
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
                videoList.clear();
                for(DataSnapshot yVideo: dataSnapshot.getChildren()){
                    videoList.addFirst(yVideo.getValue(YoutubeVideo.class));
                }
                if(adapter==null) adapter = new YoutubeVideoAdapter(getActivity(), videoList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
