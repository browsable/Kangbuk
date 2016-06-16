package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.List;
import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.adapter.ScheduleAdapter;
import hwang.daemin.kangbuk.data.ScheduleData;
import hwang.daemin.kangbuk.event.BackKeyEvent;


/**
 * Created by user on 2016-06-14.
 */
public class ScheduleFragment extends Fragment{
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<ScheduleData> scheduleList;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_schedule,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_schedule));

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view); //
        scheduleList = new ArrayList<>(); // 메모 공간 선언 (메모리 할당)
        adapter = new ScheduleAdapter(scheduleList); // 어댑터 메모리 할당

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // Initialize Firebase Remote Config.
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        // Define Firebase Remote Config Settings.
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings =
                new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(true)
                        .build();

        mFirebaseRemoteConfig.setConfigSettings(firebaseRemoteConfigSettings);
        // Fetch remote config.
        fetchConfig();
        return rootView;
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    // Fetch the config to determine the allowed length of messages.
    public void fetchConfig() {
        long cacheExpiration = 3600; // 1 hour in seconds
        // If developer mode is enabled reduce cacheExpiration to 0 so that
        // each fetch goes to the server. This should not be used in release
        // builds.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings()
                .isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Make the fetched config available via
                        // FirebaseRemoteConfig get<type> calls.
                        mFirebaseRemoteConfig.activateFetched();
                        updateSchedule();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // There has been an error fetching the config
                        updateSchedule();
                    }
                });
    }


    /**
     * Apply retrieved length limit to edit text field.
     * This result may be fresh from the server or it may be from cached
     * values.
     */
    private void updateSchedule() {
        String dawn = mFirebaseRemoteConfig.getString("dawn");
        String mid = mFirebaseRemoteConfig.getString("mid");
        String after = mFirebaseRemoteConfig.getString("after");
        String wed = mFirebaseRemoteConfig.getString("wed");
        String child = mFirebaseRemoteConfig.getString("child");
        String middlehigh = mFirebaseRemoteConfig.getString("middlehigh");
        String univ = mFirebaseRemoteConfig.getString("univ");
        String newFa = mFirebaseRemoteConfig.getString("newFa");
        String leader = mFirebaseRemoteConfig.getString("leader");
        String fri = mFirebaseRemoteConfig.getString("fri");
        scheduleList.add(new ScheduleData("새벽기도회",dawn));
        scheduleList.add(new ScheduleData("주일낮예배",mid));
        scheduleList.add(new ScheduleData("주일오후예배",after));
        scheduleList.add(new ScheduleData("수요저녁예배",wed));
        scheduleList.add(new ScheduleData("주일학교",child));
        scheduleList.add(new ScheduleData("중고등부",middlehigh));
        scheduleList.add(new ScheduleData("청년대학부",univ));
        scheduleList.add(new ScheduleData("새가족성경공부",newFa));
        scheduleList.add(new ScheduleData("목자교사성경공부",leader));
        scheduleList.add(new ScheduleData("금요연합기도회",fri));
        adapter.notifyDataSetChanged();
    }
}
