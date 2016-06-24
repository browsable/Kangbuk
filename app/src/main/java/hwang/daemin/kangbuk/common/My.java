package hwang.daemin.kangbuk.common;
/**
 * Created by hernia on 2015-06-27.
 */
public enum My {
    INFO();
    My(){
        name = "anonymous";
    }

    public int loginType; //0:google 1:facebook 2:email 3: anonymous
    public String name;
    public String id;
    public String appVer;
    public String photoUrl;
    public String backKeyName;
}
