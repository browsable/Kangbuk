package hwang.daemin.kangbuk.data;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PictureData {

    public String uid;
    public String uName;
    public String title;
    public String body;
    public String thumbURL;
    public long time;

    public PictureData() {
    }

    public PictureData(String uid, String uName, String title, String body, String thumbURL, Long time) {
        this.uid = uid;
        this.uName = uName;
        this.title = title;
        this.body = body;
        this.thumbURL = thumbURL;
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getThumbURL() {
        return thumbURL;
    }

    public void setThumbURL(String thumbURL) {
        this.thumbURL = thumbURL;
    }

}
