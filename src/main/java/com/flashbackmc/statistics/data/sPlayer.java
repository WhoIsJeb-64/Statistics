package com.flashbackmc.statistics.data;

import com.flashbackmc.statistics.Statistics;
import org.bukkit.Bukkit;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class sPlayer {
    private Statistics plugin;

    private final UUID uuid;
    private final String name;
    private String group;

    private long playtime;
    private int blocksBroken;
    private int blocksPlaced;
    private int xpGained;
    private int deaths;

    private long sessionStart;
    private long sessionLength;

    public sPlayer(UUID uuid, String name, Statistics plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = name;
        this.group = plugin.getConfig().getString("settings.defaultGroup");

        this.playtime = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
        this.xpGained = 0;
        this.deaths = 0;

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public sPlayer(UUID uuid, Map<String, Object> datafile, Statistics plugin) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.name = datafile.get("name").toString();
        this.group = datafile.get("group").toString();

        try {
            this.playtime = (int) datafile.get("playtime");
        } catch(Exception e) { //Java is weird
            this.playtime = (long) datafile.get("playtime");
        }

        this.blocksBroken = (int) datafile.get("blocksBroken");
        this.blocksPlaced = (int) datafile.get("blocksPlaced");
        this.xpGained = (int) datafile.get("xpGained");
        this.deaths = (int) datafile.get("deaths");

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public int getBlocksBroken() {
        return this.blocksBroken;
    }

    public void increaseBlocksBroken() {
        this.blocksBroken = this.blocksBroken + 1;
    }

    public int getBlocksPlaced() {
        return this.blocksPlaced;
    }

    public String decFormat(int num) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(num);
    }

    public void increaseBlocksPlaced() {
        this.blocksPlaced = this.blocksPlaced + 1;
    }

    public int getXpGained() {
        return this.xpGained;
    }

    public void increaseXpGained(int difference) {
        this.xpGained = this.xpGained + difference;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void increaseDeaths() {
        this.deaths = this.deaths + 1;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void updatePlaytime() {
        this.sessionLength = System.currentTimeMillis() - this.sessionStart;
        this.playtime = this.playtime + this.sessionLength;
        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public void startSession() {
        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public void updateRank() {
        int rankNum = 0;

        for (Group group : plugin.getGroups()) {
            int requiredHours = group.getRequiredHours();
            int requiredXp = group.getRequiredXp();
            if (this.playtime / 3600000 >= requiredHours) {
                //
            } else {
                break;
            }
            if (this.xpGained >= requiredXp) {
                rankNum++;
            }
        }
        if (!plugin.getGroupNames().contains(this.group)) {
            return;
        }
        if (!this.group.equals(plugin.getGroupNames().get(rankNum - 1))) {
            this.group = plugin.getGroupNames().get(rankNum - 1);
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "pex user " + this.name + " group set " + this.group);
            String promotionBroadcast = plugin.getConfig().getString("groups." + this.group + ".promotionBroadcast");
            promotionBroadcast = promotionBroadcast.replaceAll("&", "§");
            Bukkit.getServer().broadcastMessage(promotionBroadcast.replace("%player%", this.name));
        }
    }

    public String formattedPlaytime() {
        //Each of the next 3 lines determines how many of the unit remain after as many of the next are taken out.
        long seconds = (this.playtime / 1000) % 60;
        long minutes = (this.playtime / (1000 * 60)) % 60;
        long hours = (this.playtime / (1000 * 60 * 60));

        if (this.playtime >= 86400000) {
            return String.format("%02dh %02dm", hours, minutes);
        }
        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
    }

    public void save(Statistics plugin) throws FileNotFoundException {
        Map<String, Object> data = new LinkedHashMap<>();

        data.put("uuid", this.getUuid().toString());
        data.put("name", this.getName());
        data.put("group", this.group);
        data.put("playtime", this.getPlaytime());
        data.put("blocksBroken", this.getBlocksBroken());
        data.put("blocksPlaced", this.getBlocksPlaced());
        data.put("xpGained", this.getXpGained());
        data.put("deaths", this.getDeaths());

        File datafile = new File(plugin.getDataFolder().toString() + "/userdata/" + this.getUuid().toString() + ".yml");
        datafile.getParentFile().mkdirs();
        PrintWriter writer = new PrintWriter(datafile);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);

        Yaml yaml = new Yaml(options);
        yaml.dump(data, writer);
        writer.close();
    }
}
