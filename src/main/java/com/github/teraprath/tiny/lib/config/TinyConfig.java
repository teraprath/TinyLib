package com.github.teraprath.tiny.lib.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;

public abstract class TinyConfig {

    private final JavaPlugin plugin;
    private final String fileName;
    private File file;
    private FileConfiguration config;

    public TinyConfig(@Nonnull JavaPlugin plugin, @Nonnull String fileName) {
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
            config.save(file);
            reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void onLoad(FileConfiguration config);

}
