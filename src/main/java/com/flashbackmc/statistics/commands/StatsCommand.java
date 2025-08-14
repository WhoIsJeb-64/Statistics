package com.flashbackmc.statistics.commands;

import com.flashbackmc.statistics.Statistics;
import com.flashbackmc.statistics.sPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import static com.flashbackmc.statistics.Statistics.playerMap;

public class StatsCommand implements CommandExecutor {

    private final Statistics plugin;
    public StatsCommand(Statistics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            Bukkit.getLogger().info("A player must be specified if command is run from console!");
            return true;
        }
        Player p = commandSender.getServer().getPlayer(commandSender.getName());
        if (!p.hasPermission("statistics.stats")) {
            p.sendMessage(command.getPermissionMessage());
            return true;
        }

        sPlayer sp;
        if (strings[0] != null) {
            try {
                sp = loadPlayer(strings[0]);
            } catch (IOException e) {
                commandSender.sendMessage("§4» §cSpecified player does not exist!");
                throw new RuntimeException(e);
            }
        } else {
            sp = playerMap.get(p.getUniqueId());
        }
        if (sp == null) {
            commandSender.sendMessage("§4sp == null");
            return true;
        }
        sp.updatePlaytime();
        sp.updateRank();

        commandSender.sendMessage("» §7Playtime:§c " + sp.formattedPlaytime());
        commandSender.sendMessage("» §7Blocks broken/placed:§e " + sp.decFormat(sp.getBlocksBroken()) + " §7/§e " + sp.decFormat(sp.getBlocksPlaced()));
        commandSender.sendMessage("» §7Exp gained:§a " + sp.decFormat(sp.getXpGained()));
        commandSender.sendMessage("» §7Deaths:§3 " + sp.decFormat(sp.getDeaths()));
        return true;
    }

    private sPlayer loadPlayer(String name) throws IOException {
        Player p = Bukkit.getPlayer(name);
        if (p == null) {
            return null;
        }
        UUID uuid = p.getUniqueId();
        if (playerMap.containsKey(uuid)) {
            return playerMap.get(uuid);
        }
        File datafile = new File(plugin.getDataFolder().toString() + "/userdata/" + uuid.toString() + ".yml");
        if (datafile.exists()) {
            InputStream inputStream = Files.newInputStream(Paths.get(datafile.getPath()));
            Yaml yaml = new Yaml();
            Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);
            return new sPlayer(uuid, data, plugin);
        }
        return null;
    }
}
