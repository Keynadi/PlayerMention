package me.keynadi.PlayerMention;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Saver {
    private PlayerMentionMain plugin;

    public Saver(PlayerMentionMain plugin) {
        this.plugin = plugin;
    }

    public static void save(JsonObject obj, String path) throws IOException {
        FileWriter file = new FileWriter(path);
        file.write(obj.toString());
        file.flush();
    }

    public static Object load(String path) throws Exception {
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(new FileReader(path));
        JsonObject jsonObject = (JsonObject) obj;
        return jsonObject;
    }
}