package com.github.teraprath.tiny.lib.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;

public class TinySkull {

    private final ItemStack itemStack;

    public TinySkull(@Nonnull String owner) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer("owner"));
        this.itemStack.setItemMeta(meta);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
