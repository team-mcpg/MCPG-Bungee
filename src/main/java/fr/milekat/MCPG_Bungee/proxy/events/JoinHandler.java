package fr.milekat.MCPG_Bungee.proxy.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.proxy.ConnectionsManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
            Profile profil = manager.getProfil(event.getConnection().getUniqueId().toString());
            if (profil==null || profil.getTeam()==0) throw new SQLException();
            if (MainBungee.DATE_OPEN!=null && MainBungee.DATE_OPEN.getTime() > new Date().getTime()) {
                if (profil.notMaintenance()) {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.building")
                            .replaceAll("@time", DateMilekat.reamingToString(MainBungee.DATE_OPEN))));
                    event.setCancelled(true);
                    return;
                }
            }
            if (MainBungee.DATE_MAINTENANCE!=null && MainBungee.DATE_MAINTENANCE.getTime() > new Date().getTime()) {
                if (profil.notMaintenance()) {
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
            if (profil.isBan()) {
                // Le joueur est ban ou tempban
                if (profil.getBanned().equals("def")) {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.ban")));
                } else {
                    event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.tempban")
                            .replaceAll("@time", DateMilekat.reamingToString(DateMilekat.getDate(profil.getBanned())))));
                }
                event.setCancelled(true);
            }
        } catch (SQLException throwables) {
            event.setCancelReason(new TextComponent(MainBungee.getConfig().getString("connection.register")));
            event.setCancelled(true);
        }
    }
}