package hwang.daemin.kangbuk.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by user on 2016-06-14.
 */
public class ColumnData {
    @JsonProperty("products")
    private List<Product> products;
    @JsonProperty("success")
    private int success;
    @JsonProperty("loginstate")
    private int loginstate;
    public static class Product{
        @JsonProperty("num")
        private String num;
        @JsonProperty("title")
        private String title;
        @JsonProperty("content")
        private String content;
        @JsonProperty("bible_content")
        private String bible_content;
        @JsonProperty("date")
        private String date;
        @JsonCreator
        public Product(@JsonProperty("num") String num,
                       @JsonProperty("title") String title,
                       @JsonProperty("content") String content,
                       @JsonProperty("bible_content") String bible_content,
                       @JsonProperty("date") String date) {
            this.num = num;
            this.title = title;
            this.content = content;
            this.bible_content = bible_content;
            this.date = date;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getBible_content() {
            return bible_content;
        }

        public void setBible_content(String bible_content) {
            this.bible_content = bible_content;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getLoginstate() {
        return loginstate;
    }

    public void setLoginstate(int loginstate) {
        this.loginstate = loginstate;
    }

    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }
    @JsonCreator
    public ColumnData(@JsonProperty("products") List<Product> products,
                      @JsonProperty("success") int success,
                      @JsonProperty("loginstate") int loginstate) {
        this.products = products;
        this.success = success;
        this.loginstate = loginstate;
    }

}
