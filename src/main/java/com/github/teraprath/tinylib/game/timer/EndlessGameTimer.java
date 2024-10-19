package com.github.teraprath.tinylib.game.timer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;

public abstract class EndlessGameTimer {

    private final JavaPlugin plugin;
    private long elapsedTime = 0;
    private BukkitRunnable timerTask;
    private boolean isPaused = false;
    private boolean isRunning = false;

    public EndlessGameTimer(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
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
                if (!isPaused) {
                    onTick();
                    elapsedTime += 1000;
                }
            }
        };
        timerTask.runTaskTimer(plugin, 0, 20); // 20 Ticks = 1 Sekunde
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
            elapsedTime = 0;
        }
    }

    public void stop() {
        if (isRunning) {
            isRunning = false;
            isPaused = false;
            if (timerTask != null) {
                timerTask.cancel();
            }
            onStop();
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

    public int getElapsedTimeInSeconds() {
        return (int) (elapsedTime / 1000);
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public String getFormattedElapsedTime() {
        long totalSeconds = getElapsedTimeInSeconds();
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
    protected abstract void onStop();
}
