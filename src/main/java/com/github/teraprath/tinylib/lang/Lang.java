package com.github.teraprath.tinylib.lang;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

import javax.annotation.Nonnull;

public class Lang {

    public static Component get(@Nonnull String key) {
        return MiniMessage.miniMessage().deserialize("<lang:" + key + ">");
    }

    public static Component parse(@Nonnull String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static TextReplacementConfig replace(@Nonnull String string, @Nonnull String replacement) {
        return TextReplacementConfig.builder().matchLiteral(string).replacement(replacement).build();
    }

}
