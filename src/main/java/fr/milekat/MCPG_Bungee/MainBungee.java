package fr.milekat.MCPG_Bungee;

import fr.milekat.MCPG_Bungee.proxy.ConnectionsManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import fr.milekat.MCPG_Bungee.data.MariaManage;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;

public class MainBungee extends Plugin {
    //  Dates
    public final static Date DATE_MAINTENANCE = DateMilekat.getDate("01/01/2021 00:00:00");
    public final static Date DATE_OPEN = DateMilekat.getDate("16/04/2021 14:00:00");
    //  Jedis
    public final static boolean DEBUG_JEDIS = false;

    //  Core
    public final static boolean DEBUG_ERRORS = false;
    public final static String PREFIX = "§8[§6Cité Givrée§8]§r ";

    //  Main
    private static MainBungee mainBungee;
    private Configuration config;
    private MariaManage sql;

    @Override
    public void onEnable(){
        mainBungee = this;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(),"config.yml"));
        } catch (IOException throwables) {
            MainBungee.warning("Erreur config File: " + throwables);
        }
        /* SQL */
        sql = new MariaManage("jdbc:mysql://",
                config.getString("SQL.host"),
                config.getString("SQL.db-user"),
                config.getString("SQL.user"),
                config.getString("SQL.log"));
        sql.connection();
        new ConnectionsManager(this, ProxyServer.getInstance().getPluginManager());
    }

    @Override
    public void onDisable(){
        sql.disconnect();
    }


    public static MainBungee getInstance(){ return mainBungee; }

    public static void log(String message) { ProxyServer.getInstance().getLogger().info(MainBungee.PREFIX + message); }
    public static void info(String log) { ProxyServer.getInstance().getLogger().info(MainBungee.PREFIX + log); }
    public static void warning(String log) { ProxyServer.getInstance().getLogger().warning(MainBungee.PREFIX + log); }

    public static Configuration getConfig(){ return mainBungee.config; }

    public static Connection getSql(){ return mainBungee.sql.getConnection(); }
}