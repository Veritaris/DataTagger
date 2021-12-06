package me.veritaris.datatagger.Model;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;


public class ConfigHandler {
    public static Path configPath;

    private static ConfigHandler configHandler = null;

    Config config;


    public static ConfigHandler getInstance(File file) throws FileNotFoundException {
        configPath = file.toPath();
        return getInstance(file.toPath());
    }


    public static ConfigHandler getInstance(Path configPath) throws FileNotFoundException {
        if (configHandler == null) {
            configHandler = new ConfigHandler(configPath);
        }
        return configHandler;
    }


    private ConfigHandler(Path configPath) throws FileNotFoundException {
        this.config = loadConfig(configPath);

        if (this.config == null) {
            writeDefaultConfig(configPath);
        }

        this.config = loadConfig(configPath);
    }

    private void writeDefaultConfig(Path path) {
        try {
            Files.write(path, Collections.singletonList("!!me.veritaris.datatagger.Model.Config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Config loadConfig(Path configPath) throws FileNotFoundException {
        Constructor constructor = new Constructor(Config.class);
        Yaml yaml = new Yaml(constructor);
        return yaml.load(new FileInputStream(configPath.toFile()));
    }


    public void dumpConfig() throws IllegalArgumentException, IllegalAccessException, IOException {
        dumpConfig(this.config, ConfigHandler.configPath);
    }


    public void dumpConfig(Config config, Path configPath) throws IllegalArgumentException, IllegalAccessException, IOException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        Yaml yml = new Yaml(options);
        yml.dump(config, new FileWriter(configPath.toFile()));
    }


    public Config getConfig() {
        return this.config;
    }

}
