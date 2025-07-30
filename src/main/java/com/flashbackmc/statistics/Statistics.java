package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.StatsCommand;
import com.flashbackmc.statistics.listeners.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
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
        rankLadder.put("Topaz", 54000000L);
        rankLadder.put("Sapphire", 144000000L);
        rankLadder.put("Amethyst", 360000000L);
        rankLadder.put("Emerald", 1080000000L);
        rankLadder.put("Diamond", 2160000000L);
    }
}
