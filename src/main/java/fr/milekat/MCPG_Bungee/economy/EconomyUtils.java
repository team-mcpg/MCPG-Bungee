package fr.milekat.MCPG_Bungee.economy;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import net.md_5.bungee.api.ProxyServer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class EconomyUtils {
    public static void sendAllRankings() {
        ProxyServer.getInstance().getScheduler().runAsync(MainBungee.getInstance(), () -> {
            try {
                ChatUtils.sendAnnounce("Classement des SOLO\n" + EconomyUtils.getBalTopSolo(0, 5));
                TimeUnit.SECONDS.sleep(5);
                ChatUtils.sendAnnounce("Classement des DUO\n" + EconomyUtils.getBalTopDuo(0, 5));
                TimeUnit.SECONDS.sleep(5);
                ChatUtils.sendAnnounce("Classement des TEAM\n" + EconomyUtils.getBalTopTeam(0, 5));
            } catch (SQLException | InterruptedException throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public static String getBalTop(int sizeMin, int sizeMax, int min, int max) throws SQLException {
        PreparedStatement q = MainBungee.getSql().prepareStatement(
                "SELECT *, ROW_NUMBER() OVER(ORDER BY sub.`money` DESC) as rank FROM " +
                        "(SELECT * FROM (SELECT t.`name`, t.`money`, COUNT(*) as size FROM `mcpg_team` as t " +
                        "NATURAL JOIN `mcpg_player` as p GROUP BY t.`team_id`) as query WHERE query.`size` >= " + sizeMin +
                        " AND query.`size` <= " + sizeMax + " ORDER BY query.`money` DESC) as sub LIMIT ?, ?;");
        q.setInt(1, min);
        q.setInt(2, max - min);
        q.execute();
        String top = getTopFormatted(q);
        q.close();
        return top;
    }

    public static String getBalTopTeam(int min, int max) throws SQLException {
        return getBalTop(4, 6, min, max);
    }

    public static String getBalTopDuo(int min, int max) throws SQLException {
        return getBalTop(2, 3, min, max);
    }

    public static String getBalTopSolo(int min, int max) throws SQLException {
        return getBalTop(1, 1, min, max);
    }

    private static String getTopFormatted(PreparedStatement q) throws SQLException {
        StringBuilder builder = new StringBuilder();
        while (q.getResultSet().next()) {
            builder.append("§6Top §b")
                    .append(q.getResultSet().getInt("rank"))
                    .append(" §c")
                    .append(q.getResultSet().getString("name"))
                    .append("§6 avec §2")
                    .append(q.getResultSet().getString("money"))
                    .append("E§r");
            if (!q.getResultSet().isLast()) {
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
