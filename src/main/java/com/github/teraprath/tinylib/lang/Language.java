package com.github.teraprath.tinylib.lang;

import com.github.teraprath.tinylib.config.ConfigHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Language extends ConfigHandler {

    private final Map<String, String> variables = new HashMap<>();
    private final Map<String, Component> messages = new HashMap<>();
    private String prefix;

    /**
     * Constructs a new Language handler for managing message localization.
     *
     * @param plugin The plugin instance for which the language system is managed.
     */
    public Language(@NotNull JavaPlugin plugin) {
        super(plugin, "language");
    }

    /**
     * This method is called when the configuration file is loaded.
     * It reads and processes the messages and variables from the configuration file.
     *
     * @param config The configuration file that is loaded.
     */
    @Override
    public void onLoad(FileConfiguration config) {
        // Clear any previously loaded messages
        this.messages.clear();
        // Retrieve the prefix from the configuration
        this.prefix = config.getString("prefix");

        // Load variables and messages from their respective sections in the configuration
        ConfigurationSection variables = config.getConfigurationSection("var");
        ConfigurationSection messages = config.getConfigurationSection("messages");

        // Load variables from the configuration, if they exist
        if (variables != null) {
            for (String key : variables.getKeys(false)) {
                String value = variables.getString(key);
                if (value != null) {
                    this.variables.put(key, value);
                }
            }
        }

        // Load messages and replace placeholders, if they exist
        if (messages != null) {
            for (String key : messages.getKeys(false)) {
                String message = messages.getString(key);
                if (message != null) {
                    // Replace <prefix> placeholder with the actual prefix if it exists
                    if (message.contains("<prefix>")) {
                        if (prefix != null) {
                            message = message.replace("<prefix>", prefix);
                        }
                    }

                    // Replace custom variables in the message
                    AtomicReference<String> result = new AtomicReference<>(message);
                    this.variables.forEach((current, value) -> {
                        String placeholder = "<var:" + current + ">";
                        if (result.get().contains(placeholder)) {
                            result.set(result.get().replace(placeholder, value));
                        }
                    });

                    // Deserialize the message using MiniMessage for text formatting
                    MiniMessage miniMessage = MiniMessage.miniMessage();
                    this.messages.put(key, miniMessage.deserialize(result.get()));
                }
            }
        }

        // Clear variables after loading messages
        this.variables.clear();
    }

    /**
     * This method is called before saving the configuration.
     * In this case, it does nothing, but can be overridden by subclasses if needed.
     *
     * @param config The configuration file that will be saved.
     */
    @Override
    public void onPreSave(FileConfiguration config) {}

    /**
     * Retrieves the localized message associated with the given key.
     * If the message is not found, the key itself is returned as a fallback.
     *
     * @param key The key for the message in the configuration.
     * @return A Component containing the localized message or the key as text if not found.
     */
    public Component get(@Nonnull String key) {
        Component message = this.messages.get(key);
        if (message == null) {
            return Component.text(key);
        }
        return message;
    }

}