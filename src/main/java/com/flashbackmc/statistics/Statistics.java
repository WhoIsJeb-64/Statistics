package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.StatsCommand;
import com.flashbackmc.statistics.listeners.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    private File ranksFile = new File(getDataFolder(), "rankLadder.yml");
    private FileConfiguration ranksConfig = YamlConfiguration.loadConfiguration(ranksFile);
    public static HashMap<UUID, Player> playerMap;
    public static HashMap<String, Long> rankLadder;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        playerMap = new HashMap<>();
        rankLadder = new HashMap<>();
        if (!ranksFile.exists()) {
            saveResource("rankLadder.yml", false);
            log.info("rankLadder.yml does not exist, creating new one.");
        }

        initializeRankLadder();
        registerListeners();
        getCommand("stats").setExecutor(new StatsCommand(this));

        log.info("Statistics has loaded!");
    }

    @Override
    public void onDisable() {
        log.info("Statistics has unloaded!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEtc(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakPlace(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(getLogger(), this), this);
    }

    private void initializeRankLadder() {
        Map<String, Object> rankList = new HashMap<>();
        rankList.putAll(ranksConfig.getValues(false));
        for (Map.Entry<String, Object> entry : rankList.entrySet()) {
            rankLadder.put(entry.getKey(), (Long) entry.getValue());
        }
    }
}
