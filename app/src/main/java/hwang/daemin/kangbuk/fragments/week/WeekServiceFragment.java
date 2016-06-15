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
public class WeekServiceFragment extends Fragment {
    private static String url_get_weekly0 = "http://browsable.cafe24.com/weekly/get_weekly0.php";
    private static final String TAG_SUCCESS= "success";
    private static final String TAG_WEEKLY0 = "weekly0";
    private static final String TAG_WEEKLY3_1 = "weekly3_1";
    private static final String TAG_WEEKLY3_2 = "weekly3_2";
    private static final String TAG_WEEKLY3_3 = "weekly3_3";
    private static final String TAG_WEEKLY3_4 = "weekly3_4";
    private static final String TAG_WEEKLY3_5 = "weekly3_5";
    private static final String TAG_WEEKLY3_6 = "weekly3_6";
    private static final String TAG_WEEKLY3_7 = "weekly3_7";
    private static final String TAG_WEEKLY3_8 = "weekly3_8";
    private static final String TAG_WEEKLY3_9 = "weekly3_9";
    private static final String TAG_WEEKLY3_10 = "weekly3_10";
    private static final String TAG_WEEKLY3_11 = "weekly3_11";
    private static final String TAG_WEEKLY3_12 = "weekly3_12";
    private static final String TAG_WEEKLY3_13 = "weekly3_13";
    private static final String TAG_WEEKLY3_14 = "weekly3_14";
    private static final String TAG_WEEKLY3_15 = "weekly3_15";
    private static final String TAG_WEEKLY3_16 = "weekly3_16";
    private static final String TAG_WEEKLY3_MONTH ="weekly3_month";
    TextView weekly3_1;
    TextView weekly3_2;
    TextView weekly3_3;
    TextView weekly3_4;
    TextView weekly3_5;
    TextView weekly3_6;
    TextView weekly3_7;
    TextView weekly3_8;
    TextView weekly3_9;
    TextView weekly3_10;
    TextView weekly3_11;
    TextView weekly3_12;
    TextView weekly3_13;
    TextView weekly3_14;
    TextView weekly3_15;
    TextView weekly3_16;
    TextView weekly3_month;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_service,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_service));
        weekly3_1 = (TextView) rootView.findViewById(R.id.weekly3_1);
        weekly3_2 = (TextView) rootView.findViewById(R.id.weekly3_2);
        weekly3_3 = (TextView) rootView.findViewById(R.id.weekly3_3);
        weekly3_4 = (TextView) rootView.findViewById(R.id.weekly3_4);
        weekly3_5 = (TextView) rootView.findViewById(R.id.weekly3_5);
        weekly3_6 = (TextView) rootView.findViewById(R.id.weekly3_6);
        weekly3_7 = (TextView) rootView.findViewById(R.id.weekly3_7);
        weekly3_8 = (TextView) rootView.findViewById(R.id.weekly3_8);
        weekly3_9 = (TextView) rootView.findViewById(R.id.weekly3_9);
        weekly3_10 = (TextView) rootView.findViewById(R.id.weekly3_10);
        weekly3_11 = (TextView) rootView.findViewById(R.id.weekly3_11);
        weekly3_12 = (TextView) rootView.findViewById(R.id.weekly3_12);
        weekly3_13 = (TextView) rootView.findViewById(R.id.weekly3_13);
        weekly3_14 = (TextView) rootView.findViewById(R.id.weekly3_14);
        weekly3_15 = (TextView) rootView.findViewById(R.id.weekly3_15);
        weekly3_16 = (TextView) rootView.findViewById(R.id.weekly3_16);
        weekly3_month = (TextView) rootView.findViewById(R.id.weekly3_month);
        getWeekService(getActivity());
        return rootView;
    }
    public void getWeekService(final Context context) {
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
                                    weekly3_month.setText(c.getString(TAG_WEEKLY3_MONTH));
                                    weekly3_1.setText(c.getString(TAG_WEEKLY3_1));
                                    weekly3_2.setText(c.getString(TAG_WEEKLY3_2));
                                    weekly3_3.setText(c.getString(TAG_WEEKLY3_3));
                                    weekly3_4.setText(c.getString(TAG_WEEKLY3_4));
                                    weekly3_5.setText(c.getString(TAG_WEEKLY3_5));
                                    weekly3_6.setText(c.getString(TAG_WEEKLY3_6));
                                    weekly3_7.setText(c.getString(TAG_WEEKLY3_7));
                                    weekly3_8.setText(c.getString(TAG_WEEKLY3_8));
                                    weekly3_9.setText(c.getString(TAG_WEEKLY3_9));
                                    weekly3_10.setText(c.getString(TAG_WEEKLY3_10));
                                    weekly3_11.setText(c.getString(TAG_WEEKLY3_11));
                                    weekly3_12.setText(c.getString(TAG_WEEKLY3_12));
                                    weekly3_13.setText(c.getString(TAG_WEEKLY3_13));
                                    weekly3_14.setText(c.getString(TAG_WEEKLY3_14));
                                    weekly3_15.setText(c.getString(TAG_WEEKLY3_15));
                                    weekly3_16.setText(c.getString(TAG_WEEKLY3_16));
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
