package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.Player;
import com.flashbackmc.statistics.Statistics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import java.util.UUID;
import java.util.logging.Logger;
import static com.flashbackmc.statistics.Statistics.playerMap;

public class BlockBreakPlace implements Listener {

    private final Statistics plugin;
    private final Logger logger;

    public BlockBreakPlace(Logger logger, Statistics plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = playerMap.get(uuid);
        player.increaseBlocksBroken();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = playerMap.get(uuid);
        player.increaseBlocksPlaced();
    }
}
