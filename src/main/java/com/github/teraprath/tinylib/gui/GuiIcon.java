package com.github.teraprath.tinylib.gui;

import com.github.teraprath.tinylib.item.ItemBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class GuiIcon {

    private final ItemStack itemStack;
    private boolean fixed = true;
    private Consumer<InventoryClickEvent> action;

    /**
     * Creates a GuiIcon from the provided ItemStack.
     *
     * @param itemStack The ItemStack representing the icon.
     */
    public GuiIcon(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Creates a GuiIcon from the provided ItemBuilder.
     *
     * @param itemBuilder The ItemBuilder to construct the ItemStack for the icon.
     */
    public GuiIcon(@Nonnull ItemBuilder itemBuilder) {
        this.itemStack = itemBuilder.build();
    }

    /**
     * Sets whether the icon can be moved when clicked.
     *
     * @param fixed If true, the icon cannot be moved by the player.
     * @return The GuiIcon instance for chaining.
     */
    public GuiIcon setFixed(boolean fixed) {
        this.fixed = fixed;
        return this;
    }

    /**
     * Defines the action to be performed when the icon is clicked.
     *
     * @param onClick A Consumer handling the InventoryClickEvent when the icon is clicked.
     * @return The GuiIcon instance for chaining.
     */
    public GuiIcon onClick(@Nonnull Consumer<InventoryClickEvent> onClick) {
        this.action = onClick;
        return this;
    }

    /**
     * Returns whether the icon is fixed or can be moved by the player.
     *
     * @return True if the icon is fixed and cannot be moved, false otherwise.
     */
    public boolean isFixed() {
        return this.fixed;
    }

    /**
     * Returns the ItemStack associated with this icon.
     *
     * @return The ItemStack that represents the icon.
     */
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Returns the action to be executed when the icon is clicked.
     *
     * @return A Consumer that handles the InventoryClickEvent.
     */
    public Consumer<InventoryClickEvent> getAction() {
        return this.action;
    }
}