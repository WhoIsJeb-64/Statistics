package com.flashbackmc.statistics;

import java.util.Map;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private final String name;

    private long playtime;
    private int blocksBroken;
    private int blocksPlaced;

    private long sessionStart;
    private long sessionLength;

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.playtime = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public Player(UUID uuid, Map<String, Object> datafile) {
        this.uuid = uuid;
        this.name = datafile.get("name").toString();

        this.playtime = (long) datafile.get("playtime");
        this.blocksBroken = (int) datafile.get("blocksBroken");
        this.blocksPlaced = (int) datafile.get("blocksPlaced");

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public int getBlocksBroken() {
        return this.blocksBroken;
    }

    public void increaseBlocksBroken() {
        this.blocksBroken = this.blocksBroken + 1;
    }

    public int getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public void increaseBlocksPlaced() {
        this.blocksPlaced = this.blocksPlaced + 1;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void updatePlaytime() {
        this.sessionLength = System.currentTimeMillis() - this.sessionStart;
        this.playtime = this.playtime + this.sessionLength;
        this.sessionLength = 0;
    }

    public void resetSessionStart() {
        this.sessionStart = System.currentTimeMillis();
    }
}
