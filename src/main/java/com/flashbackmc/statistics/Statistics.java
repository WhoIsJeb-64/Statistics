package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.*;
import com.flashbackmc.statistics.listeners.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    private File configFile;
    private FileConfiguration config;
    public static HashMap<UUID, sPlayer> playerMap;
    private HashMap<String, Long> rankLadder;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();
        configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        if (!configFile.exists()) {
            saveResource("config.yml", false);
            log.info("Created Statistics's config!");
        }

        playerMap = new HashMap<>();
        rankLadder = new HashMap<>();
        initRanks();

        registerListeners();
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("statsadmin").setExecutor(new StatsAdminCommand(this));

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
        getServer().getPluginManager().registerEvents(new ExpChange(getLogger(), this), this);
    }

    private void initRanks() {
        for (String key : config.getConfigurationSection("ranks.").getKeys(false)) {
            rankLadder.put(key, (config.getLong("ranks." + key) / 3600000));
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public HashMap<String, Long> getRankLadder() {
        return rankLadder;
    }

    public ArrayList<String> getRanks() {
        return new ArrayList<>(rankLadder.keySet());
    }
}
