package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.GameShutdownEvent;
import com.github.teraprath.tinylib.game.api.event.ShutdownTickEvent;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class ShutdownTimer extends GameTimer {

    private final GameAPI api;
    private final ShutdownTickEvent event;

    public ShutdownTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getShutdownDuration());
        this.api = api;
        this.event = new ShutdownTickEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(event);
    }

    @Override
    protected void onComplete() {
        api.getPlugin().getServer().getPluginManager().callEvent(new GameShutdownEvent(this.api));
        api.getPlugin().getServer().shutdown();
    }
}
