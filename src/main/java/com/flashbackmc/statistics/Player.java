package com.flashbackmc.statistics;

import java.util.Map;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private final String name;
    private final int blocksBroken;
    private final int blocksPlaced;

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
    }

    public Player(UUID uuid, Map<String, Object> file) {
        this.uuid = uuid;
        this.name = file.get("name").toString();
        this.blocksBroken = (int) file.get("blocksBroken");
        this.blocksPlaced = (int) file.get("blocksPlaced");
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

    public int getBlocksPlaced() {
        return this.blocksPlaced;
    }
}
