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

import java.sql.SQLException;

public class ChatUtils {
    /**
     * Process an announce (Chat + RedisChat)
     */
    public static void sendAnnounce(String announce) {
        announce = ChatColor.translateAlternateColorCodes('&', announce.replace("\\n", System.lineSeparator()));
        JedisPub.sendRedisChat("**Annonce »** " + ChatColor.stripColor(announce));
        MainBungee.log("Annonce » " + ChatColor.stripColor(announce));
        StringBuilder prettyAnnounce = new StringBuilder();
        for (String splitLines : announce.split("\\r?\\n")) {
            for (String splitSize : splitLines.split("(?<=\\G.{37,}\\s)")) {
                if (splitSize.length() > 1) prettyAnnounce.append("   ")
                        .append(ChatColor.translateAlternateColorCodes('&', splitSize))
                        .append(System.lineSeparator());
            }
        }
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent("§r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r §7[§6Annonce Cité§7§7]§r §r§7§m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯§r" +
                    System.lineSeparator() + System.lineSeparator() + prettyAnnounce + System.lineSeparator() +
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
        for (ProxiedPlayer loop : ProxyServer.getInstance().getPlayers()) {
            if (loop.hasPermission("modo.chat.see.private") && !loop.equals(sender) && !loop.equals(receiver)) {
                loop.sendMessage(sender.getUniqueId(),
                        new TextComponent("§6[§c" + sender.getName() + " §6> §c" + receiver.getName() + "§6]§r " + message));
            }
        }
    }

    /**
     * Send message in chat for team
     */
    public static void sendChatTeam(ProxiedPlayer player, String message) throws SQLException {
        Team team = CoreUtils.getPlayerTeam(player.getUniqueId());
        for (Profile member : team.getMembers()) {
            ProxiedPlayer pMember = ProxyServer.getInstance().getPlayer(member.getUuid());
            if (pMember==null || !pMember.isConnected()) continue;
            pMember.sendMessage(player.getUniqueId(), new TextComponent("§a[Team]§r " + player.getName() + " §b»§r " + message));
        }
        if (team.getName().equalsIgnoreCase("staff")) return;
        for (ProxiedPlayer loop : ProxyServer.getInstance().getPlayers()) {
            if (loop.hasPermission("modo.chat.see.team")) loop.sendMessage(player.getUniqueId(),
                    new TextComponent("§a[" + team.getName() + "]§r " + player.getName() + " §b»§r " + message));
        }
    }
}
