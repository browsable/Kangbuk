package hwang.daemin.kangbuk.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.common.CustomJSONObjectRequest;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.common.MyVolley;

/**
 * Created by user on 2016-06-14.
 */
public class ColumnDetailFragment extends Fragment {
    private static String url_column_details = "http://browsable.cafe24.com/column/get_column_details.php?num=";
    private static String url_column_web_view = "http://browsable.cafe24.com/column/column_web_view.php?num=";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_NUM = "num";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_BIBLE_CONTENT = "bible_content";
    private static final String TAG_DATE = "date";
    TextView textTitle;
    TextView textDate;
    TextView textBible_content;
    WebView webview;
    int num;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_columndetail,container,false);
        My.INFO.backKeyName ="ColumnDetailFragment";
        Bundle extra = getArguments();
        this.num = extra.getInt("num");
        textTitle = (TextView) rootView.findViewById(R.id.title_column_detail);
        textDate = (TextView) rootView.findViewById(R.id.date_column_detail);
        textBible_content = (TextView) rootView.findViewById(R.id.bible_content_column_detail);
        webview = (WebView)rootView.findViewById(R.id.webview);
        webview.setWebViewClient(new WebClient());
        WebSettings set = webview.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        webview.setBackgroundColor(0x00000000);
        webview.loadUrl("http://browsable.cafe24.com/column/column_web_view.php?num="+num);
        getColumnDetails(getActivity());

        return rootView;
    }
    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    public void getColumnDetails(final Context context) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                url_column_details+num, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int success = response.getInt(TAG_SUCCESS);
                            if (success==1) {
                                JSONObject json = response.getJSONArray(TAG_PRODUCT).getJSONObject(0);
                                textTitle.setText(json.getString(TAG_TITLE));
                                textDate.setText(json.getString(TAG_DATE));
                                textBible_content.setText(json.getString(TAG_BIBLE_CONTENT));
                                //JSONObject data = response.getJSONObject("data");
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
