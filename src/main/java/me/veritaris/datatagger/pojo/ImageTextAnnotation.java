package me.veritaris.datatagger.pojo;


public class ImageTextAnnotation implements Annotation {
    private final int id;
    private final String annotation;


    public ImageTextAnnotation(int id, String text) {
        this.id = id;
        this.annotation = text;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.id, this.annotation);
    }

    public String getAnnotation() {
        return annotation;
    }

    public int getId() {
        return id;
    }
}
