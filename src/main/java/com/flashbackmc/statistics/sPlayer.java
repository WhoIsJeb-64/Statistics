package com.flashbackmc.statistics;

import org.bukkit.Bukkit;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import static com.flashbackmc.statistics.Statistics.*;

public class sPlayer {

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

    public sPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.group = "Silver";

        this.playtime = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
        this.xpGained = 0;
        this.deaths = 0;

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public sPlayer(UUID uuid, Map<String, Object> datafile) {
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

    public String getGroup() {
        return this.group;
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

    public String formatNumber(int num) {
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
    }

    public void updateRank() {
        int rankNum = 0;
        for (Long key : rankTimes) {if (this.playtime >= key) {rankNum++;}}
        if (this.group.equals(rankNames.get(rankNum))) {return;}
        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "manuadd " + this.name + " " + rankNames.get(rankNum));
        this.group = rankNames.get(rankNum);
    }

    public String formattedPlaytime(Long playtime) {
        //Each of the next 4 lines determines how many of the unit remain after as many of the next are taken out.
        long seconds = (playtime / 1000) % 60;
        long minutes = (playtime / (1000 * 60)) % 60;
        long hours = (playtime / (1000 * 60 * 60)) % 24;
        long days = (playtime / (1000 * 60 * 60 * 24));

        //Seconds stop being displayed to players who have over 1 day of playtime
        if (playtime >= 86400000) {
            return String.format("%01dd %02dh %02dm", days, hours, minutes);
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
        Yaml yaml = new Yaml();
        yaml.dump(data, writer);
        writer.close();
    }
}
