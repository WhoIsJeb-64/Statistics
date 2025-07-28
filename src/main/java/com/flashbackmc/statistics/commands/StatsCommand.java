package com.flashbackmc.statistics.commands;

import com.flashbackmc.statistics.Statistics;
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

        Player user = commandSender.getServer().getPlayer(commandSender.getName());
        UUID uuid = user.getUniqueId();
        com.flashbackmc.statistics.Player player = playerMap.get(uuid);
        player.updatePlaytime();

        commandSender.sendMessage("§7» §4Playtime:§c " + player.formattedPlaytime(player.getPlaytime()));
        commandSender.sendMessage("§7» §6Blocks broken/placed:§e " + player.formatNumber(player.getBlocksBroken()) + " §6/§e " + player.formatNumber(player.getBlocksPlaced()));
        return true;
    }
}
