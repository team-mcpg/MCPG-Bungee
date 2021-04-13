package fr.milekat.MCPG_Bungee.chat.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class Announce extends Command {
    public Announce() {
        super("broadcast", "modo.chat.announce.new", "bc", "alert", "say");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sendHelp(sender);
        } else {
            ChatUtils.sendAnnounce(CoreUtils.getArgsText(0, args));
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/bc <message>:§r envoyer une annonce dans le chat."));
    }
}
