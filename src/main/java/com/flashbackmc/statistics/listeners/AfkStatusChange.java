package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.Statistics;
import com.flashbackmc.statistics.sPlayer;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.logging.Logger;

import static com.flashbackmc.statistics.Statistics.playerMap;

public class AfkStatusChange implements Listener {
    private final Statistics plugin;
    private final Logger logger;

    public AfkStatusChange(Logger logger, Statistics plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onAfkStatusChange(final AfkStatusChangeEvent event) {
        sPlayer sp = playerMap.get(event.getAffected().getUUID());
        if (!event.getValue()) {
            sp.startSession(); //This makes it so time spent afk is not counted.
        }
    }
}
