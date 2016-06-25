package hwang.daemin.kangbuk.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.firebase.fUtil;

/**
 * Created by user on 2016-06-11.
 */
public class DetailActivity extends AppCompatActivity {
    ImageView ivDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String uId = getIntent().getStringExtra("uId");
        ivDetail = (ImageView) findViewById(R.id.ivDetail);

        fUtil.databaseReference.child("user").child(uId).child("fullPhotoURL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fullPhotoURL = (String) dataSnapshot.getValue();
                ColorDrawable cd = new ColorDrawable(ContextCompat.getColor(DetailActivity.this, R.color.white));
                Glide.with(DetailActivity.this)
                        .load(fullPhotoURL)
                        .placeholder(cd)
                        .crossFade()
                        .centerCrop()
                        .into(ivDetail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(ivDetail);
    }
}
