package hwang.daemin.kangbuk.fragments.week;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.CustomJSONObjectRequest;
import hwang.daemin.kangbuk.common.MyVolley;
import hwang.daemin.kangbuk.event.BackKeyEvent;


/**
 * Created by user on 2016-06-14.
 */
public class WeekDailyFragment extends Fragment {
    private static String url_get_weekly0 = "http://browsable.cafe24.com/weekly/get_weekly0.php";
    private static final String TAG_SUCCESS= "success";
    private static final String TAG_WEEKLY0 = "weekly0";
    private static final String TAG_WEEKLY7_1 = "weekly7_1";
    private static final String TAG_WEEKLY7_2 = "weekly7_2";
    private static final String TAG_WEEKLY7_3 = "weekly7_3";
    private static final String TAG_WEEKLY7_4 = "weekly7_4";
    private static final String TAG_WEEKLY7_5 = "weekly7_5";
    private static final String TAG_WEEKLY7_6 = "weekly7_6";
    private static final String TAG_WEEKLY7_7 = "weekly7_7";
    private static final String TAG_WEEKLY7_8 = "weekly7_8";
    private static final String TAG_WEEKLY7_9 = "weekly7_9";
    private static final String TAG_WEEKLY7_10 = "weekly7_10";
    private static final String TAG_WEEKLY7_11 = "weekly7_11";
    private static final String TAG_WEEKLY7_12 = "weekly7_12";
    private static final String TAG_WEEKLY7_13 = "weekly7_13";
    private static final String TAG_WEEKLY7_14 = "weekly7_14";
    TextView weekly7_1;
    TextView weekly7_2;
    TextView weekly7_3;
    TextView weekly7_4;
    TextView weekly7_5;
    TextView weekly7_6;
    TextView weekly7_7;
    TextView weekly7_8;
    TextView weekly7_9;
    TextView weekly7_10;
    TextView weekly7_11;
    TextView weekly7_12;
    TextView weekly7_13;
    TextView weekly7_14;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_daily,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_daily));
        weekly7_1 = (TextView) rootView.findViewById(R.id.weekly7_1);
        weekly7_2 = (TextView) rootView.findViewById(R.id.weekly7_2);
        weekly7_3 = (TextView) rootView.findViewById(R.id.weekly7_3);
        weekly7_4 = (TextView) rootView.findViewById(R.id.weekly7_4);
        weekly7_5 = (TextView) rootView.findViewById(R.id.weekly7_5);
        weekly7_6 = (TextView) rootView.findViewById(R.id.weekly7_6);
        weekly7_7 = (TextView) rootView.findViewById(R.id.weekly7_7);
        weekly7_8 = (TextView) rootView.findViewById(R.id.weekly7_8);
        weekly7_9 = (TextView) rootView.findViewById(R.id.weekly7_9);
        weekly7_10 = (TextView) rootView.findViewById(R.id.weekly7_10);
        weekly7_11 = (TextView) rootView.findViewById(R.id.weekly7_11);
        weekly7_12 = (TextView) rootView.findViewById(R.id.weekly7_12);
        weekly7_13 = (TextView) rootView.findViewById(R.id.weekly7_13);
        weekly7_14 = (TextView) rootView.findViewById(R.id.weekly7_14);
        getWeekDaily(getActivity());
        return rootView;
    }
    public void getWeekDaily(final Context context) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST,
                url_get_weekly0, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            if (success==1) {
                                JSONArray weekly0 = response.getJSONArray(TAG_WEEKLY0);
                                for (int i = 0; i < weekly0.length(); i++) {
                                    JSONObject c = weekly0.getJSONObject(i);
                                    weekly7_1.setText(c.getString(TAG_WEEKLY7_1));
                                    weekly7_2.setText(c.getString(TAG_WEEKLY7_2));
                                    weekly7_3.setText(c.getString(TAG_WEEKLY7_3));
                                    weekly7_4.setText(c.getString(TAG_WEEKLY7_4));
                                    weekly7_5.setText(c.getString(TAG_WEEKLY7_5));
                                    weekly7_6.setText(c.getString(TAG_WEEKLY7_6));
                                    weekly7_7.setText(c.getString(TAG_WEEKLY7_7));
                                    weekly7_8.setText(c.getString(TAG_WEEKLY7_8));
                                    weekly7_9.setText(c.getString(TAG_WEEKLY7_9));
                                    weekly7_10.setText(c.getString(TAG_WEEKLY7_10));
                                    weekly7_11.setText(c.getString(TAG_WEEKLY7_11));
                                    weekly7_12.setText(c.getString(TAG_WEEKLY7_12));
                                    weekly7_13.setText(c.getString(TAG_WEEKLY7_13));
                                    weekly7_14.setText(c.getString(TAG_WEEKLY7_14));
                                }
                            }else if (success == 0) {
                                Toast.makeText(context, "Invalid email or password..", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Something went wrong.Please try again..", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
            }
        });
        MyVolley.getRequestQueue().add(rq);
    }
}
