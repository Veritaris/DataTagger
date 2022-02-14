package me.veritaris.datatagger.Model;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;

public class MetadataHandler {
    private static MetadataHandler metadataHandler = null;
    public static Path metadataPath;

    private Metadata metadata;


    public static MetadataHandler getInstance(File file) throws FileNotFoundException {
        metadataPath = file.toPath();
        return getInstance(file.toPath());
    }


    public static MetadataHandler getInstance(Path configPath) throws FileNotFoundException {
        if (metadataHandler == null) {
            metadataHandler = new MetadataHandler(configPath);
        }
        return metadataHandler;
    }


    private MetadataHandler(Path metadataPath) throws FileNotFoundException {
        this.metadata = loadMetadata(metadataPath);

        if (this.metadata == null) {
            writeDefaultMetadata(metadataPath);
        }

        this.metadata = loadMetadata(metadataPath);
    }

    private void writeDefaultMetadata(Path path) {
        try {
            Files.write(path, Arrays.asList("!!me.veritaris.datatagger.Model.Metadata", "datasetName: "+path.getParent(), "gitSavingEnabled: false", "imagesAmount: 0", "lastTaggedImage: 0", "taggedImages: {}", "repeatedImages: {}"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Metadata loadMetadata(Path metadataPath) throws FileNotFoundException {
        Constructor constructor = new Constructor(Metadata.class);
        Yaml yaml = new Yaml(constructor);
        return yaml.load(new FileInputStream(metadataPath.toFile()));
    }


    public void dumpMetadata() throws IllegalArgumentException, IOException {
        dumpMetadata(this.metadata, MetadataHandler.metadataPath);
    }


    public void dumpMetadata(Metadata metadata, Path metadataPath) throws IllegalArgumentException, IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(metadata, new FileWriter(metadataPath.toFile()));
    }


    public Metadata getMetadata() {
        return this.metadata;
    }

}
