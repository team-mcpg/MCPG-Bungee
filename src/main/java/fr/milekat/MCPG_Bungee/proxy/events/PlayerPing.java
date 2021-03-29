package fr.milekat.MCPG_Bungee.proxy.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.Date;

public class PlayerPing implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void proxyPing(ProxyPingEvent event) {
        final ServerPing ping = event.getResponse();
        assert MainBungee.DATE_OPEN != null;
        assert MainBungee.DATE_MAINTENANCE != null;
        if (MainBungee.DATE_OPEN.getTime() > new Date().getTime()) {
            ping.setDescriptionComponent(new TextComponent(
                    MainBungee.PREFIX + "§6Serveur en préparation§c.\n" +
                            "§6Retrouvez nous sur§c: §b§nwww.la-cite-givree.fr"));
            ping.getVersion().setProtocol(1);
            ping.getVersion().setName("En préparation");
            ping.getPlayers().setSample(new ServerPing.PlayerInfo[]{
                    new ServerPing.PlayerInfo("§6Ouverture dans§c:", ""),
                    new ServerPing.PlayerInfo("§b" + DateMilekat.reamingToString(MainBungee.DATE_OPEN), "")
            });
        } else if (MainBungee.DATE_MAINTENANCE.getTime() > new Date().getTime()) {
            ping.setDescriptionComponent(new TextComponent(
                    MainBungee.PREFIX + "§6Serveur en maintenance§c.\n" +
                            "§6Retrouvez nous sur§c: §b§nwww.la-cite-givree.fr"));
            ping.getVersion().setProtocol(1);
            ping.getVersion().setName("En maintenance");
            ping.getPlayers().setSample(new ServerPing.PlayerInfo[]{
                    new ServerPing.PlayerInfo("§6Ré-ouverture dans§c:", ""),
                    new ServerPing.PlayerInfo("§b" + DateMilekat.reamingToString(MainBungee.DATE_MAINTENANCE), "")
            });
        } else {
            ping.setDescriptionComponent(new TextComponent(
                    MainBungee.PREFIX + "§6Évent cité au §2émeraudes §b[EN COURS]§c.\n" +
                    "§6Web§c: §b§nwww.la-cite-givree.fr§6 - jusqu'au §c27/12/2020"));
            ArrayList<ServerPing.PlayerInfo> onlines = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                if (!player.hasPermission("modo")) onlines.add(new ServerPing.PlayerInfo(player.getName(), ""));
            }
            ping.setPlayers(new ServerPing.Players(150, onlines.size(), (ServerPing.PlayerInfo[]) onlines.toArray()));
        }
        event.setResponse(ping);
    }
}
