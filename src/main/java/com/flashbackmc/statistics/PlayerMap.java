package com.flashbackmc.statistics;

import java.util.HashMap;
import java.util.UUID;

public class PlayerMap {

    private HashMap<UUID, Player> playerMap;

    public HashMap<UUID, Player> getPlayerMap() {
        return playerMap;
    }

    public Player getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }
}
