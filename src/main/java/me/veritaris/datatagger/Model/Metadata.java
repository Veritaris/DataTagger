package me.veritaris.datatagger.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Metadata {
    private String datasetName;
    private int imagesAmount;
    private int lastTaggedImage;
    private boolean gitSavingEnabled;
    private Map<Integer, String> taggedImages = new HashMap<>();
    private Map<String, List<Integer>> repeatedImages = new HashMap<String, List<Integer>>();

    public Metadata() {
    }

    public boolean isGitSavingEnabled() {
        return gitSavingEnabled;
    }

    public void setGitSavingEnabled(boolean gitSavingEnabled) {
        this.gitSavingEnabled = gitSavingEnabled;
    }

    public int getLastTaggedImage() {
        return lastTaggedImage;
    }

    public void setLastTaggedImage(int lastTaggedImage) {
        this.lastTaggedImage = lastTaggedImage;
    }

    public Map<Integer, String> getTaggedImages() {
        return taggedImages;
    }

    public void setTaggedImages(Map<Integer, String> taggedImages) {
        this.taggedImages = taggedImages;
    }

    public Map<String, List<Integer>> getRepeatedImages() {
        return repeatedImages;
    }

    public void setRepeatedImages(Map<String, List<Integer>> repeatedImages) {
        this.repeatedImages = repeatedImages;
    }

    public int getImagesAmount() {
        return imagesAmount;
    }

    public void setImagesAmount(int imagesAmount) {
        this.imagesAmount = imagesAmount;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }
}
