package fr.milekat.MCPG_Bungee.chat.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.events.CustomJedisSub;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;

public class MuteEvent implements Listener {
    @EventHandler
    public void onDiscordMute(CustomJedisSub event) {
        if (event.getChannel().equalsIgnoreCase("log_discord") && event.getCommand().equalsIgnoreCase("mute")) {
            try {
                ChatUtils.mute(event.getMessage()[0], event.getMessage()[1],
                        Long.getLong(event.getMessage()[2]), event.getMessage()[3]);
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible d'unmute.");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onDiscordUnMute(CustomJedisSub event) {
        if (event.getChannel().equalsIgnoreCase("log_discord") && event.getCommand().equalsIgnoreCase("unmute")) {
            try {
                ChatUtils.unMute(event.getMessage()[0], event.getMessage()[1], event.getMessage()[2]);
            } catch (SQLException throwable) {
                MainBungee.warning("Impossible d'unmute.");
                if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
            }
        }
    }
}
