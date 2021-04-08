package fr.milekat.MCPG_Bungee.economy;

import fr.milekat.MCPG_Bungee.economy.commands.Top;
import fr.milekat.MCPG_Bungee.economy.engines.Ranking;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class EconomyManager {
    public EconomyManager(Plugin plugin, PluginManager pm) {
        new Ranking();
        pm.registerCommand(plugin, new Top());
    }
}
