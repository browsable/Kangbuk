package hwang.daemin.kangbuk.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by user on 2016-06-14.
 */
public class ColumnData {
    @JsonProperty("success")
    private int success;
    @JsonProperty("products")
    private List<Products> products;
    public static class Products{
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

        public Products(String num, String title, String content, String bible_content, String date) {
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

    public List<Products> getProducts() {
        return products;
    }
    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ColumnData(int success, List<Products> products) {
        this.success = success;
        this.products = products;
    }
}
