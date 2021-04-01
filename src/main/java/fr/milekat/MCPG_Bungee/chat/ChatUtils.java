package fr.milekat.MCPG_Bungee.chat;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.core.obj.Team;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ChatUtils {
    /**
     * Process an mute of a player
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
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§cVous êtes mute !"));
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Raison:§r " + reason));
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
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `muted` = NULL WHERE `uuid` = ?;");
        q.setString(1, profile.getUuid().toString());
        q.execute();
        q.close();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (player.getUniqueId().equals(profile.getUuid())) {
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§2Vous n'êtes plus mute !"));
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Soyez plus vigilant à l'avenir !"));
            }
        }
        MainBungee.info(profile.getName() + " n'est plus mute !");
        JedisPub.sendRedisLog("unmute#:#" + profile.getUuid() + "#:#" + mod + "#:#" + reason);
    }

    /**
     * Process an announce (Chat + RedisChat)
     */
    public static void sendAnnounce(String announce) {
        JedisPub.sendRedisChat(announce);
        StringBuilder prettyAnnounce = new StringBuilder();
        for (String splitLines : ChatColor.translateAlternateColorCodes('&', announce).split("\n")) {
            for (String splitSize : splitLines.split("(?<=\\G.{37,}\\s)")) {
                if (splitSize.length() > 1) prettyAnnounce.append("   ")
                        .append(ChatColor.translateAlternateColorCodes('&', splitSize))
                        .append(System.lineSeparator());
            }
        }
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent("§r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r §7[§6Annonce Cité§7§7]§r §r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r" +
                    System.lineSeparator() + System.lineSeparator() + prettyAnnounce.toString() + System.lineSeparator() +
                    "§r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r §7[§6Annonce Cité§7§7]§r §r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r"));
        }
    }

    /**
     * Send a private message
     */
    public static void sendPrivate(ProxiedPlayer sender, ProxiedPlayer receiver, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        sender.sendMessage(sender.getUniqueId(), new TextComponent("§6[§cMoi §6> §c" + receiver.getName() + "§6]§r " + message));
        receiver.sendMessage(sender.getUniqueId(), new TextComponent("§6[§c" + sender.getName() + " §6> §cMoi§6]§r " + message));
    }

    /**
     * Send message in chat for team
     */
    public static void sendChatTeam(ProxiedPlayer player, String message) throws SQLException {
        Team team = CoreUtils.getPlayerTeam(player.getUniqueId());
        for (Profile member : team.getMembers()) {
            ProxiedPlayer pMember = ProxyServer.getInstance().getPlayer(member.getUuid());
            if (pMember==null || !pMember.isConnected()) continue;
            pMember.sendMessage(player.getUniqueId(), new TextComponent("[Team] " + player.getName() + " §b»§r " + message));
        }
    }
}
