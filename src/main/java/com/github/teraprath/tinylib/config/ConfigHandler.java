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

    public ConfigHandler(@Nonnull JavaPlugin plugin, @Nonnull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName + ".yml";
    }

    public void init() {
        this.file = new File(plugin.getDataFolder(), this.fileName);
        reload();
    }

    public void reload() {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(this.fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
        onLoad(this.config);
    }

    public void save() {
        try {
            onPreSave(config);
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void onLoad(FileConfiguration config);
    public abstract void onPreSave(FileConfiguration config);

}
