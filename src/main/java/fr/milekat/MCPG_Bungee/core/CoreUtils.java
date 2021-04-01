package fr.milekat.MCPG_Bungee.core;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.utils.Tools;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class CoreUtils {
    public static String getPrefix(UUID uuid) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        CompletableFuture<User> userLoadTask = luckPerms.getUserManager().loadUser(uuid);
        return userLoadTask.join().getCachedData().getMetaData().getPrefix();
    }

    /**
     * Get a player by his discord id
     */
    public static Profile getProfile(UUID uuid) throws SQLException {
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("SELECT * FROM `mcpg_player` WHERE `uuid` = ?;");
        q.setString(1, uuid.toString());
        q.execute();
        q.getResultSet().next();
        Profile profile = getFromSQL(q);
        q.close();
        return profile;
    }

    /**
     * Get a player by his discord id
     */
    public static Profile getProfile(String player) throws SQLException {
        Connection connection = MainBungee.getSql();
        PreparedStatement q = connection.prepareStatement("SELECT * FROM `mcpg_player` WHERE `username` = ?;");
        q.setString(1, player);
        q.execute();
        q.getResultSet().next();
        Profile profile = getFromSQL(q);
        q.close();
        return profile;
    }

    /**
     * Set Profile from SQL ResultSet
     */
    public static Profile getFromSQL(PreparedStatement q) throws SQLException {
        return new Profile(q.getResultSet().getString("username"),
                q.getResultSet().getString("uuid")!=null ?
                        UUID.fromString(q.getResultSet().getString("uuid")) : null,
                q.getResultSet().getInt("team_id"),
                q.getResultSet().getTimestamp("muted")==null ? null :
                        new Date(q.getResultSet().getTimestamp("muted").getTime()),
                q.getResultSet().getTimestamp("banned")==null ? null :
                        new Date(q.getResultSet().getTimestamp("banned").getTime()),
                q.getResultSet().getString("reason"),
                q.getResultSet().getBoolean("maintenance"));
    }

    /**+
     * Concatenates args from minecraft command
     */
    public static String getReason(int skip_args, String... args) {
        StringBuilder sb = new StringBuilder();
        for (int loop=0; loop < args.length; loop++) {
            if (loop < skip_args) continue;
            sb.append(args[loop]).append(" ");
        }
        return Tools.remLastChar(sb.toString());
    }
}
