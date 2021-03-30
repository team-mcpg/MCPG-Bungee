package fr.milekat.MCPG_Bungee.chat.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.data.jedis.JedisManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinLeaveMessages implements Listener {
    @EventHandler
    public void onProxyJoined(PostLoginEvent event){
        String msg = MainBungee.getConfig().getString("connection.join")
                .replaceAll("@player", event.getPlayer().getName());
        //  Redis
        JedisManager.sendRedis(msg);
        //  Chat
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) player.sendMessage(new TextComponent(msg));
    }

    @EventHandler
    public void onProxyLeave(PlayerDisconnectEvent event) {
        String msg = MainBungee.getConfig().getString("connection.join")
                .replaceAll("@player", event.getPlayer().getName());
        //  Redis
        JedisManager.sendRedis(msg);
        //  Chat
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) player.sendMessage(new TextComponent(msg));
    }
}
