package com.flashbackmc.statistics;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    public static HashMap<UUID, Player> playerMap;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        playerMap = new HashMap<>();

        registerListeners();

        log.info("Statistics has loaded!");
    }

    @Override
    public void onDisable() {
        log.info("Statistics has unloaded!");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitEtc(getLogger(), this), this);
    }
}
