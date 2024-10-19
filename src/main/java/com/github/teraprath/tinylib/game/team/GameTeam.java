package com.github.teraprath.tinylib.game.team;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameTeam {

    private final String name;
    private final List<UUID> members;

    public GameTeam(@Nonnull String name, @Nonnegative int maxMembers) {
        this.name = name;
        this.members = new ArrayList<>(maxMembers);
    }

    public String getName() {
        return name;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public int getMaxMembers() {
        return this.members.size();
    }
}
