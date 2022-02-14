package me.veritaris.datatagger.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;

public class Model {
    private static final Model instance = new Model();
    private static Metadata metadata;
    private static File file;
    private static Path datasetPath;
    private static GitHandler gitHandler;

    private Model() {

    }

    public boolean loadMetadata(File file) {
        Model.datasetPath = file.toPath();
        Model.file = new File(file.getAbsolutePath() + "/metadata.yml");

        try {
            metadata = MetadataHandler.getInstance(new File(file.toPath().toAbsolutePath() + "/metadata.yml")).getMetadata();
            return true;
        } catch (FileNotFoundException exception) {
            try {
                new File(file.getAbsolutePath(), "metadata.yml").createNewFile();
                metadata = MetadataHandler.getInstance(new File(file.toPath().toAbsolutePath() + "/metadata.yml")).getMetadata();
                if (metadata.isGitSavingEnabled()) {
                    initGit();
                }
                return true;
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        }
    }

    public static GitHandler getGitHandler() {
        return gitHandler;
    }

    public static Metadata getMetadata() {
        return metadata;
    }

    public static String getDatasetPath() {
        return Model.datasetPath.toAbsolutePath().toString();
    }

    public static String getLastTaggedImagePath() {
        return String.format("%s/%s/image.png", getDatasetPath(), metadata.getLastTaggedImage());
    }

    public static void writeImageTag(String tagText) {
        try {
            Files.write(
                new File(String.format("%s/%s/solution", getDatasetPath(), metadata.getLastTaggedImage())).toPath(),
                Collections.singleton(tagText)
            );
            Map<Integer, String> newTagged = metadata.getTaggedImages();
            newTagged.put(metadata.getLastTaggedImage(), tagText);
            metadata.setTaggedImages(newTagged);
            metadata.setLastTaggedImage(metadata.getLastTaggedImage() + 1);
            Model.getInstance().saveProgress();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static File getMetadataFile() {
        return file;
    }

    public static Model getInstance() {
        return instance;
    }

    public void saveProgress() {
        try {
            MetadataHandler.getInstance(Model.file).dumpMetadata(metadata, file.toPath());
            makeBackup();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void makeBackup() {
        try {
            MetadataHandler.getInstance(Model.file).dumpMetadata(metadata, Paths.get(file.getParent() + "/.metadata.yml"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void initGit() {
        gitHandler = new GitHandler(new File(Model.datasetPath.toFile() + "/.git"));
    }

    public void loadProgress() {
        loadMetadata(Model.file);
    }
}
