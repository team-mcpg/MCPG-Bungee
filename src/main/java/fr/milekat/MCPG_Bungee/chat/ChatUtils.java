package fr.milekat.MCPG_Bungee.chat;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChatUtils {
    /**
     * Process an unmute of a player
     */
    public static void mute(String username, String mod, Long time, String reason) throws SQLException {
        Profile profile = CoreUtils.getProfile(username);
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `muted` = ? WHERE `uuid` = ?;");
        q.setTimestamp(1, new Timestamp(time));
        q.setString(2, profile.getUuid().toString());
        q.execute();
        q.close();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getUniqueId().equals(profile.getUuid())) {
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§cVous êtes mute !"
                        + System.lineSeparator() + "§6Raison: " + reason));
            }
        }
        MainBungee.info(profile.getName() + " a été mute par " + mod + " !");
        JedisPub.sendRedisLog("mute#:#" + profile.getUuid() + "#:#" + mod + "#:#" + time + "#:#" + reason);
    }

    /**
     * Process an unmute of a player
     */
    public static void unMute(String username, String mod, String reason) throws SQLException {
        Profile profile = CoreUtils.getProfile(username);
        if (profile.getMuted().getTime() < System.currentTimeMillis()){
            Connection connection = MainBungee.getSql();
            PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `muted` = NULL WHERE `uuid` = ?;");
            q.setString(1, profile.getUuid().toString());
            q.execute();
            q.close();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (player.getUniqueId().equals(profile.getUuid())) {
                    player.sendMessage(new TextComponent(MainBungee.PREFIX + "§2Vous n'êtes plus mute !"
                            + System.lineSeparator() + "§6Soyez plus vigilant à l'avenir !"));
                }
            }
            MainBungee.info(profile.getName() + " n'est plus mute !");
            JedisPub.sendRedisLog("unmute#:#" + profile.getUuid() + "#:#" + mod + "#:#" + reason);
        }
    }
}
