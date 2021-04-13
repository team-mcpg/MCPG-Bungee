package fr.milekat.MCPG_Bungee.economy.engines;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.economy.EconomyUtils;
import net.md_5.bungee.api.ProxyServer;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Ranking {
    private int LAST_HOUR = 0;
    public Ranking() {
        ProxyServer.getInstance().getScheduler().schedule(MainBungee.getInstance(), () -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (calendar.get(Calendar.HOUR_OF_DAY)!= LAST_HOUR && calendar.get(Calendar.HOUR_OF_DAY)==18 &&
                    calendar.get(Calendar.MINUTE) < 2) {
                EconomyUtils.sendAllRankings();
            }
            LAST_HOUR = calendar.get(Calendar.HOUR_OF_DAY);
        },0L,30L, TimeUnit.SECONDS);
    }
}
