package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.navercorp.volleyextensions.request.Jackson2Request;

import java.util.ArrayList;
import java.util.List;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.adapter.ColumnAdapter;
import hwang.daemin.kangbuk.common.GridSpacingItemDecoration;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.common.MyVolley;
import hwang.daemin.kangbuk.common.RecyclerViewOnItemClickListener;
import hwang.daemin.kangbuk.data.ColumnData;


/**
 * Created by user on 2016-06-14.
 */
public class ColumnFragment extends Fragment {
    private ProgressBar bar;
    private RecyclerView recyclerView;
    private ColumnAdapter adapter;
    private List<ColumnData.Product> columnList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_column,container,false);
        My.INFO.backKeyName ="";
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_column));
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view); //
        columnList = new ArrayList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new ColumnAdapter(getActivity(), columnList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getColumnData(getActivity());
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(getActivity(), recyclerView,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        TextView tvNum = (TextView) v.findViewById(R.id.tvNum);
                        int num = Integer.parseInt(tvNum.getText().toString());
                        Bundle bundle = new Bundle();
                        bundle.putInt("num",num);
                        Fragment fragment = new ColumnDetailFragment();
                        fragment.setArguments(bundle);
                        FragmentManager fm = getFragmentManager();
                        fm.beginTransaction().replace(R.id.content_frame,fragment).commit();
                    }
                    @Override
                    public void onItemLongClick(View v, int position) {
                    }
                }
        ));
        return rootView;
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public static final String GET_COLUMN = "http://browsable.cafe24.com/column/get_all_column.php";
    public void getColumnData(final Context context) {
        Jackson2Request<ColumnData> jackson2Request = new Jackson2Request<>(
                Request.Method.GET, GET_COLUMN, ColumnData.class,
                new Response.Listener<ColumnData>() {
                    @Override
                    public void onResponse(ColumnData response) {
                        if(response.getSuccess()==1) {
                            columnList.addAll(response.getProducts());
                            adapter.notifyDataSetChanged();
                        }
                        bar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);
            }
        });
        MyVolley.getRequestQueue().add(jackson2Request);
    }

}
