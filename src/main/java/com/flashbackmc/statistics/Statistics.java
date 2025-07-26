package com.flashbackmc.statistics;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        PlayerJoinQuitEtc listener = new PlayerJoinQuitEtc(this);

        log.info("Statistics has loaded!");
    }

    @Override
    public void onDisable() {
        //Save playerdata that has changed and new players created
    }
}
