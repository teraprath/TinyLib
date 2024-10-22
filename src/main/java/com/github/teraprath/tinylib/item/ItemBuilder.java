package com.github.teraprath.tinylib.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta meta;

    /**
     * Constructs an ItemBuilder for creating a new item from a specified material.
     *
     * @param material The type of material the item will be made of.
     */
    public ItemBuilder(@Nonnull Material material) {
        this.itemStack = new ItemStack(material);
        this.meta = itemStack.getItemMeta();
    }

    /**
     * Constructs an ItemBuilder from an existing ItemStack.
     *
     * @param itemStack The existing item stack to modify.
     */
    public ItemBuilder(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
        this.meta = itemStack.getItemMeta();
    }

    /**
     * Sets the ItemMeta for the item, which includes various properties like name, lore, etc.
     *
     * @param itemMeta The item meta to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setItemMeta(@Nonnull ItemMeta itemMeta) {
        this.meta = itemMeta;
        return this;
    }

    /**
     * Sets the amount of the item in the stack.
     *
     * @param amount The number of items in the stack.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    /**
     * Changes the type of material for the item.
     *
     * @param material The new material to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setType(@Nonnull Material material) {
        this.itemStack.setType(material);
        return this;
    }

    /**
     * Sets the display name of the item.
     *
     * @param component The Component that represents the item's display name.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setName(@Nonnull Component component) {
        this.meta.displayName(component);
        return this;
    }

    /**
     * Sets the lore (description text) of the item.
     *
     * @param components The components that represent each line of the item's lore.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setLore(@Nonnull Component... components) {
        List<Component> lore = new ArrayList<>(Arrays.asList(components));
        this.meta.lore(lore);
        return this;
    }

    public ItemBuilder setLore(@Nonnull List<Component> lore) {
        this.meta.lore(lore);
        return this;
    }

    /**
     * Sets whether the item is unbreakable.
     *
     * @param unbreakable True to make the item unbreakable, false otherwise.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.meta.setUnbreakable(unbreakable);
        return this;
    }

    /**
     * Adds an enchantment to the item.
     *
     * @param enchantment The enchantment to add.
     * @param level       The level of the enchantment.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder addEnchant(@Nonnull Enchantment enchantment, int level) {
        this.meta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Removes an enchantment from the item.
     *
     * @param enchantment The enchantment to remove.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder removeEnchant(@Nonnull Enchantment enchantment) {
        this.meta.removeEnchant(enchantment);
        return this;
    }

    /**
     * Adds item flags to hide certain attributes (e.g., enchantments, unbreakable state).
     *
     * @param flags The ItemFlags to add.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder addItemFlags(ItemFlag... flags) {
        this.meta.addItemFlags(flags);
        return this;
    }

    /**
     * Removes item flags that hide attributes.
     *
     * @param flags The ItemFlags to remove.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder removeItemFlags(ItemFlag... flags) {
        this.meta.removeItemFlags(flags);
        return this;
    }

    /**
     * Sets custom model data for the item, which allows for custom textures or models.
     *
     * @param data The custom model data to set.
     * @return The current ItemBuilder instance for chaining.
     */
    public ItemBuilder setCustomModelData(int data) {
        this.meta.setCustomModelData(data);
        return this;
    }

    /**
     * Finalizes the item by applying all the properties and returns the ItemStack.
     *
     * @return The constructed ItemStack with all the modifications applied.
     */
    public ItemStack build() {
        this.itemStack.setItemMeta(this.meta);
        return this.itemStack;
    }

}