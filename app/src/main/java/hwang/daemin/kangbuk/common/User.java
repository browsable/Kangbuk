package hwang.daemin.kangbuk.common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hernia on 2015-06-27.
 */
public enum User {
    INFO();
    User(){
        UserName = "anonymous";
    }

    public int loginType; //0:google 1:facebook 2:email 3: anonymous
    public String UserName;
    public String appVer;
    public String PhotoUrl;
}
