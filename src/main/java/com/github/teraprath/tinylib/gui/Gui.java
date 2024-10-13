package com.github.teraprath.tinylib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public abstract class Gui implements InventoryHolder {

    private Inventory inventory;
    private Component title;
    private final int size;
    private final Map<Integer, GuiIcon> items = new HashMap<>();

    /**
     * Creates a new GUI with the given title and number of rows.
     *
     * @param title The title of the GUI, displayed to the player.
     * @param rows  The number of rows in the GUI (each row contains 9 slots).
     */
    public Gui(@Nonnull Component title, @Nonnegative int rows) {
        this.title = title;
        this.size = rows * 9;
        this.inventory = Bukkit.createInventory(this, this.size, title);
    }

    /**
     * Updates the title of the GUI.
     *
     * @param title The new title to set for the GUI.
     */
    public void setTitle(@Nonnull Component title) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this, this.size, title);
    }

    /**
     * Adds a new item to the GUI in the first available empty slot.
     *
     * @param guiIcon The GuiIcon to be added to the GUI.
     */
    public void addItem(@Nonnull GuiIcon guiIcon) {
        this.items.forEach((slot, item) -> {
            if (item == null) {
                setItem(slot, guiIcon);
            }
        });
    }

    /**
     * Sets a specific item at the given slot in the GUI.
     *
     * @param slot    The slot number where the item should be placed.
     * @param guiIcon The GuiIcon to place in the specified slot.
     */
    public void setItem(@Nonnegative int slot, @Nonnull GuiIcon guiIcon) {
        this.items.put(slot, guiIcon);
    }

    /**
     * Updates the GUI by placing the stored items in their corresponding slots.
     */
    public void update() {

        for (int slot = 0; slot < inventory.getSize(); slot++) {
            if (this.items.get(slot) != null) {
                this.inventory.setItem(slot, this.items.get(slot).getItemStack());
            }
        }
    }

    /**
     * Opens the GUI for the specified player.
     *
     * @param player The player for whom the GUI will be opened.
     */
    public void open(@Nonnull Player player) {
        player.openInventory(this.inventory);
    }

    /**
     * Called when the inventory is opened.
     * Must be implemented by subclasses to define behavior on inventory open.
     *
     * @param event The event triggered when the inventory is opened.
     */
    public abstract void onOpen(InventoryOpenEvent event);

    /**
     * Called when the inventory is closed.
     * Must be implemented by subclasses to define behavior on inventory close.
     *
     * @param event The event triggered when the inventory is closed.
     */
    public abstract void onClose(InventoryCloseEvent event);

    /**
     * Handles click events inside the GUI, managing item interaction and actions.
     *
     * @param event The event triggered when an item is clicked in the inventory.
     */
    public void handleClick(InventoryClickEvent event) {

        int slot = event.getRawSlot();

        // If there's no item in this slot, cancel the event.
        if (this.items.get(slot) == null) {
            event.setCancelled(true);
            return;
        }

        // If the item is marked as fixed, cancel the event (cannot be moved).
        if (this.items.get(slot).isFixed()) {
            event.setCancelled(true);
        }

        // Execute the item's click action if one is defined.
        if (this.items.get(slot).getAction() != null) {
            this.items.get(slot).getAction().accept(event);
        }
    }

    /**
     * Returns the inventory of the GUI.
     * This method is required by the InventoryHolder interface.
     *
     * @return The inventory object associated with the GUI.
     */
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
