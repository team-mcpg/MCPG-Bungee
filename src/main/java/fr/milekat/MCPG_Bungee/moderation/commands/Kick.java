package fr.milekat.MCPG_Bungee.moderation.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Kick extends Command implements TabExecutor {
    public Kick() {
        super("kick", "modo.command.kick.full", "eject");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
            if (target==null || !target.isConnected()) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cJoueur introuvable."));
                return;
            }
            target.disconnect(new TextComponent(MainBungee.getConfig().getString("connection.kick")
                    .replaceAll("@reason", CoreUtils.getArgsText(1, args))));
            JedisPub.sendRedisLog("kick" + "#:#" +
                    target.getName() + "#:#" +
                    sender.getName() + "#:#" +
                    "null" + "#:#" +
                    CoreUtils.getArgsText(1, args));
        } else {
            sendHelp(sender);
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/kick <player>:§r Kick le joueur."));
        sender.sendMessage(new TextComponent("§6/kick <player> <reason>:§r Kick le joueur avec un motif."));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            return McTools.getTabArgs(args[0], ProxyServer.getInstance().getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }
}
