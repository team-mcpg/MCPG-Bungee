package fr.milekat.MCPG_Bungee.moderation.engine;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.moderation.ModerationUtils;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Mutes {
    public Mutes() {
        ProxyServer.getInstance().getScheduler().schedule(MainBungee.getInstance(), () -> {
            try {
                Connection connection = MainBungee.getSql();
                PreparedStatement q = connection.prepareStatement(
                        "SELECT `username`, `muted` FROM `mcpg_player` WHERE `muted` IS NOT NULL;");
                q.execute();
                while (q.getResultSet().next()){
                    if (q.getResultSet().getTimestamp("muted").getTime() <= new Date().getTime()) {
                        ModerationUtils.unMute(q.getResultSet().getString("username"),
                                "CONSOLE", "Fin du délai");
                    }
                }
                q.close();
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible d'effectuer le check des mutes !");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }, 1000L, 500L, TimeUnit.MILLISECONDS);
    }
}
