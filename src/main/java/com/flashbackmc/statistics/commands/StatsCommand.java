package com.flashbackmc.statistics.commands;

import com.flashbackmc.statistics.Statistics;
import com.flashbackmc.statistics.sPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
        Player player = commandSender.getServer().getPlayer(commandSender.getName());
        if (!player.hasPermission("statistics.stats")) {
            player.sendMessage(command.getPermissionMessage());
            return true;
        }

        UUID uuid = player.getUniqueId();
        sPlayer sPlayer = playerMap.get(uuid);
        sPlayer.updatePlaytime();
        sPlayer.updateRank();

        commandSender.sendMessage("» §7Playtime:§c " + sPlayer.formattedPlaytime());
        commandSender.sendMessage("» §7Blocks broken/placed:§e " + sPlayer.decFormat(sPlayer.getBlocksBroken()) + " §7/§e " + sPlayer.decFormat(sPlayer.getBlocksPlaced()));
        commandSender.sendMessage("» §7Exp gained:§a " + sPlayer.decFormat(sPlayer.getXpGained()));
        commandSender.sendMessage("» §7Deaths:§3 " + sPlayer.decFormat(sPlayer.getDeaths()));
        return true;
    }
}
