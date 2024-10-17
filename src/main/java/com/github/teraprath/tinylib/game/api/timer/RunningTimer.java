package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.RunningTickEvent;
import com.github.teraprath.tinylib.game.state.GameState;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class RunningTimer extends GameTimer {

    private final GameAPI api;
    private final RunningTickEvent event;

    public RunningTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getRunningDuration());
        this.api = api;
        this.event = new RunningTickEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(event);
    }

    @Override
    protected void onComplete() {
        api.setGameState(GameState.SHUTDOWN);
        api.getShutdownTimer().start();
    }
}
