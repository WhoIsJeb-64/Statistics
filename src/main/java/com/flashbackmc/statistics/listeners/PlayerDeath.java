package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.Statistics;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;
import java.util.logging.Logger;

import static com.flashbackmc.statistics.Statistics.playerMap;

public class PlayerDeath implements Listener {

    private final Statistics plugin;
    private final Logger logger;

    public PlayerDeath(Logger logger, Statistics plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(final EntityDeathEvent event) {

        if (event.getEntity() instanceof Player) {
            Player user = (Player) event.getEntity();
            UUID uuid = user.getUniqueId();
            com.flashbackmc.statistics.Player player = playerMap.get(uuid);
            player.increaseDeaths();
        }
    }
}
