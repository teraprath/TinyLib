package com.github.teraprath.tinylib.game.api.listener;

import com.github.teraprath.tinylib.game.api.GameAPI;
import com.github.teraprath.tinylib.game.api.event.GameJoinEvent;
import com.github.teraprath.tinylib.game.state.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final GameAPI api;

    public PlayerJoinListener(GameAPI api) {
        this.api = api;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        e.joinMessage(null);
        player.getInventory().clear();
        player.setLevel(0);
        player.setExp(0);
        player.setGameMode(GameMode.ADVENTURE);

        if (api.getGameState().equals(GameState.WAITING)) {

            int online = api.getPlugin().getServer().getOnlinePlayers().size();

            if (online >= api.getSettings().getMinPlayers() && !api.getWaitingTimer().isRunning()) {
                api.getWaitingTimer().start();
            }

        } else {
            api.retire(player);
        }

        api.getPlugin().getServer().getPluginManager().callEvent(new GameJoinEvent(player, api));
    }

}
