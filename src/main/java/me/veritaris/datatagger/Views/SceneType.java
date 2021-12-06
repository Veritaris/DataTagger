package me.veritaris.datatagger.Views;

public enum SceneType {
    MAIN("mainView", "Dataset tagging tool"),
    IMAGE_TEXT("textImageTaggingView", "Image tagging");

    public String fileName;
    public String title;

    SceneType(String fileName, String title) {
        this.fileName = fileName;
        this.title = title;
    }
}
