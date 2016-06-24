package hwang.daemin.kangbuk.data;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {

    public String uid;
    public String author;
    public String text;
    public Long time;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text , Long time) {
        this.uid = uid;
        this.author = author;
        this.text = text;
        this.time = time;
    }

}
// [END comment_class]
