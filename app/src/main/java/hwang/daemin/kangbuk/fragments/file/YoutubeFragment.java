package hwang.daemin.kangbuk.fragments.file;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appinvite.AppInviteInvitation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.My;

/**
 * Created by user on 2016-06-14.
 */
@TargetApi(13)
public class YoutubeFragment extends Fragment {
    protected RecyclerView recyclerView;
    List<YoutubeVideo> videoList;
    private YoutubeVideoAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_youtube,container,false);
        My.INFO.backKeyName ="YoutubeFragment";
        Log.i("frag", "YoutubeFragment");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.youtube_recycler_view); //
        videoList = new ArrayList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new YoutubeVideoAdapter(getActivity(), videoList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new VerticalItemDecorator(20));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getData();

        // Button launches NewPostActivity
        rootView.findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return  rootView;
    }

    /*

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mYoutubeVideoAdapter != null) {
            mYoutubeVideoAdapter.releaseLoaders();
            mYoutubeVideoAdapter = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mYoutubeVideoAdapter != null) {
            mYoutubeVideoAdapter.releaseLoaders();
            mYoutubeVideoAdapter = null;
        }
    }
*/

    public void getData(){
        videoList.add(new YoutubeVideo("mb9ovXBsoQc", "유기성 목사 - 무엇을 사랑하는가에 따라 인생은 결정된다"));
        adapter.notifyDataSetChanged();
    }
}
