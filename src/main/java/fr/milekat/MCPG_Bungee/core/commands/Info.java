package fr.milekat.MCPG_Bungee.core.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.core.obj.Team;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.util.ArrayList;

public class Info extends Command implements TabExecutor {
    public Info() {
        super("info");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 1) {
            try {
                Team team = CoreUtils.getPlayerTeam(args[0]);
                StringBuilder builder = new StringBuilder();
                builder.append(MainBungee.PREFIX)
                        .append("§6Équipe §b")
                        .append(team.getName())
                        .append(" §6Classement ");
                if (team.getMembers().size()==1) {
                    builder.append("§dSolo");
                } else if (team.getMembers().size() >= 2 && team.getMembers().size() <= 3) {
                    builder.append("§dDuo / Trio");
                } else if (team.getMembers().size() >= 4 && team.getMembers().size() <= 6) {
                    builder.append("§dTeam");
                } else {
                    builder.append("§dSpécial");
                }
                builder.append(" (").append(team.getMembers().size()).append(")");
                for (Profile profile : team.getMembers()) {
                    builder.append(System.lineSeparator())
                            .append("§7 - §b")
                            .append(profile.getName());
                }
                sender.sendMessage(new TextComponent(builder.toString()));
            } catch (SQLException throwable) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cJoueur non trouvé."));
            }
        } else sendHelp(sender);
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/info <player>:§r Récupère le nom de l'équipe et les membres du joueur."));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            ArrayList<String> names = new ArrayList<>();
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) names.add(player.getDisplayName());
            return McTools.getTabArgs(args[0], names);
        }
        return new ArrayList<>();
    }
}
