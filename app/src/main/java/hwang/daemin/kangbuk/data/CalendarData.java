package hwang.daemin.kangbuk.data;

/**
 * Created by user on 2016-06-14.
 */
public class CalendarData {
    private int year;
    private int month;
    private int day;
    private String title;

    public CalendarData() {
    }

    public CalendarData(int year, int month, int day, String title) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
