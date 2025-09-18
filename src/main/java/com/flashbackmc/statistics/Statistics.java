package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.*;
import com.flashbackmc.statistics.data.*;
import com.flashbackmc.statistics.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.*;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    public static HashMap<UUID, sPlayer> playerMap;
    private LinkedHashMap<String, Group> groups;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        setupConfig();

        playerMap = new HashMap<>();
        groups = new LinkedHashMap<>();
        loadRanks();

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

    private void loadRanks() {
        for (String groupName : getConfig().getConfigurationSection("groupLadders.").getKeys(false)) {
            int requiredHours = getConfig().getInt("groups." + groupName + ".requiredHours");
            int requiredXp = getConfig().getInt("groups." + groupName + ".requiredXp");
            Group group = new Group(groupName, requiredHours, requiredXp);
            groups.put(groupName, group);
        }
    }

    public ArrayList<Group> getGroups() {
        return new ArrayList<>((Collection<Group>) groups);
    }

    public ArrayList<String> getRanks() {
        return new ArrayList<>(getConfig().getConfigurationSection("groups.").getKeys(false));
    }
}
