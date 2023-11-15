package com.github.teraprath.tinylib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class TinyCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return this.onExecute(sender, command, s, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        String current = args[args.length - 1].toLowerCase();

        List<String> list = this.onTab(sender, command, s, args);

        list.removeIf(string -> !string.toLowerCase().startsWith(current));
        return list;
    }

    public abstract boolean onExecute(CommandSender sender, Command command, String s, String[] args);
    public abstract List<String> onTab(CommandSender sender, Command command, String s, String[] args);

}
