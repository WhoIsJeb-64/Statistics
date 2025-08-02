package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.Statistics;
import com.flashbackmc.statistics.sPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.UUID;
import java.util.logging.Logger;
import static com.flashbackmc.statistics.Statistics.playerMap;

public class ExpChange implements Listener {

    private final Statistics plugin;
    private final Logger logger;

    public ExpChange(Logger logger, Statistics plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerExpChange(final PlayerExpChangeEvent event) {
        if (event.getAmount() > 0) {
            UUID uuid = event.getPlayer().getUniqueId();
            sPlayer sPlayer = playerMap.get(uuid);
            sPlayer.increaseXpGained(event.getAmount());
        }
    }
}
