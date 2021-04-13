package fr.milekat.MCPG_Bungee.core;

import fr.milekat.MCPG_Bungee.core.commands.Debugger;
import fr.milekat.MCPG_Bungee.core.commands.Info;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class CoreManager {
    public CoreManager(Plugin plugin, PluginManager pm) {
        pm.registerCommand(plugin, new Debugger());
        pm.registerCommand(plugin, new Info());
    }
}
