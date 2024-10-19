package com.github.teraprath.tinylib.game.team;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTeamManager {

    private final Map<String, GameTeam> teams;

    public GameTeamManager() {
        this.teams = new HashMap<>();
    }

    public void register(@Nonnull GameTeam team) {
        this.teams.put(team.getName(), team);
    }

    public GameTeam getTeam(@Nonnull String name) {
        return this.teams.get(name);
    }

    public GameTeam getTeam(@Nonnull Player player) {
        for (GameTeam team : teams.values()) {
            if (team.getMembers().contains(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public List<GameTeam> getTeams() {
        return new ArrayList<>(teams.values());
    }


}
