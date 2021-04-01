package fr.milekat.MCPG_Bungee.chat.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.Date;

public class MuteCmd extends Command {
    public MuteCmd() {
        super("mute", "modo.chat.mute.mute");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 3) {
            try {
                Profile profile = CoreUtils.getProfile(args[0]);
                Long time = DateMilekat.stringToPeriod(args[1]) + new Date().getTime();
                ChatUtils.mute(profile.getName(), sender.getName(), time, CoreUtils.getReason(2, args));
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
        sender.sendMessage(new TextComponent("§6/mute <player> <time> <reason>:§r Mute le joueur."));
    }
}
