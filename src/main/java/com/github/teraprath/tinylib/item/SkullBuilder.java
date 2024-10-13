package com.github.teraprath.tinylib.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;

public class SkullBuilder {

    private final ItemStack itemStack;

    /**
     * Constructs a SkullBuilder for creating a player head item.
     * The head will be associated with the specified player's name.
     *
     * @param owner The name of the player whose head will be represented.
     */
    public SkullBuilder(@Nonnull String owner) {
        this.itemStack = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) this.itemStack.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer("owner"));
        this.itemStack.setItemMeta(meta);
    }

    /**
     * Finalizes and returns the player head ItemStack.
     *
     * @return The constructed player head ItemStack with the owner's data applied.
     */
    public ItemStack build() {
        return itemStack;
    }
}