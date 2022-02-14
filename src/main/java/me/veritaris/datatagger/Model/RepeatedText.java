package me.veritaris.datatagger.Model;


public class RepeatedText {
    private String text;
    private Integer[] ids;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }
}
