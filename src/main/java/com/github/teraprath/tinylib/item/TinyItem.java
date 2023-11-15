package com.github.teraprath.tinylib.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TinyItem {

    private ItemStack itemStack;
    private ItemMeta meta;

    public TinyItem(@Nonnull Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
    }

    public TinyItem setItemMeta(@Nonnull ItemMeta itemMeta) {
        this.meta = itemMeta;
        return this;
    }

    public TinyItem setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public TinyItem setType(@Nonnull Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public TinyItem setName(@Nonnull String displayName) {
        this.meta.displayName(Component.text(displayName));
        return this;
    }

    public TinyItem setLore(@Nonnull String... strings) {
        List<Component> lore = new ArrayList<>();
        for (String string : strings) {
            lore.add(Component.text(string));
        }
        this.meta.lore(lore);
        return this;
    }

    public TinyItem setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    public TinyItem addEnchant(@Nonnull Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    public TinyItem removeEnchant(@Nonnull Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    public TinyItem addItemFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    public TinyItem removeItemFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    public ItemStack get() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

}
