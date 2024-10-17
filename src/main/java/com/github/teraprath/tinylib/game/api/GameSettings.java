package com.github.teraprath.tinylib.game.api;

import javax.annotation.Nonnegative;

public class GameSettings {

    private int minPlayers = 2;
    private long waitingDuration = 60;
    private long runningDuration = 600;
    private long shutdownDuration = 20;

    public int getMinPlayers() {
        return minPlayers;
    }

    public GameSettings setMinPlayers(@Nonnegative int amount) {
        this.minPlayers = amount;
        return this;
    }

    public long getWaitingDuration() {
        return waitingDuration;
    }

    public GameSettings setWaitingDuration(@Nonnegative long seconds) {
        this.waitingDuration = seconds;
        return this;
    }

    public long getRunningDuration() {
        return runningDuration;
    }

    public GameSettings setRunningDuration(@Nonnegative long seconds) {
        this.runningDuration = seconds;
        return this;
    }

    public long getShutdownDuration() {
        return shutdownDuration;
    }

    public GameSettings setShutdownDuration(@Nonnegative long seconds) {
        this.shutdownDuration = seconds;
        return this;
    }

}
