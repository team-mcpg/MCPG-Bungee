package fr.milekat.MCPG_Bungee;

import fr.milekat.MCPG_Bungee.chat.ChatManager;
import fr.milekat.MCPG_Bungee.core.CoreManager;
import fr.milekat.MCPG_Bungee.data.DataManager;
import fr.milekat.MCPG_Bungee.data.MariaDB.MariaManage;
import fr.milekat.MCPG_Bungee.moderation.ModerationManager;
import fr.milekat.MCPG_Bungee.proxy.ConnectionsManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.sql.Connection;
import java.util.Date;

public class MainBungee extends Plugin {
    //  Bungee/configs
    private static MainBungee mainBungee;
    private Configuration config;
    public final static String PREFIX = "§7[§bLa Cité Givrée§7]§r ";
    public static boolean DEBUG_ERRORS = false;

    //  SQL/Jedis
    private MariaManage sql;
    public static boolean DEBUG_JEDIS = false;

    //  Dates
    public static Date DATE_MAINTENANCE = new Date();
    public static Date DATE_MAINTENANCE_OFF = new Date();
    public static Date DATE_OPEN = new Date();
    public static Date DATE_BAN = new Date();

    @Override
    public void onEnable(){
        /* Bungee/configs */
        mainBungee = this;
        DataManager data = new DataManager(this);
        config = data.getConfigurations();
        /* SQL/Jedis */
        sql = data.getSQL();
        data.loadDates();
        data.getJedis();
        /* Classes */
        new CoreManager(this, ProxyServer.getInstance().getPluginManager());
        new ConnectionsManager(this, ProxyServer.getInstance().getPluginManager());
        new ChatManager(this, ProxyServer.getInstance().getPluginManager());
        new ModerationManager(this, ProxyServer.getInstance().getPluginManager());
    }

    @Override
    public void onDisable(){
        sql.disconnect();
    }

    public static MainBungee getInstance(){ return mainBungee; }

    public static void log(String message) { ProxyServer.getInstance().getLogger().info(MainBungee.PREFIX + message); }
    public static void info(String log) { ProxyServer.getInstance().getLogger().info(MainBungee.PREFIX + log); }
    public static void warning(String log) { ProxyServer.getInstance().getLogger().warning(MainBungee.PREFIX + log); }

    public static Configuration getConfig() { return mainBungee.config; }

    public static Connection getSql() { return mainBungee.sql.getConnection(); }
}
