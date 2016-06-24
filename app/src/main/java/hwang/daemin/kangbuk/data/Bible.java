package hwang.daemin.kangbuk.data;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Bible {

    public String pos;
    public String ko;
    public String en;

    public Bible() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Bible(String pos, String ko, String en) {
        this.pos = pos;
        this.ko = ko;
        this.en = en;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getKo() {
        return ko;
    }

    public void setKo(String ko) {
        this.ko = ko;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
// [END comment_class]
