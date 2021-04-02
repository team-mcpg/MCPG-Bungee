package fr.milekat.MCPG_Bungee.moderation.engine;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.moderation.ModerationUtils;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Bans {
    public Bans() {
        ProxyServer.getInstance().getScheduler().schedule(MainBungee.getInstance(), () -> {
            Connection connection = MainBungee.getSql();
            try {
                PreparedStatement q = connection.prepareStatement(
                        "SELECT * FROM `mcpg_player` WHERE `banned` IS NOT NULL;");
                q.execute();
                while (q.getResultSet().next()){
                    Profile profile = CoreUtils.getProfileFromSQL(q);
                    if (profile.getBanned().getTime() < new Date().getTime()) {
                        ModerationUtils.unBan(profile.getName(), "CONSOLE", "Fin du délai");
                    }
                }
                q.close();
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible d'effectuer le check des bans !");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }, 1000L, 500L, TimeUnit.MILLISECONDS);
    }
}
