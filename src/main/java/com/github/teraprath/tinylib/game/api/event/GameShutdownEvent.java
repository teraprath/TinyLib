package com.github.teraprath.tinylib.game.api.event;

import com.github.teraprath.tinylib.game.api.GameAPI;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameShutdownEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final GameAPI api;

    public GameShutdownEvent(final GameAPI api) {
        this.api = api;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() { return HANDLER_LIST; }

    public GameAPI getGame() { return this.api; }

}
