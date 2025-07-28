package com.flashbackmc.statistics.commands;

import com.flashbackmc.statistics.Statistics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;
import static com.flashbackmc.statistics.Statistics.playerMap;

public class StatsCommand implements CommandExecutor {

    public StatsCommand(Statistics statistics) {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Player user = commandSender.getServer().getPlayer(commandSender.getName());
        UUID uuid = user.getUniqueId();
        com.flashbackmc.statistics.Player player = playerMap.get(uuid);
        player.updatePlaytime();

        commandSender.sendMessage("§4Playtime:§c " + player.getPlaytime());
        commandSender.sendMessage("§6Blocks broken/placed:§e " + player.getBlocksBroken() + "§6/§e" + player.getBlocksPlaced());
        return true;
    }
}
