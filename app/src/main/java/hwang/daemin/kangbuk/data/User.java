
package hwang.daemin.kangbuk.data;

public class User {
    private String uName;
    private String fullPhotoURL;
    private String thumbPhotoURL;
    private String biblenum;

    public User() {

    }
    public User(String fullPhotoURL, String thumbPhotoURL) {
        this.fullPhotoURL = fullPhotoURL;
        this.thumbPhotoURL = thumbPhotoURL;
    }
    public User(String uName, String fullPhotoURL, String thumbPhotoURL, String biblenum) {
        this.uName = uName;
        this.fullPhotoURL = fullPhotoURL;
        this.thumbPhotoURL = thumbPhotoURL;
        this.biblenum = biblenum;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getFullPhotoURL() {
        return fullPhotoURL;
    }

    public void setFullPhotoURL(String fullPhotoURL) {
        this.fullPhotoURL = fullPhotoURL;
    }

    public String getThumbPhotoURL() {
        return thumbPhotoURL;
    }

    public void setThumbPhotoURL(String thumbPhotoURL) {
        this.thumbPhotoURL = thumbPhotoURL;
    }

    public String getBiblenum() {
        return biblenum;
    }

    public void setBiblenum(String biblenum) {
        this.biblenum = biblenum;
    }
}
