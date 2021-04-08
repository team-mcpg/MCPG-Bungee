package fr.milekat.MCPG_Bungee.economy.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Team;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class Money extends Command {
    public Money() {
        super("money", "", "bal");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (sender.hasPermission("modo.command.money.other") && args.length == 1) {
                Team team = CoreUtils.getTeam(CoreUtils.getProfile(args[0]).getTeam());
                sender.sendMessage(new TextComponent(MainBungee.PREFIX +
                        "§6L'équipe §b" + team.getName() + " §6a §2" + team.getMoney() + "E§6."));
            } else if (args.length == 0 && sender instanceof ProxiedPlayer) {
                Team team = CoreUtils.getPlayerTeam(((ProxiedPlayer) sender).getUniqueId());
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Ton équipe a §2" + team.getMoney() + "E§6."));
            } else sendHelp(sender);
        } catch (SQLException throwable) {
            if (throwable.getMessage().equalsIgnoreCase("Illegal operation on empty result set.")) {
                sender.sendMessage(new TextComponent("§cÉquipe introuvable"));
            } else {
                sender.sendMessage(new TextComponent("§cData error"));
                throwable.printStackTrace();
            }
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        if (sender instanceof ProxiedPlayer) {
            sender.sendMessage(new TextComponent("§6/money:§r Voir ses émeraudes en banque."));
        }
        if (sender.hasPermission("modo.command.money.other")) {
            sender.sendMessage(new TextComponent("§6/money <player>:§r Voir le nombre d'émeraudes de l'équipe du joueur."));
        }
    }
}
