package com.github.teraprath.tinylib.text;

import org.bukkit.ChatColor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TinyText {

    private final StringBuilder result = new StringBuilder();
    private final HashMap<String, String> placeholder;

    public TinyText(@Nonnull String string) {
        this.placeholder = new HashMap<>();
        this.result.append(string);
    }

    public TinyText value(@Nonnull String placeholder, Object value) {
        this.placeholder.put(placeholder, value.toString());
        return this;
    }

    public TinyText add(@Nonnull String string) {
        this.result.append(string);
        return this;
    }

    public TinyText delete(@Nonnull String string) {
        int index = this.result.indexOf(string);
        this.result.delete(index, index + string.length());
        return this;
    }


    public String toString() {

        final String[] message = {this.result.toString()};

        this.placeholder.forEach((p, val) -> {
            message[0] = message[0].replace("%" + p + "%", val);
        });

        return hex(message[0]);
    }

    private static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}