package fr.milekat.MCPG_Bungee.proxy.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class EventToggle extends Command implements TabExecutor {
    public EventToggle() {
        super("event", "modo.event.command.toggle");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        try {
            if (args.length == 1 && args[0].equalsIgnoreCase("on")) {
                PreparedStatement q = MainBungee.getSql().prepareStatement("UPDATE `mcpg_config` SET `value` = '1';");
                q.execute();
                q.close();
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Event §2OUVERT!"));
            } else if (args.length == 1 && args[0].equalsIgnoreCase("off")) {
                PreparedStatement q = MainBungee.getSql().prepareStatement("UPDATE `mcpg_config` SET `value` = '0';");
                q.execute();
                q.close();
                for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                    if (player.getServer()!=null && player.getServer().getInfo().getName().equalsIgnoreCase("event")) {
                        if (!player.hasPermission("modo.event.connect.bypass")) {
                            player.connect(ProxyServer.getInstance().getServerInfo("cite"));
                        }
                    }
                }
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Event §cFERMÉ!"));
            } else sendHelp(sender);
        } catch (SQLException throwable) {
            sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cData error"));
            throwable.printStackTrace();
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/event <on/off>:§r Active/Désactive l'accès au serveur event pour les joueurs."));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            return McTools.getTabArgs(args[0], new ArrayList<>(Arrays.asList("on", "off")));
        }
        return new ArrayList<>();
    }
}
