package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.sPlayer;
import com.flashbackmc.statistics.Statistics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import static com.flashbackmc.statistics.Statistics.playerMap;

public class PlayerJoinQuitEtc implements Listener {

    private final Statistics plugin;
    private final Logger logger;

    public PlayerJoinQuitEtc(Logger logger, Statistics plugin) {
        this.logger = logger;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) throws IOException {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!playerMap.containsKey(uuid)) {
            File datafile = new File(plugin.getDataFolder().toString() + "/userdata/" + uuid.toString() + ".yml");
            if (datafile.exists()) {
                InputStream inputStream = Files.newInputStream(Paths.get(datafile.getPath()));
                Yaml yaml = new Yaml();
                Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);
                sPlayer sPlayer = new sPlayer(uuid, data, plugin);
                playerMap.put(uuid, sPlayer);
            } else {
                sPlayer sPlayer = new sPlayer(uuid, event.getPlayer().getName(), plugin);
                playerMap.put(uuid, sPlayer);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) throws IOException {
        UUID uuid = event.getPlayer().getUniqueId();
        sPlayer sPlayer = playerMap.get(uuid);
        sPlayer.updatePlaytime();
        sPlayer.updateRank();

        sPlayer.save(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerKick(final PlayerKickEvent event) throws IOException {
        UUID uuid = event.getPlayer().getUniqueId();
        sPlayer sPlayer = playerMap.get(uuid);
        sPlayer.updatePlaytime();
        sPlayer.updateRank();

        sPlayer.save(plugin);
    }
}
