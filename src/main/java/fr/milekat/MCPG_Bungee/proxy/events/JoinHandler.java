package fr.milekat.MCPG_Bungee.proxy.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.proxy.ConnectionsManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class JoinHandler implements Listener {
    private final ConnectionsManager manager;
    public JoinHandler(ConnectionsManager manager) {
        this.manager = manager;
    }

    /**
     *      Quand quelqu'un essaie de rejoindre le proxy
     *      - Vérif que la maintenance n'est pas activée
     *      - Vérif qu'il ne soit pas Ban / TempBan
     *      - Vérif qu'il soit inscrit
     *      - Init de startLogging pour le message de join / leave
     * @param event LoginEvent
     */
    @EventHandler
    public void onTryJoin(LoginEvent event) {
        try {
            Profile profile = manager.getProfile(event.getConnection().getUniqueId().toString());
            if (profile==null || profile.getTeam()==0) throw new SQLException();
            if (MainBungee.DATE_OPEN!=null && MainBungee.DATE_OPEN.getTime() > new Date().getTime()) {
                if (profile.notMaintenance()) {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.building")
                            .replaceAll("@time", DateMilekat.reamingToString(MainBungee.DATE_OPEN))));
                    event.setCancelled(true);
                    return;
                }
            }
            if (MainBungee.DATE_MAINTENANCE!=null && MainBungee.DATE_MAINTENANCE.getTime() > new Date().getTime()) {
                if (profile.notMaintenance()) {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.maintenance")
                            .replaceAll("@time", DateMilekat.reamingToString(MainBungee.DATE_MAINTENANCE))));
                    event.setCancelled(true);
                    return;
                }
            }
            if (manager.getNonStaff().size()>=150) {
                event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.full")));
                event.setCancelled(true);
                return;
            }
            if (profile.isBan()) {
                // Le joueur est ban ou tempban
                if (profile.getBanned().equals(MainBungee.DATE_BAN)) {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.ban")
                            .replaceAll("@reason", profile.getReason())));
                } else {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.tempban")
                            .replaceAll("@time", DateMilekat.reamingToString(profile.getBanned()))
                            .replaceAll("@reason", profile.getReason())));
                }
                event.setCancelled(true);
            }
        } catch (SQLException throwable) {
            event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.register")));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoinUpdateProfile(PostLoginEvent event) {
        try {
            Connection connection = MainBungee.getSql();
            PreparedStatement q = connection.prepareStatement("UPDATE `mcpg_player` SET `username`= ? WHERE `uuid` = ?;");
            q.setString(1, event.getPlayer().getName());
            q.setString(2, event.getPlayer().getUniqueId().toString());
            q.execute();
            q.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    @EventHandler
    public void onJoinEvent(ServerConnectEvent event) {
        if (!event.getTarget().getName().equalsIgnoreCase("event")) return;
        if (event.getPlayer().hasPermission("modo.event.connect.bypass")) return;
        try {
            Connection connection = MainBungee.getSql();
            PreparedStatement q = connection.prepareStatement("SELECT `value` FROM `mcpg_config` WHERE `name` = ?;");
            q.setString(1, "EVENT");
            q.execute();
            q.getResultSet().next();
            if (!q.getResultSet().getBoolean("value")) {
                if (event.getPlayer().getServer()!=null) {
                    event.setCancelled(true);
                } else {
                    event.setTarget(ProxyServer.getInstance().getServerInfo("cite"));
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
