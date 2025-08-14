package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.*;
import com.flashbackmc.statistics.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    public static HashMap<UUID, sPlayer> playerMap;
    private LinkedHashMap<String, Long> rankLadder;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        setupConfig();

        playerMap = new HashMap<>();
        rankLadder = new LinkedHashMap<>();
        initRanks();

        registerListeners();
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("statsadmin").setExecutor(new StatsAdminCommand(this));

        log.info("Statistics has loaded!");
        log.info("Ranks: " + getRanks());
    }

    @Override
    public void onDisable() {
        log.info("Statistics has unloaded!");
    }

    private void setupConfig() {
        this.getConfig().options().copyDefaults();
        this.saveDefaultConfig();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEtc(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakPlace(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeath(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new ExpChange(getLogger(), this), this);
        getServer().getPluginManager().registerEvents(new AfkStatusChange(getLogger(), this), this);
    }

    private void initRanks() {
        for (String rank : getConfig().getConfigurationSection("ranks.").getKeys(false)) {
            rankLadder.put(rank, (getConfig().getLong("ranks." + rank) * 3600000));
        }
    }

    public LinkedHashMap<String, Long> getRankLadder() {
        return rankLadder;
    }

    public ArrayList<String> getRanks() {
        return new ArrayList<>(getConfig().getConfigurationSection("ranks.").getKeys(false));
    }
}
