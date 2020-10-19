package me.keynadi.PlayerMention;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class Listener implements org.bukkit.event.Listener {

    private FileConfiguration config;

    private JsonArray ignoreArray;

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncPlayerChatEvent e) {
        Player sender = e.getPlayer();
        String format = e.getFormat().replace(" %2$s", "");
        UUID UUID = sender.getUniqueId();
        String message = e.getMessage();

        ArrayList<Player> mentionedPlayers = new ArrayList<Player>();

        String symbol = plugin.getConfig().getString("symbol");

        for (Player recipient : new HashSet<Player>(e.getRecipients())) {

            if (message.contains(symbol + recipient.getName())) {

                if (!sender.hasPermission("playermention.mention") && plugin.getConfig().getBoolean("needPermission")) {
                    return;
                }

                File ignorefile = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + recipient.getUniqueId() + ".json");

                if (ignorefile.exists()) {
                    try {
                        ignoreArray = ((JsonObject) Saver.load(plugin.getDataFolder() + File.separator + "players" + File.separator + recipient.getUniqueId() + ".json")).getAsJsonArray("Ignore");
                    } catch (Exception excep) {
                        excep.printStackTrace();
                    }
                }
                JsonParser parser = new JsonParser();
                if (!ignorefile.exists() || !ignoreArray.contains(parser.parse(String.valueOf(UUID))) || sender.hasPermission("playermention.admin")) {
                    e.getRecipients().remove(recipient);
                    mentionedPlayers.add(recipient);
                } else {
                    if (!recipient.hasPermission("playermention.silent")) {
                        String to = plugin.getConfig().getString("playerhasturnedoffnotifymessage")
                                .replace("%receiver%", recipient.getDisplayName())
                                .replace("&", "§");
                        sender.sendMessage(to);
                    }
                }
            }
        }

        if (!mentionedPlayers.isEmpty()) {

            for (Player mentionedPlayer : mentionedPlayers) {
                if (plugin.getConfig().getBoolean("enableSound") == true) {
                    Sound sound = Sound.valueOf(plugin.getConfig().getString("sound").replace(".", "_").toUpperCase());

                    mentionedPlayer.playSound(mentionedPlayer.getLocation(), sound, 2F, 2F);
                }

                String mentionedMessage = plugin.getConfig().getString("format")
                        .replace("%format%", format)
                        .replace("%message%", message)
                        .replace("&", "§");
                mentionedPlayer.sendMessage(mentionedMessage);
            }

        }
    }


    private PlayerMentionMain plugin;

    public Listener(PlayerMentionMain plugin) {
        this.plugin = plugin;
        config = this.plugin.getConfig();
    }

}
