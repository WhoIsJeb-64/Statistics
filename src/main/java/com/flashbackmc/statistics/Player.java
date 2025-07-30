package com.flashbackmc.statistics;

import org.anjocaido.groupmanager.data.Group;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.WorldDataHolder;
import org.bukkit.Bukkit;
import java.text.DecimalFormat;
import java.util.*;

import static com.flashbackmc.statistics.Statistics.rankLadder;

public class Player {

    private final UUID uuid;
    private final String name;

    private long playtime;
    private int blocksBroken;
    private int blocksPlaced;
    private int deaths;

    private long sessionStart = 0;
    private long sessionLength;

    public Player(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;

        this.playtime = 0;
        this.blocksBroken = 0;
        this.blocksPlaced = 0;
        this.deaths = 0;

        this.sessionStart = System.currentTimeMillis();
        this.sessionLength = 0;
    }

    public Player(UUID uuid, Map<String, Object> datafile) {
        this.uuid = uuid;
        this.name = datafile.get("name").toString();

        this.playtime = (Long) datafile.get("playtime");
        this.blocksBroken = (int) datafile.get("blocksBroken");
        this.blocksPlaced = (int) datafile.get("blocksPlaced");
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

    public String formatNumber(int num) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(num);
    }

    public void increaseBlocksPlaced() {
        this.blocksPlaced = this.blocksPlaced + 1;
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

        ArrayList<String> rankList = new ArrayList<>(rankLadder.keySet());
        int rankNum = 0;
        for (Long value : rankLadder.values()) {
            if (this.playtime >= value) {
                rankNum++;
            }
        }
        org.bukkit.entity.Player player = Bukkit.getPlayer(this.name);
        WorldDataHolder wdh = new WorldDataHolder("world");
        Group defaultGroup = new Group("Default");
        wdh.setDefaultGroup(defaultGroup);
        User user = new User(wdh, this.name);
        Group newGroup = new Group(rankList.get(rankNum));
        user.setGroup(newGroup);
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

    public void resetSessionStart() {
        this.sessionStart = System.currentTimeMillis();
    }
}
