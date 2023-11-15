package com.github.teraprath.tinylib.lang;

import com.github.teraprath.tinylib.text.TinyText;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class MultiLanguage {

    private final JavaPlugin plugin;
    private final String folderName;
    private final HashMap<String, Language> locales;
    private final String fallback;

    public MultiLanguage(@Nonnull JavaPlugin plugin, @Nonnull String folderName, @Nonnull String fallback) {
        this.plugin = plugin;
        this.folderName = folderName;
        this.locales = new HashMap<>();
        this.fallback = fallback;
    }

    public MultiLanguage init() {
        File folder = new File(plugin.getDataFolder(), folderName);
        folder.mkdirs();
        File[] languages = folder.listFiles();
        assert languages != null;
        if (languages.length > 0) {
            for (File file : languages) {
                String locale = file.getName().replace(".yml", "");
                Language language = new Language(plugin, folderName + "/" + locale);
                language.init();
                this.locales.put(locale, language);
                plugin.getLogger().info("Locale " + locale + " loaded in " + folderName + "/" + locale + ".yml");
            }
        } else {
            Language language = new Language(plugin, folderName + "/" + fallback.toLowerCase());
            language.init();
            this.locales.put(fallback, language);
        }
        reload();
        return this;
    }

    public void reload() {
        this.locales.forEach((locale, language) -> {
            language.reload();
        });
    }

    public void sendMessage(@Nonnull Player player, @Nonnull String key) {
        player.sendMessage(new TinyText(getMessage(player, key)).toString());
    }

    public String getMessage(@Nonnull Player player, @Nonnull String key) {
        return getMessage(player.locale(), key);
    }

    public String getMessage(@Nonnull Locale locale, @Nonnull String key) {
        Language language = this.locales.get(locale.toString().toLowerCase());
        if (language == null) {
            language = this.locales.get(fallback);
        }
        return language.getMessage(key);
    }
}
