package me.keynadi.PlayerMention;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class PlayerMentionMain extends JavaPlugin {

    private File config;
    private File players;

    private static PlayerMentionMain instance;

    private ArrayList<Object> list = new ArrayList<Object>();

    @Override
    public void onEnable() {

        config = new File(getDataFolder() + File.separator + "config.yml");
        players = new File(getDataFolder() + File.separator + "players");

        if (!players.exists()) {
            players.mkdir();
        }

        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            this.saveDefaultConfig();
        }

        getLogger().info("Plugin PlayerMention by Keynadi turned on");
        getServer().getPluginManager().registerEvents(new Listener(this), this);
        getCommand("playermention").setExecutor(new Commands(this));
        getCommand("playermention").setTabCompleter(new TabCompleter());
        instance = this;

    }

    public void onDisable() {
        getLogger().info("Plugin PlayerMention by Keynadi turned off");
    }

    public ArrayList<Object> getlist() {
        return list;
    }


    public static PlayerMentionMain getInstance() {
        return instance;
    }
}
