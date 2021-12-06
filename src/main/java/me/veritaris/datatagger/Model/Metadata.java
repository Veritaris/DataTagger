package me.veritaris.datatagger.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Metadata {
    private String datasetName;
    private int imagesAmount;
    private int lastTaggedImage;
    private Map<Integer, String> taggedImages = new HashMap<>();
    private List<Integer> repeatedImages = new ArrayList<>();

    public Metadata() {}

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

    public List<Integer> getRepeatedImages() {
        return repeatedImages;
    }

    public void setRepeatedImages(List<Integer> repeatedImages) {
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
