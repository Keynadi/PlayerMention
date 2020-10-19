package me.keynadi.PlayerMention;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;


public class Commands implements CommandExecutor {
    private PlayerMentionMain plugin;
    private FileConfiguration config;
    private JsonObject ignore = new JsonObject();
    private JsonArray ignoreArray = new JsonArray();
    private UUID UUID;
    private UUID UUIDIgnored;

    public Commands(PlayerMentionMain plugin) {
        this.plugin = plugin;
        config = this.plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender p, Command command, String s, String[] args) {
        config = plugin.getConfig();

        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            if (p.hasPermission("playermention.reload")) {
                plugin.saveDefaultConfig();
                plugin.reloadConfig();
                p.sendMessage(plugin.getConfig().getString("configreload").replace("&", "§"));
                return true;
            } else {
                p.sendMessage(plugin.getConfig().getString("nopermissions").replace("&", "§"));
                return true;
            }

        }

        if (args[0].equalsIgnoreCase("ignore")) {
            if (args.length > 1 && args[1] != null) {

                if (!playerOnline(args[1])) {
                    p.sendMessage(config.getString("playeroffline").replace("&", "§"));
                    return true;
                }

                UUID = Bukkit.getPlayer(p.getName()).getUniqueId();

                UUIDIgnored = Bukkit.getPlayer(args[1]).getUniqueId();

                if (UUID == UUIDIgnored) {
                    p.sendMessage(config.getString("ignoreyourself").replace("&", "§"));
                    return true;
                }

                File ignorefile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + UUID + ".json");

                if (ignorefile.exists()) {
                    try {
                        ignore = (JsonObject) Saver.load(plugin.getDataFolder() + File.separator + "players" + File.separator + UUID + ".json");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ignoreArray = ignore.getAsJsonArray("Ignore");
                }
                JsonParser parser = new JsonParser();
                if (ignoreArray.contains(parser.parse(String.valueOf(UUIDIgnored)))) {
                    ignoreArray.remove(parser.parse(String.valueOf(UUIDIgnored)));
                    ignore = new JsonObject();
                    ignore.add("Ignore", ignoreArray);
                    try {
                        Saver.save(ignore, plugin.getDataFolder() + File.separator + "players" + File.separator + UUID + ".json");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(config.getString("nolongerignoring").replace("&", "§").replace("%player%", Bukkit.getPlayer(UUIDIgnored).getName()));
                } else {
                    ignoreArray.add(String.valueOf(UUIDIgnored));
                    ignore.add("Ignore", ignoreArray);
                    try {
                        Saver.save(ignore, plugin.getDataFolder() + File.separator + "players" + File.separator + UUID + ".json");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(config.getString("nowignoring").replace("&", "§").replace("%player%", Bukkit.getPlayer(UUIDIgnored).getName()));
                }
            } else {
                p.sendMessage(config.getString("plmignoreusage").replace("&", "§"));
        }
            return true;
        }
        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?") ) {
            p.sendMessage(config.getString("help").replace("&", "§"));
            return true;
        }
        return false;
    }

    public static boolean playerOnline(String name) {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (onlinePlayers.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

}
