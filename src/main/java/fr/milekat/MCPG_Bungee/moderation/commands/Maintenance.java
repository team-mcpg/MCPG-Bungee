package fr.milekat.MCPG_Bungee.moderation.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.data.DataManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Date;

public class Maintenance extends Command {
    public Maintenance() {
        super("maintenance", "modo.command.maintenance.toggle");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sendHelp(sender);
            return;
        }
        if (args[0].equalsIgnoreCase("off")) {
            DataManager.setMaintenance(MainBungee.DATE_MAINTENANCE_OFF);
            sender.sendMessage(new TextComponent(MainBungee.PREFIX + "Maintenance désactivée"));
            return;
        } else if (args[0].equalsIgnoreCase("on")) {
            args[0] = "5m";
        }
        long time = DateMilekat.stringToPeriod(args[0]) + new Date().getTime();
        // Check si la maintenance est plus petite que 10s (10000ms)
        if (time < (new Date().getTime() + 10000)) {
            sender.sendMessage(new TextComponent(MainBungee.PREFIX +
                    "§cMerci d'indiquer un délais suppérieur à 10s."));
            return;
        }
        DataManager.setMaintenance(new Date(time));
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (!player.hasPermission("modo.maintenance.bypass")) {
                player.disconnect(new TextComponent(MainBungee.getConfig().getString("connection.maintenance")
                        .replaceAll("@time", DateMilekat.reamingToString(MainBungee.DATE_MAINTENANCE))));
            }
        }
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "Maintenance activée pour: " +
                DateMilekat.reamingToString(MainBungee.DATE_MAINTENANCE)));
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX));
        sender.sendMessage(new TextComponent(
                "§6/maintenance <time>:§r Active la maintenance pour la durée indiquée et kick les joueurs."));
        sender.sendMessage(new TextComponent(
                "§6/maintenance on:§r Active la maintenance pour 5 minutes et kick les joueurs."));
        sender.sendMessage(new TextComponent("§6/maintenance off:§r Désactive la maintenance."));
    }
}
