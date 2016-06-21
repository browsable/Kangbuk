package hwang.daemin.kangbuk.common;

import android.app.Activity;
import android.app.FragmentManager;
import android.widget.Toast;

import hwang.daemin.kangbuk.R;
import hwang.daemin.kangbuk.fragments.ColumnFragment;
import hwang.daemin.kangbuk.fragments.MainFragment;
import hwang.daemin.kangbuk.fragments.file.YoutubeFragment;

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
            case "MainFragment":
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
            default:
                My.INFO.backKeyName="MainFragment";
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