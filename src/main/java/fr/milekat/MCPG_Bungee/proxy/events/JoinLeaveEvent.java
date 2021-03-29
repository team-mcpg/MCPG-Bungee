package fr.milekat.MCPG_Bungee.proxy.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.obj.Profil;
import fr.milekat.MCPG_Bungee.proxy.ConnectionsManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import fr.milekat.MCPG_Bungee.data.jedis.JedisManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class JoinLeaveEvent implements Listener {
    private final ConnectionsManager manager;
    public JoinLeaveEvent(ConnectionsManager manager) {
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
            Profil profil = manager.getProfil(event.getConnection().getUniqueId().toString());
            if (MainBungee.DATE_OPEN!=null && MainBungee.DATE_OPEN.getTime() > new Date().getTime()) {
                if (profil!=null && profil.notMaintenance()) {
                    event.setCancelReason(new TextComponent(MainBungee.PREFIX
                            + System.lineSeparator() +
                            "§cServeur en préparation !"
                            + System.lineSeparator() +
                            "§6Délais avant l'ouverture§c: §b" + DateMilekat.reamingToString(MainBungee.DATE_OPEN) + "§c."
                            + System.lineSeparator() +
                            "§6En attendant vous pouvez visiter notre site :D"
                            + System.lineSeparator() +
                            "§b§nwww.la-cite-givree.fr"));
                    event.setCancelled(true);
                    return;
                }
            }
            if (MainBungee.DATE_MAINTENANCE!=null && MainBungee.DATE_MAINTENANCE.getTime() > new Date().getTime()) {
                if (profil!=null && profil.notMaintenance()) {
                    event.setCancelReason(new TextComponent(MainBungee.PREFIX
                            + System.lineSeparator() +
                            "§cServeur en maintenance !"
                            + System.lineSeparator() +
                            "§6Délais avant la ré-ouverture§c: §b" + DateMilekat.reamingToString(MainBungee.DATE_MAINTENANCE) + "§c."
                            + System.lineSeparator() +
                            "§6En attendant vous pouvez visiter notre site :D"
                            + System.lineSeparator() +
                            "§b§nwww.la-cite-givree.fr"));
                    event.setCancelled(true);
                    return;
                }
            }
            if (profil==null || profil.getTeam()==0) {
                event.setCancelReason(new TextComponent(MainBungee.PREFIX
                        + System.lineSeparator() +
                        "§cVous n'êtes pas inscrit/n'avez pas d'équipe!"
                        + System.lineSeparator() +
                        "§6Vous devez vous inscrire directement sur le §9discord§c."
                        + System.lineSeparator() +
                        "§6Une fois inscrit, vous devrez intégrer une §béquipe§c."
                        + System.lineSeparator() +
                        "§9§ndiscord.gg/3q2f53E"
                        + System.lineSeparator() +
                        "§b§nwww.la-cite-givree.fr"));
                event.setCancelled(true);
                return;
            }
            if (manager.getNonStaff().size()>=150) {
                event.setCancelReason(new TextComponent(MainBungee.PREFIX
                        + System.lineSeparator() +
                        "§cLe serveur est complet!"
                        + System.lineSeparator() +
                        "§6Vous pouvez patienter sur le Discord§c."
                        + System.lineSeparator() +
                        "§9§ndiscord.gg/3q2f53E"
                        + System.lineSeparator() +
                        "§b§nwww.la-cite-givree.fr"));
                event.setCancelled(true);
                return;
            }
            if (profil.isBan()) {
                // Le joueur est ban ou tempban
                if (profil.getBanned().equals("def")) {
                    event.setCancelReason(new TextComponent(MainBungee.PREFIX
                            + System.lineSeparator() +
                            "§cVous êtes définitivement bannis pour la raison suivante:"
                            + System.lineSeparator() +
                            "§e" + profil.getReason()
                            + System.lineSeparator() +
                            "§6Vous pouvez faire appel de cette décision directement sur §9Discord§c."
                            + System.lineSeparator() +
                            "§b§nwww.la-cite-givree.fr"));
                } else {
                    Date ban = DateMilekat.getDate(profil.getBanned());
                    event.setCancelReason(new TextComponent(MainBungee.PREFIX
                            + System.lineSeparator() +
                            "§cVous êtes actuellement suspendu pour la raison suivante:"
                            + System.lineSeparator() +
                            "§e" + profil.getReason()
                            + System.lineSeparator() +
                            "§6Délais de suspension restant§c: §b" + DateMilekat.reamingToString(ban) + "§c."
                            + System.lineSeparator() +
                            "§6Vous pouvez faire appel de cette décision directement sur §9Discord§c:"
                            + System.lineSeparator() +
                            "§b§nwww.la-cite-givree.fr"));
                }
                event.setCancelled(true);
            }
        } catch (SQLException throwables) {
            event.setCancelReason(new TextComponent(MainBungee.PREFIX
                    + System.lineSeparator() +
                    "§cVous n'êtes pas inscrit/n'avez pas d'équipe!"
                    + System.lineSeparator() +
                    "§6Vous devez vous inscrire directement sur le §9discord§c."
                    + System.lineSeparator() +
                    "§6Une fois inscrit, vous devrez intégrer une §béquipe§c."
                    + System.lineSeparator() +
                    "§9§ndiscord.gg/3q2f53E"
                    + System.lineSeparator() +
                    "§b§nwww.la-cite-givree.fr"));
            event.setCancelled(true);
        }
    }

    /**
     *      Connection autorisée par le proxy, init du joueur
     * @param event PostLoginEvent
     */
    @EventHandler
    public void onProxyJoined(PostLoginEvent event){
        UUID pUuid = event.getPlayer().getUniqueId();
        // Redis
        JedisManager.sendRedis("join_notif#:#" + event.getPlayer().getName());
        // Insert du message de log + update pseudo + send chat
        Connection connection = MainBungee.getSql();
        try {
            PreparedStatement q = connection.prepareStatement(
                    "INSERT INTO `mcpg_chat`(`player_id`, `msg`, `msg_type`, `date_msg`) VALUES (" +
                        "(SELECT `player_id` FROM `mcpg_player` WHERE `uuid` = '" + pUuid + "'),'join',5,?)" +
                        " RETURNING `msg_id`;" +
                        "UPDATE `mcpg_player` SET `online` = '1', `name` = ? WHERE `uuid` = ?;");
            q.setString(1, DateMilekat.setDateNow());
            q.setString(2, event.getPlayer().getName());
            q.setString(3, pUuid.toString());
            q.execute();
            q.getResultSet().last();
            //new ChatSend().sendChatFor("all",q.getResultSet().getInt("msg_id"),true);
            q.close();
        } catch (SQLException throwables) {
            MainBungee.warning("Erreur SQL onProxyJoined.");
            if (MainBungee.DEBUG_ERRORS) throwables.printStackTrace();
        }
        /*ProxyServer.getInstance().getScheduler().runAsync(MainBungee.getInstance(),()->
                new ChatSend().RlChatFor(event.getPlayer().getName(),20));*/
    }

    /**
     *      Désactive 'online', et insert le message de déconnection, & reset des vars
     * @param event PlayerDisconnectEvent
     */
    @EventHandler
    public void onProxyLeave(PlayerDisconnectEvent event) {
        // Redis
        JedisManager.sendRedis("quit_notif#:#" + event.getPlayer().getName());
        Connection connection = MainBungee.getSql();
        try {
            PreparedStatement q = connection.prepareStatement(
                    "INSERT INTO `mcpg_chat`(`player_id`, `msg`, `msg_type`, `date_msg`) " +
                    "VALUES ((SELECT `player_id` FROM `mcpg_player` WHERE `uuid` = '" +
                    event.getPlayer().getUniqueId() + "'),'quit',5,?) RETURNING `msg_id`;" +
                    "UPDATE `mcpg_player` SET `online` = '0' WHERE `uuid` = ?;");
            q.setString(1, DateMilekat.setDateNow());
            q.setString(2, event.getPlayer().getUniqueId().toString());
            q.execute();
            q.getResultSet().last();
            //new ChatSend().sendChatFor("all",q.getResultSet().getInt("msg_id"),false);
            q.close();
        } catch (SQLException throwables) {
            MainBungee.warning("Erreur SQL onProxyLeave :");
            if (MainBungee.DEBUG_ERRORS) throwables.printStackTrace();
        }
    }

    /**
     *      Enregistrement d'un joueur, ou update du name si l'uuid a changé !
     * @param p jouer à reg
     */
    private void setPlayerInfo(ProxiedPlayer p) {
        try {
            Connection connection = MainBungee.getSql();
            PreparedStatement q = connection.prepareStatement(
                    "INSERT INTO `mcpg_player`(`uuid`, `name`, `date_join`) VALUES (?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE `name` = ?;");
            q.setString(1,p.getUniqueId().toString());
            q.setString(2,p.getName());
            q.setString(3,DateMilekat.setDateNow());
            q.setString(4,p.getName());
            q.execute();
            q.close();
        } catch (SQLException throwables) {
            MainBungee.warning("Erreur lors de l'enregistrement de : " + p.getName() + ".");
            if (MainBungee.DEBUG_ERRORS) throwables.printStackTrace();
        }
    }
}
