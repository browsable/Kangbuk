package hwang.daemin.kangbuk.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by user on 2016-06-14.
 */
public class ScheduleData {
    private String title;
    private String time;

    public ScheduleData(String title, String time) {
        this.title = title;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
