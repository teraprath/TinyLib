package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.WaitingTickEvent;
import com.github.teraprath.tinylib.game.state.GameState;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class WaitingTimer extends GameTimer {

    private final GameAPI api;
    private final WaitingTickEvent event;

    public WaitingTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getWaitingDuration());
        this.api = api;
        this.event = new WaitingTickEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(event);
    }

    @Override
    protected void onComplete() {
        api.setGameState(GameState.RUNNING);
        api.getRunningTimer().start();
    }
}
