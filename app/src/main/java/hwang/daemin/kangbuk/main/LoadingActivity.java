package hwang.daemin.kangbuk.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.auth.SignInActivity;

/**
 * Created by user on 2016-06-11.
 */
public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent i = new Intent(LoadingActivity.this, SignInActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2200);
            return;
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent i = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, 2200);
        }

    }
}
