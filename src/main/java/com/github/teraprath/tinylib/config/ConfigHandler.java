package com.github.teraprath.tinylib.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public abstract class ConfigHandler {

    protected final JavaPlugin plugin;
    private final String fileName;
    private File file;
    private FileConfiguration config;

    /**
     * Constructs a new ConfigHandler for managing configuration files.
     *
     * @param plugin   The plugin instance that this configuration is associated with.
     * @param fileName The name of the configuration file (without extension).
     */
    public ConfigHandler(@Nonnull JavaPlugin plugin, @Nonnull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";
    }

    /**
     * Initializes the configuration handler by loading the file from the plugin's data folder.
     * If the file doesn't exist, it is created.
     */
    public void init() {
        this.file = new File(plugin.getDataFolder(), this.fileName);
        reload();
    }

    /**
     * Reloads the configuration file.
     * If the file does not exist, it will be created and loaded from the plugin's resources.
     * The onLoad method is called to allow custom loading behavior.
     */
    public void reload() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(this.fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        onLoad(this.config);
    }

    /**
     * Saves the configuration to disk.
     * Calls the onPreSave method before saving to allow for any last-minute changes.
     * If an error occurs during saving, a RuntimeException is thrown.
     */
    public void save() {
        try {
            onPreSave(config);
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when the configuration is loaded.
     * Subclasses should implement this method to define custom loading behavior.
     *
     * @param config The configuration that was loaded.
     */
    public abstract void onLoad(FileConfiguration config);

    /**
     * Called just before the configuration is saved.
     * Subclasses should implement this method to define any actions to be taken before saving.
     *
     * @param config The configuration that is about to be saved.
     */
    public abstract void onPreSave(FileConfiguration config);

}