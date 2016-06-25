/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hwang.daemin.kangbuk.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.data.User;
import hwang.daemin.kangbuk.firebase.fUtil;
import hwang.daemin.kangbuk.main.MainActivity;

public class EmailPasswordActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "EmailPassword";
    private EditText etEmail;
    private EditText etPW;
    private EditText etName;
    private ProgressBar bar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        // Views
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPW = (EditText) findViewById(R.id.etPW);
        etName = (EditText) findViewById(R.id.etName);
        bar = (ProgressBar) findViewById(R.id.progressBar);
        // Buttons
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);
        findViewById(R.id.email_create_account_button).setOnClickListener(this);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // My is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // My is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        fUtil.firebaseAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            fUtil.firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void createAccount(final String name, final String email, final String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        bar.setVisibility(View.VISIBLE);
        fUtil.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(EmailPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(EmailPasswordActivity.this, getString(R.string.auth_creat_failed),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPreferences pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
                            pref.edit().putInt("loginType",2).apply();
                            fUtil.firebaseUser = task.getResult().getUser();
                            Random r = new Random();
                            String bibleNum = String.valueOf(r.nextInt(239));
                            fUtil.getUserRef().child(fUtil.firebaseUser.getUid()).setValue(new User(name,null,null,bibleNum));
                            Intent i = new Intent(EmailPasswordActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        }
                        // [START_EXCLUDE]
                        bar.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });


    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        bar.setVisibility(View.VISIBLE);

        // [START sign_in_with_email]
        fUtil.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, getString(R.string.auth_sigin_failed),
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            SharedPreferences pref =  getSharedPreferences("USERINFO", MODE_PRIVATE);
                            pref.edit().putInt("loginType",2).commit();
                            finish();
                            startActivity(new Intent(EmailPasswordActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        }

                        // [START_EXCLUDE]
                        bar.setVisibility(View.GONE);
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPW.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPW.setError("Required.");
            valid = false;
        } else {
            etPW.setError(null);
        }
        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            etName.setError("Required.");
            valid = false;
        } else {
            etName.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_create_account_button:
                createAccount(etName.getText().toString(),etEmail.getText().toString(), etPW.getText().toString());
                break;
            case R.id.email_sign_in_button:
                signIn(etEmail.getText().toString(), etPW.getText().toString());
                break;
        }
    }
}
