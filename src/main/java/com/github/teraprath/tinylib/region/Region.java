package com.github.teraprath.tinylib.region;

import org.bukkit.Location;

import javax.annotation.Nonnull;

public class Region extends RegionCuboid {

    private final String name;
    private String displayName;

    public Region(String name, Location pos1, Location pos2) {
        super(pos1, pos2);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(@Nonnull String string) {
        this.displayName = string;
    }

}