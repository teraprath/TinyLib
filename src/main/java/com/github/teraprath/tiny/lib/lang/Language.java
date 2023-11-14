package com.github.teraprath.tiny.lib.lang;

import com.github.teraprath.tiny.lib.config.TinyConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

public class Language extends TinyConfig {

    private HashMap<String, String> messages;

    public Language(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        super(plugin, fileName);
        this.messages = new HashMap<>();
    }

    @Override
    public void onLoad(FileConfiguration config) {
        Set<String> keys = config.getKeys(true);
        keys.forEach(message -> {
            this.messages.put(message, config.getString(message));
        });
    }

    public String getMessage(@Nonnull String key) {
        return this.messages.get(key);
    }

}
