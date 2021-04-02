package fr.milekat.MCPG_Bungee.moderation;

import fr.milekat.MCPG_Bungee.moderation.commands.*;
import fr.milekat.MCPG_Bungee.moderation.engine.Bans;
import fr.milekat.MCPG_Bungee.moderation.engine.Mutes;
import fr.milekat.MCPG_Bungee.moderation.events.BanJedis;
import fr.milekat.MCPG_Bungee.moderation.events.MuteJedis;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ModerationManager {
    public ModerationManager(Plugin plugin, PluginManager pm) {
        //  Mute
        new Mutes();
        pm.registerListener(plugin, new MuteJedis());
        pm.registerCommand(plugin, new Mute());
        pm.registerCommand(plugin, new UnMute());
        //  Ban
        new Bans();
        pm.registerListener(plugin, new BanJedis());
        pm.registerCommand(plugin, new Ban());
        pm.registerCommand(plugin, new UnBan());
        //  Kick
        pm.registerCommand(plugin, new Kick());
        //  Maintenance
        pm.registerCommand(plugin, new Maintenance());
    }
}
