package hwang.daemin.kangbuk.common;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.widget.Toast;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.fragments.ColumnFragment;
import hwang.daemin.kangbuk.fragments.MainFragment;
import hwang.daemin.kangbuk.fragments.file.VideoPostActivity;
import hwang.daemin.kangbuk.fragments.file.YoutubeActivity;
import hwang.daemin.kangbuk.main.MainActivity;

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;
    private Activity activity;
    private Toast toast;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }
    public void onBackPressed(String name) {
        FragmentManager fm = activity.getFragmentManager();
        switch (name) {
            case "MainFragment":case "MainActivity":
                    if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                        backKeyPressedTime = System.currentTimeMillis();
                        showGuide();
                        return;
                    }
                    if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                        activity.finish();
                        toast.cancel();
                    }
                break;
            case "ColumnDetailFragment":
                fm.beginTransaction().replace(R.id.content_frame,new ColumnFragment()).commit();
                break;
            case "YoutubeActivity":
                Intent in= new Intent(activity.getApplicationContext(), MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(in);
                break;
            case "VideoPostActivity":
                Intent i = new Intent(activity.getApplicationContext(), YoutubeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(i);
                break;
            default:
                fm.beginTransaction().replace(R.id.content_frame,new MainFragment()).commit();
                break;
            }
        }
        public void showGuide() {
            toast = Toast.makeText(activity, activity.getResources().getString(R.string.backpress)
                    , Toast.LENGTH_SHORT);
            toast.show();
        }
}