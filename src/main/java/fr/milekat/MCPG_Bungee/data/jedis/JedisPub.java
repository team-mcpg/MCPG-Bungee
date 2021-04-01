package fr.milekat.MCPG_Bungee.data.jedis;

import fr.milekat.MCPG_Bungee.MainBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;
import redis.clients.jedis.Jedis;

import java.util.Objects;

public class JedisPub {
    /**
     *      Send message through Redis MQ
     */
    public static void sendRedisChat(String msg){
        new Thread(() -> {
            Configuration config = MainBungee.getConfig();
            try (Jedis jedis = new Jedis(config.getString("redis.host"),
                    Integer.parseInt(Objects.requireNonNull(config.getString("redis.port"))),0)) {
                jedis.auth(config.getString("redis.auth"));
                jedis.publish(config.getString("redis.out-channels.chat"), msg);
                jedis.quit();
            } catch (Exception throwable) {
                ProxyServer.getInstance().getLogger().warning("Exception : " + throwable.getMessage());
            }
        }).start();
    }

    /**
     *      Log something through Redis MQ
     */
    public static void sendRedisLog(String msg){
        new Thread(() -> {
            Configuration config = MainBungee.getConfig();
            try (Jedis jedis = new Jedis(config.getString("redis.host"),
                    Integer.parseInt(Objects.requireNonNull(config.getString("redis.port"))),0)) {
                jedis.auth(config.getString("redis.auth"));
                jedis.publish(config.getString("redis.out-channels.log"), msg);
                jedis.quit();
            } catch (Exception throwable) {
                ProxyServer.getInstance().getLogger().warning("Exception : " + throwable.getMessage());
            }
        }).start();
    }
}
