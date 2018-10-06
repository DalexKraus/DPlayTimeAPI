package at.dalex.playtime;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

/*
 * Copyright 2018 David Kraus. All rights reserved.
 */
public class Main extends JavaPlugin implements PluginMessageListener, IPlayTimes {

    public static String prefix = "§8[§bPlayTime§8]§7: ";

    public final String PLAYTIME_REQUEST_CHANNEL   = "pt_request_channel";
    public final String PLAYTIME_RESPONSE_CHANNEL  = "pt_response_channel";
    public final String PLAYTIME_GET_TOTAL_TIME    = "get_total_playtime";
    public final String PLAYTIME_GET_SESSION_TIME  = "get_session_playtime";

    @Override
    public void onEnable() {
        //Register Channels
        getServer().getMessenger().registerOutgoingPluginChannel(this, PLAYTIME_REQUEST_CHANNEL);
        getServer().getMessenger().registerIncomingPluginChannel(this, PLAYTIME_RESPONSE_CHANNEL, this);
        new PlayTimeAPI(this); //Initialize API

        getServer().getConsoleSender().sendMessage(prefix + "§aAPI geladen");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(prefix + "§4API deaktiviert.");
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            String channelName = inputStream.readUTF();
            String message = inputStream.readUTF();

            //Split message and parse player's UUID
            String[] splitContent = message.split(";");
            UUID playerId = UUID.fromString(splitContent[0]);

            if (channelName.equals(PLAYTIME_GET_TOTAL_TIME)) {
                //Store player's played time in interface's HashMap
                playerPlayTimes.put(playerId, Integer.parseInt(splitContent[1]));
            }
            else {
                //Store player's session time in interface's HashMap
                playerSessionTimes.put(playerId, Integer.valueOf(splitContent[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
