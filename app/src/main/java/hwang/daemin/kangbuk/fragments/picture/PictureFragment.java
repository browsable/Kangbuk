package hwang.daemin.kangbuk.fragments.picture;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.adapter.PictureAdapter;
import hwang.daemin.kangbuk.common.GridSpacingItemDecoration;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.data.Post;


/**
 * Created by user on 2016-06-14.
 */
public class PictureFragment extends Fragment {
    private RecyclerView recyclerView;
    private PictureAdapter adapter;
    private List<Post> picList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_picture,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_picture));
        My.INFO.backKeyName ="";
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        picList = new ArrayList<>();
        adapter = new PictureAdapter(getActivity(), picList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


        return rootView;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
