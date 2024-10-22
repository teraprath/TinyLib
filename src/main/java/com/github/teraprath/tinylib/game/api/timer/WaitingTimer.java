package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.WaitingCompleteEvent;
import com.github.teraprath.tinylib.game.api.event.WaitingTickEvent;
import com.github.teraprath.tinylib.game.state.GameState;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class WaitingTimer extends GameTimer {

    private final GameAPI api;
    private final WaitingTickEvent tickEvent;
    private final WaitingCompleteEvent completeEvent;

    public WaitingTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getWaitingDuration());
        this.api = api;
        this.tickEvent = new WaitingTickEvent(api);
        this.completeEvent = new WaitingCompleteEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(tickEvent);
    }

    @Override
    protected void onComplete() {
        api.getPlugin().getServer().getPluginManager().callEvent(completeEvent);
        api.setGameState(GameState.RUNNING);
        api.getRunningTimer().start();
    }
}
