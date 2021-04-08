package fr.milekat.MCPG_Bungee.moderation.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.moderation.ModerationUtils;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.Date;

public class Ban extends Command {
    public Ban() {
        super("ban", "modo.command.ban.full", "tempban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            try {
                Profile profile = CoreUtils.getProfile(args[0]);
                long time;
                if (args[1].equalsIgnoreCase("def") && MainBungee.DATE_BAN!=null) {
                    time = MainBungee.DATE_BAN.getTime();
                } else {
                    time = DateMilekat.stringToPeriod(args[1]) + new Date().getTime();
                    // Check si la maintenance est plus petite que 10s (10000ms)
                    if (time < (new Date().getTime() + 10000)) {
                        sender.sendMessage(new TextComponent(MainBungee.PREFIX +
                                "§cMerci d'indiquer un délais suppérieur à 10s."));
                        return;
                    }
                }
                ModerationUtils.ban(profile.getName(), sender.getName(), time, CoreUtils.getArgsText(2, args));
            } catch (SQLException throwable) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cJoueur introuvable."));
            }
        } else {
            sendHelp(sender);
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/ban <player> def <reason>:§r Ban le joueur définitivement."));
        sender.sendMessage(new TextComponent("§6/ban <player> <time> <reason>:§r Ban le joueur."));
    }
}
