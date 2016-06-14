package hwang.daemin.kangbuk.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.auth.SignInActivity;
import hwang.daemin.kangbuk.common.User;
import hwang.daemin.kangbuk.fragments.BibleFragment;
import hwang.daemin.kangbuk.fragments.CalendarFragment;
import hwang.daemin.kangbuk.fragments.ColumnFragment;
import hwang.daemin.kangbuk.fragments.IntroFragment;
import hwang.daemin.kangbuk.fragments.MainFragment;
import hwang.daemin.kangbuk.fragments.PictureFragment;
import hwang.daemin.kangbuk.fragments.PlaceFragment;
import hwang.daemin.kangbuk.fragments.ScheduleFragment;
import hwang.daemin.kangbuk.fragments.WeekFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.OnConnectionFailedListener {
    public static final String ANONYMOUS = "anonymous";
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_INVITE = 1;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getSharedPreferences("USERINFO", MODE_PRIVATE);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        User.INFO.loginType = pref.getInt("loginType",0);
        if(mFirebaseUser==null){
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }else{
            if( User.INFO.loginType==0|| User.INFO.loginType==1) { //google, facebook
                User.INFO.UserName = mFirebaseUser.getDisplayName();
            }else if( User.INFO.loginType==2){ //email
                User.INFO.UserName = pref.getString("UserName","anonymous");
            }else if( User.INFO.loginType==3){ //anonymous
                User.INFO.UserName = ANONYMOUS;
            }
            /*if(mFirebaseUser.getPhotoUrl() != null){
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }*/
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(AppInvite.API)
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        // Initialize Firebase Auth

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                mFirebaseAuth.signOut();
                if(User.INFO.loginType==0)
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                else if(User.INFO.loginType==1)
                    LoginManager.getInstance().logOut();
                User.INFO.UserName = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                return true;
            case R.id.invite_menu:
                sendInvitation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();
        // Handle navigation view item clicks here.
        switch (item.getItemId()){
            case R.id.nav_notice:
                fm.beginTransaction().replace(R.id.content_frame,new MainFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_notice));
                break;
            case R.id.nav_bible:
                fm.beginTransaction().replace(R.id.content_frame,new BibleFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_bible));
                break;
            case R.id.nav_week:
                fm.beginTransaction().replace(R.id.content_frame,new WeekFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_week));
                break;
            case R.id.nav_column:
                fm.beginTransaction().replace(R.id.content_frame,new ColumnFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_column));
                break;
            case R.id.nav_calendar:
                fm.beginTransaction().replace(R.id.content_frame,new CalendarFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_calendar));
                break;
            case R.id.nav_picture:
                fm.beginTransaction().replace(R.id.content_frame,new PictureFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_picture));
                break;
            case R.id.nav_schedule:
                fm.beginTransaction().replace(R.id.content_frame,new ScheduleFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_schedule));
                break;
            case R.id.nav_intro:
                fm.beginTransaction().replace(R.id.content_frame,new IntroFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_intro));
                break;
            case R.id.nav_place:
                fm.beginTransaction().replace(R.id.content_frame,new PlaceFragment()).commit();
                getSupportActionBar().setTitle(getString(R.string.nav_place));
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    private void sendInvitation() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode +
                ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Check how many invitations were sent.
                String[] ids = AppInviteInvitation
                        .getInvitationIds(resultCode, data);
                Log.d(TAG, "Invitations sent: " + ids.length);
            } else {
                // Sending failed or it was canceled, show failure message to
                // the user
                Log.d(TAG, "Failed to send invitation.");
            }
        }
    }
}
