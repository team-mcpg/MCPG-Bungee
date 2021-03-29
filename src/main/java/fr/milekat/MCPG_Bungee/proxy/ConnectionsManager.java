package fr.milekat.MCPG_Bungee.proxy;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.obj.Profil;
import fr.milekat.MCPG_Bungee.proxy.events.JoinLeaveEvent;
import fr.milekat.MCPG_Bungee.proxy.events.PlayerPing;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectionsManager {
    public ConnectionsManager(Plugin plugin, PluginManager pm) {
        pm.registerListener(plugin, new JoinLeaveEvent(this));
        pm.registerListener(plugin, new PlayerPing());
    }

    /**
     * Get a Player Profil
     */
    public Profil getProfil(String uuid) throws SQLException {
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("SELECT * FROM `mcpg_player` WHERE `uuid` = ?;");
        q.setString(1, uuid);
        q.execute();
        q.getResultSet().next();
        Profil profil = new Profil(UUID.fromString(q.getResultSet().getString("uuid")),
                q.getResultSet().getString("username"),
                q.getResultSet().getInt("team_id"),
                q.getResultSet().getString("muted"),
                q.getResultSet().getString("banned"),
                q.getResultSet().getString("reason"),
                q.getResultSet().getBoolean("maintenance"));
        q.close();
        return profil;
    }

    public ArrayList<ProxiedPlayer> getNonStaff() {
        ArrayList<ProxiedPlayer> onlines = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (!player.hasPermission("modo")) onlines.add(player);
        }
        return onlines;
    }
}
