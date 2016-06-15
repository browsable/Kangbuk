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
public class WeekAfternoonFragment extends Fragment {

    private static String url_get_weekly0 = "http://browsable.cafe24.com/weekly/get_weekly0.php";
    private static final String TAG_SUCCESS= "success";
    private static final String TAG_WEEKLY0 = "weekly0";
    private static final String TAG_DATE = "date";
    private static final String TAG_WEEKLY1_1 = "weekly1_1";
    private static final String TAG_WEEKLY1_2 = "weekly1_2";
    private static final String TAG_WEEKLY1_3 = "weekly1_3";
    TextView weekly1_1;
    TextView weekly1_2;
    TextView weekly1_3;
    TextView regist_day1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_week_afternoon,container,false);
        EventBus.getDefault().post(new BackKeyEvent(""));
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.nav_week_afternoon));
        weekly1_1 = (TextView) rootView.findViewById(R.id.weekly1_1);
        weekly1_2 = (TextView) rootView.findViewById(R.id.weekly1_2);
        weekly1_3 = (TextView) rootView.findViewById(R.id.weekly1_3);
        regist_day1 = (TextView) rootView.findViewById(R.id.regist_day1);
        getWeekAfternoon(getActivity());
        return rootView;
    }

    public void getWeekAfternoon(final Context context) {
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
                                    regist_day1.setText(c.getString(TAG_DATE));
                                    weekly1_1.setText(c.getString(TAG_WEEKLY1_1));
                                    weekly1_2.setText(c.getString(TAG_WEEKLY1_2));
                                    weekly1_3.setText(c.getString(TAG_WEEKLY1_3));
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
