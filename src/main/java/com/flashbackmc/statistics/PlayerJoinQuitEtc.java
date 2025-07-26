package com.flashbackmc.statistics;

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

public class PlayerJoinQuitEtc implements Listener {

    HashMap<UUID, Player> playerMap = new PlayerMap().getPlayerMap();

    public PlayerJoinQuitEtc(Statistics statistics) {
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) throws IOException {
        UUID uuid = event.getPlayer().getUniqueId();

        if (!playerMap.containsKey(uuid)) {

            File datafile = new File("./SAM/plugins/Statistics/userdata/" + uuid.toString() + ".yml");
            if (datafile.exists()) {
                InputStream inputStream = Files.newInputStream(Paths.get(datafile.getPath()));
                Yaml yaml = new Yaml();
                Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);
                Player player = new Player(data);
                playerMap.put(uuid, player);
            }

            Player player = new Player(uuid);
            playerMap.put(uuid, player);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event) throws FileNotFoundException {
        Player player = playerMap.get(event.getPlayer().getUniqueId());

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uuid", player.getUuid());
        data.put("blocksBroken", player.getBlocksBroken());
        data.put("blocksPlaced", player.getBlocksPlaced());

        PrintWriter writer = new PrintWriter("./SAM/plugins/Statistics/userdata/" + player.getUuid().toString() + ".yml");
        Yaml yaml = new Yaml();
        yaml.dump(data, writer);
    }
}
