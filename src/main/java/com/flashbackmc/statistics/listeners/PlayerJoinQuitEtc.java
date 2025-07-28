package com.flashbackmc.statistics.listeners;

import com.flashbackmc.statistics.Player;
import com.flashbackmc.statistics.Statistics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
                Player player = new Player(uuid, data);
                playerMap.put(uuid, player);
            }

            Player player = new Player(uuid, event.getPlayer().getName());
            playerMap.put(uuid, player);
        } else {
            Player player = playerMap.get(uuid);
            player.resetSessionStart();
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) throws IOException {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = playerMap.get(uuid);
        player.updatePlaytime();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uuid", player.getUuid().toString());
        data.put("name", player.getName());

        data.put("playtime", player.getPlaytime());
        data.put("blocksBroken", player.getBlocksBroken());
        data.put("blocksPlaced", player.getBlocksPlaced());

        File datafile = new File(plugin.getDataFolder().toString() + "/userdata/" + player.getUuid().toString() + ".yml");
        datafile.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(datafile);
        Yaml yaml = new Yaml();
        yaml.dump(data, writer);
        writer.close();
    }
}
