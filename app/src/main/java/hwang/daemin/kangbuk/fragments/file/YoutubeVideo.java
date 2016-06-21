package hwang.daemin.kangbuk.fragments.file;

/**
 * Created by user on 2016-06-20.
 */
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author msahakyan
 */
public class YoutubeVideo implements Parcelable {

    private String videoId;
    private String title;
    private String uId;

    public YoutubeVideo(String videoId, String title, String uId) {
        this.videoId = videoId;
        this.title = title;
        this.uId = uId;
    }

    private YoutubeVideo(Parcel in) {
        videoId = in.readString();
        title = in.readString();
    }
    private YoutubeVideo() {
    }

    public static final Creator<YoutubeVideo> CREATOR = new Creator<YoutubeVideo>() {
        @Override
        public YoutubeVideo createFromParcel(Parcel in) {
            return new YoutubeVideo(in);
        }

        @Override
        public YoutubeVideo[] newArray(int size) {
            return new YoutubeVideo[size];
        }
    };

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(title);
    }
}