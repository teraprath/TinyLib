package com.github.teraprath.tinylib.game.api.timer;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.RunningCompleteEvent;
import com.github.teraprath.tinylib.game.api.event.RunningTickEvent;
import com.github.teraprath.tinylib.game.state.GameState;
import com.github.teraprath.tinylib.game.timer.GameTimer;

public class RunningTimer extends GameTimer {

    private final GameAPI api;
    private final RunningTickEvent tickEvent;
    private final RunningCompleteEvent completeEvent;

    public RunningTimer(GameAPI api) {
        super(api.getPlugin(), api.getSettings().getRunningDuration());
        this.api = api;
        this.tickEvent = new RunningTickEvent(api);
        this.completeEvent = new RunningCompleteEvent(api);
    }

    @Override
    protected void onTick() {
        api.getPlugin().getServer().getPluginManager().callEvent(tickEvent);
    }

    @Override
    protected void onComplete() {
        api.getPlugin().getServer().getPluginManager().callEvent(completeEvent);
        api.setGameState(GameState.SHUTDOWN);
        api.getShutdownTimer().start();
    }
}
