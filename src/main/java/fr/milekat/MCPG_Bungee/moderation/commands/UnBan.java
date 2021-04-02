package fr.milekat.MCPG_Bungee.moderation.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.moderation.ModerationUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;

public class UnBan extends Command {
    public UnBan() {
        super("unban", "modo.command.unban.full", "pardon");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            try {
                Profile profile = CoreUtils.getProfile(args[0]);
                if (!profile.isBan()) {
                    sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cJoueur pas ban."));
                    return;
                }
                ModerationUtils.unBan(profile.getName(), sender.getName(), CoreUtils.getArgsText(1, args));
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
        sender.sendMessage(new TextComponent("§6/unban <player> <reason>:§r unban le joueur."));
    }
}
