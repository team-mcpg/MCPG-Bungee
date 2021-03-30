package fr.milekat.MCPG_Bungee.core;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.commands.Debugger;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class CoreManager {
    public CoreManager(Plugin plugin, PluginManager pm) {
        pm.registerCommand(plugin, new Debugger());
    }

    /**
     * Get a player by his discord id
     */
    public static Profile getProfile(String uuid) throws SQLException {
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("SELECT * FROM `mcpg_player` WHERE `uuid` = ?;");
        q.setString(1, uuid);
        q.execute();
        q.getResultSet().next();
        Profile player = getFromSQL(q);
        q.close();
        return player;
    }

    /**
     * Set Profil from SQL ResultSet
     */
    private static Profile getFromSQL(PreparedStatement q) throws SQLException {
        return new Profile(q.getResultSet().getString("username"),
                q.getResultSet().getString("uuid")!=null ?
                        UUID.fromString(q.getResultSet().getString("uuid")) : null,
                q.getResultSet().getInt("team_id"),
                q.getResultSet().getString("muted"),
                q.getResultSet().getString("banned"),
                q.getResultSet().getString("reason"),
                q.getResultSet().getBoolean("maintenance"));
    }
}
