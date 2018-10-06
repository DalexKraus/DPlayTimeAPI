package at.dalex.playtime;

import org.bukkit.Bukkit;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

/*
 * Copyright 2018 David Kraus. All rights reserved.
 */
public class PlayTimeAPI implements IPlayTimes {

    private static Main pluginInstance;

    public PlayTimeAPI(Main plugin) {
        pluginInstance = plugin;
    }

    /**
     * Get the playtime of a specific player in seconds.
     *
     * A session is meant to be the interval between the
     * login an disconnect event of a player.
     *
     * @param playerId The {@link org.bukkit.entity.Player}'s {@link UUID}
     * @return The playtime in seconds
     */
    public static int getSessionPlayTime(UUID playerId) {
        playerSessionTimes.remove(playerId);  //Clean up old entry

        ByteArrayOutputStream outputStream = createByteArrayOutputStream(pluginInstance.PLAYTIME_GET_SESSION_TIME, playerId);
        Bukkit.getServer().sendPluginMessage(pluginInstance, pluginInstance.PLAYTIME_REQUEST_CHANNEL, outputStream.toByteArray());

        Integer sessionTime;
        do {
            sessionTime = playerSessionTimes.get(playerId);
        } while (sessionTime == null);

        return sessionTime;
    }

    /**
     * Returns the total time in seconds a player
     * has played on this server.
     *
     * Returns -1 if the specified player has
     * never played on this server before.
     *
     * @param playerId The {@link org.bukkit.entity.Player}'s {@link UUID}
     */
    public static int getTotalPlayTime(UUID playerId) {
        playerPlayTimes.remove(playerId);  //Clean up old entry

        ByteArrayOutputStream outputStream = createByteArrayOutputStream(pluginInstance.PLAYTIME_GET_TOTAL_TIME, playerId);
        Bukkit.getServer().sendPluginMessage(pluginInstance, pluginInstance.PLAYTIME_REQUEST_CHANNEL, outputStream.toByteArray());

        Integer playTime;
        do {
            playTime = playerPlayTimes.get(playerId);
        } while (playTime == null);

        return playTime;
    }

    /**
     * Creates a new {@link ByteArrayOutputStream} which contains the target channel and playerId.
     * @param targetChannel The target channel
     * @param playerId The {@link org.bukkit.entity.Player}'s {@link UUID}
     * @return The created {@link ByteArrayOutputStream}
     */
    private static ByteArrayOutputStream createByteArrayOutputStream(String targetChannel, UUID playerId) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            dataOutputStream.writeUTF(targetChannel);
            dataOutputStream.writeUTF(playerId.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}
