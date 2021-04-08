package fr.milekat.MCPG_Bungee.moderation;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class ModerationUtils {
    /**
     * Process an mute of a player
     */
    public static void mute(String username, String mod, Long time, String reason) throws SQLException {
        Profile pTarget = CoreUtils.getProfile(username);
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `muted` = ? WHERE `uuid` = ?;");
        q.setTimestamp(1, new Timestamp(time));
        q.setString(2, pTarget.getUuid().toString());
        q.execute();
        q.close();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getUniqueId().equals(pTarget.getUuid())) {
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§cVous êtes mute !"));
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Raison:§r " + reason));
            }
        }
        MainBungee.info(pTarget.getName() + " a été mute par " + mod + " pour " + reason);
        JedisPub.sendRedisLog("mute#:#" + pTarget.getName() + "#:#" + mod + "#:#" + time + "#:#" + reason);
    }

    /**
     * Process an unmute of a player
     */
    public static void unMute(String username, String mod, String reason) throws SQLException {
        Profile pTarget = CoreUtils.getProfile(username);
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `muted` = NULL WHERE `uuid` = ?;");
        q.setString(1, pTarget.getUuid().toString());
        q.execute();
        q.close();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getUniqueId().equals(pTarget.getUuid())) {
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§2Vous n'êtes plus mute !"));
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Soyez plus vigilant à l'avenir !"));
            }
        }
        MainBungee.info(pTarget.getName() + " n'est plus mute !");
        JedisPub.sendRedisLog("unmute#:#" + pTarget.getName() + "#:#" + mod + "#:#" + "null" + "#:#" + reason);
    }

    /**
     * Process an ban of a player
     */
    public static void ban(String username, String mod, Long time, String reason) throws SQLException {
        Profile pTarget = CoreUtils.getProfile(username);
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement(
                "UPDATE `mcpg_player` SET `banned` = ?, `reason` = ? WHERE `uuid` = ?;");
        q.setTimestamp(1, new Timestamp(time));
        q.setString(2, reason);
        q.setString(3, pTarget.getUuid().toString());
        q.execute();
        q.close();
        ProxiedPlayer target = ProxyServer.getInstance().getPlayer(pTarget.getUuid());
        if (target!=null && target.isConnected()) {
            if (MainBungee.DATE_BAN!=null && time == MainBungee.DATE_BAN.getTime()) {
                target.disconnect(new TextComponent(MainBungee.getConfig().getString("connection.ban")));
            } else {
                target.disconnect(new TextComponent(MainBungee.getConfig().getString("connection.tempban")
                        .replaceAll("@time", DateMilekat.reamingToString(new Date(time)))
                        .replaceAll("@reason", reason)));
            }
        }
        MainBungee.info(pTarget.getName() + " a été ban par " + mod + " pour " + reason);
        JedisPub.sendRedisLog("ban#:#" + pTarget.getName() + "#:#" + mod + "#:#" + time + "#:#" + reason);
    }

    /**
     * Process an unban of a player
     */
    public static void unBan(String username, String mod, String reason) throws SQLException {
        Profile pTarget = CoreUtils.getProfile(username);
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `banned` = NULL WHERE `uuid` = ?;");
        q.setString(1, pTarget.getUuid().toString());
        q.execute();
        q.close();
        MainBungee.info(pTarget.getName() + " n'est plus ban !");
        JedisPub.sendRedisLog("unban#:#" + pTarget.getName() + "#:#" + mod + "#:#" + "null" + "#:#" + reason);
    }
}
