package hwang.daemin.kangbuk.fragments.file;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.auth.SignInActivity;
import hwang.daemin.kangbuk.common.CustomJSONObjectRequest;
import hwang.daemin.kangbuk.common.GlideUtil;
import hwang.daemin.kangbuk.common.My;
import hwang.daemin.kangbuk.common.MyVolley;
import hwang.daemin.kangbuk.main.MainActivity;

/**
 * Created by user on 2016-06-11.
 */
public class VideoPostActivity extends AppCompatActivity {

    ImageView ivThumb;
    EditText etTitle;
    String videoURL;
    String videoId;
    FirebaseAuth auth;
    DatabaseReference Ref;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videopost);
        My.INFO.backKeyName ="VideoPostActivity";
        fab = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        ivThumb = (ImageView) findViewById(R.id.ivThumb);
        etTitle = (EditText) findViewById(R.id.etTitle);
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        auth = FirebaseAuth.getInstance();
        Ref = FirebaseDatabase.getInstance().getReference();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } /*else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }*/
        } /*else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        }*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                String uId;
                if(auth.getCurrentUser()==null) startActivity(new Intent(VideoPostActivity.this, SignInActivity.class));
                else {
                    uId = auth.getCurrentUser().getUid();
                    YoutubeVideo yv = new YoutubeVideo(videoId, etTitle.getText().toString(), uId);
                    Ref.child("youtube").push().setValue(yv);
                    finish();
                    Intent i = new Intent(VideoPostActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }
        });
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            videoURL = "http://www.youtube.com/oembed?url=" +sharedText +"&format=json";
            videoId = sharedText.split("e\\/")[1];
            getVideoTitle(VideoPostActivity.this, videoURL);
        }
    }
/*
    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.i("test",imageUri.getPath());
        }
    }
    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }*/
    public void getVideoTitle(final Context context, String videoURL) {
        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST,
                videoURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            etTitle.setText(response.getString("title"));
                            GlideUtil.loadImage(response.getString("thumbnail_url"),ivThumb);
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
