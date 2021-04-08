package fr.milekat.MCPG_Bungee.chat.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;

public class JoinLeaveMessages implements Listener {
    private final HashMap<UUID, String> MSG_LAST;
    private final HashMap<UUID, Integer> MSG_RECENT;

    public JoinLeaveMessages(HashMap<UUID, String> msg_last, HashMap<UUID, Integer> msg_recent) {
        this.MSG_LAST = msg_last;
        this.MSG_RECENT = msg_recent;
    }

    @EventHandler
    public void onProxyJoined(PostLoginEvent event){
        MSG_LAST.remove(event.getPlayer().getUniqueId());
        MSG_RECENT.remove(event.getPlayer().getUniqueId());
        send(MainBungee.getConfig().getString("connection.join")
                .replaceAll("@player", event.getPlayer().getName()));
    }

    @EventHandler
    public void onProxyLeave(PlayerDisconnectEvent event) {
        send(MainBungee.getConfig().getString("connection.leave")
                .replaceAll("@player", event.getPlayer().getName()));

    }

    private void send(String msg) {
        //  Redis
        JedisPub.sendRedisChat(ChatColor.stripColor(msg));
        //  Chat
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            player.sendMessage(new TextComponent(MainBungee.PREFIX + msg));
        }
    }
}
