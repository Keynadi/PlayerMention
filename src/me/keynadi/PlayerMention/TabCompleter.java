package me.keynadi.PlayerMention;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length == 1) {
            List<String> commands = new ArrayList<>();

            if (commandSender.hasPermission("playermention.reload")) {
                commands.add("reload");
            }

            commands.add("ignore");

            return commands;
        }
        return null;
    }
}
