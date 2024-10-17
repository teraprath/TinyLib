package com.github.teraprath.tinylib.game.timer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public abstract class GameTimer {

    private final JavaPlugin plugin;
    private final long initialDurationInMillis;
    private long remainingTime;
    private BukkitRunnable timerTask;
    private boolean isPaused = false;
    private boolean isRunning = false;

    public GameTimer(@Nonnull JavaPlugin plugin, @Nonnegative long durationInSeconds) {
        this.plugin = plugin;
        this.initialDurationInMillis = durationInSeconds * 1000;
        this.remainingTime = initialDurationInMillis;
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            runTimerTask();
        }
    }

    private void runTimerTask() {
        timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (remainingTime > 0 && !isPaused) {
                    onTick();
                    remainingTime -= 1000;
                } else if (remainingTime <= 0) {
                    onComplete();
                    stop();
                }
            }
        };
        timerTask.runTaskTimer(plugin, 0, 20);
    }


    public void pause() {
        if (!isPaused && isRunning) {
            isPaused = true;
            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    public void resume() {
        if (isPaused && isRunning) {
            isPaused = false;
            runTimerTask();
        }
    }

    public void cancel() {
        if (timerTask != null && !timerTask.isCancelled()) {
            timerTask.cancel();
            isRunning = false;
            isPaused = false;
            remainingTime = initialDurationInMillis;
        }
    }

    public void stop() {
        if (isRunning) {
            remainingTime = 0;
            isRunning = false;
            isPaused = false;
            if (timerTask != null) {
                timerTask.cancel();
            }
        }
    }

    public void reset() {
        if (isRunning) {
            cancel();
            runTimerTask();
        }
    }

    public boolean isRunning() {
        return isRunning && !isPaused;
    }

    public int getRemainingTimeInSeconds() {
        return (int) (remainingTime / 1000);
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public String getFormattedRemainingTime() {
        long totalSeconds = getRemainingTimeInSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds / 60) % 60;
        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    protected abstract void onTick();
    protected abstract void onComplete();

    public void setDuration(long durationInSeconds) {
        this.remainingTime = durationInSeconds * 1000;

        if (isRunning && !isPaused) {
            if (timerTask != null) {
                timerTask.cancel();
            }
            runTimerTask();
        }
    }

}