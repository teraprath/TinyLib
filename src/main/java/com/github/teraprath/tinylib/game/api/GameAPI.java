package com.github.teraprath.tinylib.game.api;

import com.github.teraprath.tinylib.game.api.event.GameRetireEvent;
import com.github.teraprath.tinylib.game.api.event.GameStateChangeEvent;
import com.github.teraprath.tinylib.game.api.listener.GamePingListener;
import com.github.teraprath.tinylib.game.api.listener.PlayerJoinListener;
import com.github.teraprath.tinylib.game.api.listener.PlayerQuitListener;
import com.github.teraprath.tinylib.game.api.listener.WaitingListener;
import com.github.teraprath.tinylib.game.team.GameTeamManager;
import com.github.teraprath.tinylib.game.api.timer.RunningTimer;
import com.github.teraprath.tinylib.game.api.timer.ShutdownTimer;
import com.github.teraprath.tinylib.game.api.timer.WaitingTimer;
import com.github.teraprath.tinylib.game.state.GameState;
import com.github.teraprath.tinylib.game.state.GameStateManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class GameAPI extends GameStateManager {

    private final JavaPlugin plugin;
    private final GameSettings settings;
    private final GameTeamManager teamManager;
    private final WaitingTimer waitingTimer;
    private final RunningTimer runningTimer;
    private final ShutdownTimer shutdownTimer;
    private final List<Player> alive;

    public GameAPI(@Nonnull GameSettings settings, @Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.settings = settings;
        this.teamManager = new GameTeamManager();
        this.waitingTimer = new WaitingTimer(this);
        this.runningTimer = new RunningTimer(this);
        this.shutdownTimer = new ShutdownTimer(this);
        this.alive = new ArrayList<>();
    }

    public GameAPI init() {
        registerEvents();
        return this;
    }

    private void registerEvents() {
        final PluginManager pm = plugin.getServer().getPluginManager();
        pm.registerEvents(new GamePingListener(this), this.plugin);
        pm.registerEvents(new PlayerJoinListener(this), this.plugin);
        pm.registerEvents(new PlayerQuitListener(this), this.plugin);
        pm.registerEvents(new WaitingListener(this), this.plugin);
    }

    @Override
    public void onChange(GameState gameState) {
        if (gameState.equals(GameState.RUNNING)) {
            this.alive.addAll(plugin.getServer().getOnlinePlayers());
        }
        plugin.getServer().getPluginManager().callEvent(new GameStateChangeEvent(this));
    }

    public GameSettings getSettings() {
        return settings;
    }

    public GameTeamManager getTeamManager() {
        return teamManager;
    }

    public WaitingTimer getWaitingTimer() {
        return waitingTimer;
    }

    public RunningTimer getRunningTimer() {
        return runningTimer;
    }

    public ShutdownTimer getShutdownTimer() {
        return shutdownTimer;
    }

    public List<Player> getAlive() {
        return this.alive;
    }

    public void retire(@Nonnull Player player) {
        this.alive.remove(player);
        player.setGameMode(GameMode.SPECTATOR);
        plugin.getServer().getPluginManager().callEvent(new GameRetireEvent(player, this));
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }
}
