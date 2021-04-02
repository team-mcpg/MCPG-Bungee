package fr.milekat.MCPG_Bungee.chat.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class PrivateMessage extends Command implements TabExecutor {
    private final HashMap<ProxiedPlayer, ProxiedPlayer> PRIVATE_LAST;
    public PrivateMessage(HashMap<ProxiedPlayer, ProxiedPlayer> private_last) {
        super("m", "", "mp", "dm", "msg", "message", "private", "tell", "w", "whisper");
        this.PRIVATE_LAST = private_last;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            Profile profile = CoreUtils.getProfile(sender.getName());
            if (profile.isMute()) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cTu es mute !"));
                return;
            }
            if (args.length < 2) {
                sendHelp(sender);
                return;
            }
            if (sender.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cTu ne peux t'envoyer de MP !"));
                //return;
            }
            ProxiedPlayer pSender = ProxyServer.getInstance().getPlayer(sender.getName());
            ProxiedPlayer pDest = ProxyServer.getInstance().getPlayer(args[0]);
            if (pDest==null || !pDest.isConnected()) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cLe joueur est introuvable."));
                return;
            }
            ChatUtils.sendPrivate(pSender, pDest, CoreUtils.getArgsText(1, args));
            PRIVATE_LAST.put(pSender, pDest);
            PRIVATE_LAST.put(pDest, pSender);
        } catch (SQLException throwable) {
            sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cErreur, contact le staff !"));
            throwable.printStackTrace();
        }
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/mp <Destinataire> <Message>:§r envoyer message privé au destinataire."));
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
