package hwang.daemin.kangbuk.fragments.week;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.CustomJSONObjectRequest;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.common.MyVolley;


/**
 * Created by user on 2016-06-14.
 */
public class WeekMidFragment extends Fragment {
    private ProgressBar bar;
    private static String url_get_weekly0 = "http://browsable.cafe24.com/weekly/get_weekly0.php";
    private static final String TAG_SUCCESS= "success";
    private static final String TAG_WEEKLY0 = "weekly0";
    private static final String TAG_DATE = "date";
    private static final String TAG_WEEKLY0_1 = "weekly0_1";
    private static final String TAG_WEEKLY0_2 = "weekly0_2";
    private static final String TAG_WEEKLY0_3 = "weekly0_3";
    private static final String TAG_WEEKLY0_4 = "weekly0_4";
    private static final String TAG_WEEKLY0_5 = "weekly0_5";
    private static final String TAG_WEEKLY0_6 = "weekly0_6";
    private static final String TAG_WEEKLY0_7 = "weekly0_7";
    private static final String TAG_WEEKLY0_8 = "weekly0_8";
    private static final String TAG_WEEKLY0_9 = "weekly0_9";
    private TextView weekly0_1;
    private TextView weekly0_2;
    private TextView weekly0_3;
    private TextView weekly0_4;
    private TextView weekly0_5;
    private TextView weekly0_6;
    private TextView weekly0_7;
    private TextView weekly0_8;
    private TextView weekly0_9;
    private TextView regist_day0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_mid,container,false);
        My.INFO.backKeyName ="";
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_mid));
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        regist_day0 = (TextView) rootView.findViewById(R.id.regist_day0);
        weekly0_1 = (TextView) rootView.findViewById(R.id.weekly0_1);
        weekly0_2 = (TextView) rootView.findViewById(R.id.weekly0_2);
        weekly0_3 = (TextView) rootView.findViewById(R.id.weekly0_3);
        weekly0_4 = (TextView) rootView.findViewById(R.id.weekly0_4);
        weekly0_5 = (TextView) rootView.findViewById(R.id.weekly0_5);
        weekly0_6 = (TextView) rootView.findViewById(R.id.weekly0_6);
        weekly0_7 = (TextView) rootView.findViewById(R.id.weekly0_7);
        weekly0_8 = (TextView) rootView.findViewById(R.id.weekly0_8);
        weekly0_9 = (TextView) rootView.findViewById(R.id.weekly0_9);
        getWeekMid(getActivity());

        return rootView;
    }

    public void getWeekMid(final Context context) {
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
                                    regist_day0.setText(c.getString(TAG_DATE));
                                    weekly0_1.setText(c.getString(TAG_WEEKLY0_1));
                                    weekly0_2.setText(c.getString(TAG_WEEKLY0_2));
                                    weekly0_3.setText(c.getString(TAG_WEEKLY0_3));
                                    weekly0_4.setText(c.getString(TAG_WEEKLY0_4));
                                    weekly0_5.setText(c.getString(TAG_WEEKLY0_5));
                                    weekly0_6.setText(c.getString(TAG_WEEKLY0_6));
                                    weekly0_7.setText(c.getString(TAG_WEEKLY0_7));
                                    weekly0_8.setText(c.getString(TAG_WEEKLY0_8));
                                    weekly0_9.setText(c.getString(TAG_WEEKLY0_9));
                                }
                                //JSONObject data = response.getJSONObject("data");
                            }else if (success == 0) {
                                Toast.makeText(context, "Invalid email or password..", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(context, "Something went wrong.Please try again..", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                bar.setVisibility(View.GONE);
            }
        });
        MyVolley.getRequestQueue().add(rq);
    }
}
