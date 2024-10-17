package com.github.teraprath.tinylib.game.api.event;

import com.github.teraprath.tinylib.game.api.GameAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


public class GameQuitEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Player player;
    private final GameAPI api;

    public GameQuitEvent(Player player, final GameAPI api) {
        this.player = player;
        this.api = api;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public Player getPlayer() { return this.player; }

    public GameAPI getGame() { return this.api; }

}