package com.flashbackmc.statistics.commands;

import com.flashbackmc.statistics.Statistics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class StatsAdminCommand implements CommandExecutor {

    private final Statistics plugin;
    public StatsAdminCommand(Statistics plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length < 1) {
            return false;
        }
        String subCommand = strings[0];
        return false;
    }
}
