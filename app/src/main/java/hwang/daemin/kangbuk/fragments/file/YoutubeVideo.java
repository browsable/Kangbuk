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
    private String name;

    public YoutubeVideo(String videoId, String name) {
        this.videoId = videoId;
        this.name = name;
    }

    private YoutubeVideo(Parcel in) {
        videoId = in.readString();
        name = in.readString();
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

    public String getVideoId() {
        return videoId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoId);
        dest.writeString(name);
    }
}