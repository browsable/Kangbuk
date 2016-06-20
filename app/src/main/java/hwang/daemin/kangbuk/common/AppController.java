package hwang.daemin.kangbuk.common;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by hernia on 2015-06-13.
 */
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyVolley.init(this);
        getAppVer();
    }
    private void getAppVer(){
        PackageInfo pInfo;
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(
                    getApplicationContext().getPackageName(), 0);
            My.INFO.appVer = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
