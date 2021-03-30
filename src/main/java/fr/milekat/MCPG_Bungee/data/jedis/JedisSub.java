package fr.milekat.MCPG_Bungee.data.jedis;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.events.CustomJedisSub;
import net.md_5.bungee.api.ProxyServer;
import redis.clients.jedis.JedisPubSub;

public class JedisSub extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equalsIgnoreCase(MainBungee.getConfig().getString("redis.thischannel"))) {
            if (MainBungee.DEBUG_JEDIS) MainBungee.info("SUB:{" + channel + "}|MSG:{" + message + "}");
            ProxyServer.getInstance().getPluginManager().callEvent(new CustomJedisSub(channel,message));
        } else {
            if (MainBungee.DEBUG_JEDIS) MainBungee.info("PUB:{" + message + "}");
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        if (MainBungee.DEBUG_JEDIS) MainBungee.info("Redis connecté à " + channel);
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        if (MainBungee.DEBUG_JEDIS) MainBungee.warning("Redis déconnecté de " + channel);
    }
}