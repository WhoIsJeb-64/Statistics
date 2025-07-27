package com.flashbackmc.statistics;

import java.util.Map;
import java.util.UUID;

public class Player {

    private final UUID uuid;
    private final int blocksBroken;
    private final int blocksPlaced;

    public Player(UUID uuid) {
        this.uuid = uuid;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
    }

    public Player(Map<String, Object> file) {
        this.uuid = (UUID) file.get("uuid");
        this.blocksBroken = (int) file.get("blocksBroken");
        this.blocksPlaced = (int) file.get("blocksPlaced");
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getBlocksBroken() {
        return this.blocksBroken;
    }

    public int getBlocksPlaced() {
        return this.blocksPlaced;
    }
}
