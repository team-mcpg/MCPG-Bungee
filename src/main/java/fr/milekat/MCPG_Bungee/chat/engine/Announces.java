package fr.milekat.MCPG_Bungee.chat.engine;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class Announces {
    /**
     *      Every X minutes send a new announce in chat from SQL announces list (Start after a minimum of 2 minutes)
     */
    public Announces(Long timer) {
        ProxyServer.getInstance().getScheduler().schedule(MainBungee.getInstance(), () -> {
            Connection connection = MainBungee.getSql();
            try {
                PreparedStatement q = connection.prepareStatement("SELECT * FROM `mcpg_announces` " +
                        "WHERE `min` < NOW() AND `max` > NOW() ORDER BY RAND() LIMIT 1;");
                q.execute();
                if (q.getResultSet().last()) {
                    ChatUtils.sendAnnounce(q.getResultSet().getString("message"));
                } else if (MainBungee.DEBUG_ERRORS) MainBungee.warning("Pas de nouvelle annonce.");
                q.close();
            } catch (SQLException throwable) {
                MainBungee.warning("Erreur SQL sur les annonces.");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        },2L, timer, TimeUnit.MINUTES);
    }
}
