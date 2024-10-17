package com.github.teraprath.tinylib.game.api.listener;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.state.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class WaitingListener implements Listener {

    private final GameAPI api;

    public WaitingListener(GameAPI api) {
        this.api = api;
    }

    @EventHandler
    public void onItemSwap(PlayerSwapHandItemsEvent e) {
        protect(e, e.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player player) {
            protect(e, player);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        protect(e, e.getPlayer());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        protect(e, e.getPlayer());
    }


    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player) {
            protect(e, player);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            protect(e, player);
        }
    }

    private void protect(Cancellable cancellable, Player player) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            if (api.getGameState().equals(GameState.WAITING) || api.getGameState().equals(GameState.SHUTDOWN)) {
                cancellable.setCancelled(true);
            }
        }
    }

}
