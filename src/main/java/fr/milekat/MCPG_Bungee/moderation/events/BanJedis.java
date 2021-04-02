package fr.milekat.MCPG_Bungee.moderation.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.events.CustomJedisSub;
import fr.milekat.MCPG_Bungee.moderation.ModerationUtils;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

public class BanJedis implements Listener {
    @EventHandler
    public void onDiscordBan(CustomJedisSub event) {
        if (event.getChannel().equalsIgnoreCase("log_discord") && event.getCommand().equalsIgnoreCase("ban")) {
            try {
                ModerationUtils.ban(event.getMessage()[0], event.getMessage()[1],
                        Long.getLong(event.getMessage()[2]), event.getMessage()[3]);
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible de ban.");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onDiscordUnBan(CustomJedisSub event) {
        if (event.getChannel().equalsIgnoreCase("log_discord") && event.getCommand().equalsIgnoreCase("unban")) {
            try {
                ModerationUtils.unBan(event.getMessage()[0], event.getMessage()[1], event.getMessage()[2]);
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible d'unban.");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }
    }
}
