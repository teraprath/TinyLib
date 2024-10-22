package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.GameShutdownEvent;
import com.github.teraprath.tinylib.game.api.event.ShutdownCompleteEvent;
import com.github.teraprath.tinylib.game.api.event.ShutdownTickEvent;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class ShutdownTimer extends GameTimer {

    private final GameAPI api;
    private final ShutdownTickEvent tickEvent;
    private final ShutdownCompleteEvent completeEvent;

    public ShutdownTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getShutdownDuration());
        this.api = api;
        this.tickEvent = new ShutdownTickEvent(api);
        this.completeEvent = new ShutdownCompleteEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(tickEvent);
    }

    @Override
    protected void onComplete() {
        api.getPlugin().getServer().getPluginManager().callEvent(completeEvent);
        api.getPlugin().getServer().getPluginManager().callEvent(new GameShutdownEvent(this.api));
        api.getPlugin().getServer().shutdown();
    }
}
