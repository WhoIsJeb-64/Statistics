package com.flashbackmc.statistics;

import com.flashbackmc.statistics.commands.StatsCommand;
import com.flashbackmc.statistics.listeners.*;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class Statistics extends JavaPlugin {
    private JavaPlugin plugin;
    private Logger log;
    public static HashMap<UUID, sPlayer> playerMap;
    public static ArrayList<Long> rankTimes;
    public static ArrayList<String> rankNames;

    @Override
    public void onEnable() {
        plugin = this;
        log = this.getServer().getLogger();

        playerMap = new HashMap<>();
        rankTimes = new ArrayList<>();
        rankNames = new ArrayList<>();

        initializeRanks();

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
        getServer().getPluginManager().registerEvents(new ExpChange(getLogger(), this), this);
    }

    private void initializeRanks() {
        //TODO: Make this information not hardcoded
        rankTimes.add(54000000L);   //Topaz
        rankTimes.add(144000000L);  //Sapphire
        rankTimes.add(360000000L);  //Amethyst
        rankTimes.add(1080000000L); //Emerald
        rankTimes.add(2160000000L); //Diamond

        rankNames.add("Silver");
        rankNames.add("Topaz");
        rankNames.add("Sapphire");
        rankNames.add("Amethyst");
        rankNames.add("Emerald");
        rankNames.add("Diamond");
    }

    public boolean canLoadPlayer(UUID uuid, Statistics plugin) throws IOException {
        if (!playerMap.containsKey(uuid)) {
            File datafile = new File(plugin.getDataFolder().toString() + "/userdata/" + uuid.toString() + ".yml");
            if (datafile.exists()) {
                InputStream inputStream = Files.newInputStream(Paths.get(datafile.getPath()));
                Yaml yaml = new Yaml();
                Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);
                sPlayer sPlayer = new sPlayer(uuid, data);
                playerMap.put(uuid, sPlayer);
            } else {
                return false;
            }
        }
        return true;
    }
}
