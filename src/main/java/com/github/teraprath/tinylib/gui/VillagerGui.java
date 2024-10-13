package com.github.teraprath.tinylib.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class VillagerGui {

    private Component title;
    private List<MerchantRecipe> recipes;

    public VillagerGui(@Nonnull Component title) {
        this.title = title;
        this.recipes = new ArrayList<>();
        create(); // Create recipes for the GUI
    }

    /**
     * Create the trading recipes to be displayed in this GUI.
     * Must be implemented by every subclass.
     */
    protected abstract void create();

    /**
     * Add a custom trade to the GUI.
     * @param result The item the player will receive after the trade.
     * @param maxUses The maximum number of times this trade is available.
     * @param ingredients The ingredients required for this trade.
     */
    protected void addTrade(@Nonnull ItemStack result, @Nonnegative int maxUses, @Nonnull ItemStack... ingredients) {
        MerchantRecipe recipe = new MerchantRecipe(result, maxUses);
        for (ItemStack ingredient : ingredients) {
            recipe.addIngredient(ingredient);
        }
        recipes.add(recipe);
    }

    protected void addTrade(@Nonnull ItemStack result, @Nonnull ItemStack... ingredients) {
        MerchantRecipe recipe = new MerchantRecipe(result, Integer.MAX_VALUE);
        for (ItemStack ingredient : ingredients) {
            recipe.addIngredient(ingredient);
        }
        recipes.add(recipe);
    }

    /**
     * Open the villager GUI for the player.
     * @param player The player for whom the GUI is opened.
     */
    public void open(@Nonnull Player player) {
        Merchant merchant = Bukkit.createMerchant(title);
        merchant.setRecipes(recipes);
        player.openMerchant(merchant, true);
    }
}