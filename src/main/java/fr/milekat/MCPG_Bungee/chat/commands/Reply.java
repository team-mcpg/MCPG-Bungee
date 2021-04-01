package fr.milekat.MCPG_Bungee.chat.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.HashMap;

public class Reply extends Command {
    private final HashMap<ProxiedPlayer, ProxiedPlayer> PRIVATE_LAST;
    public Reply(HashMap<ProxiedPlayer, ProxiedPlayer> private_last) {
        super("r", "", "reply", "reponse");
        this.PRIVATE_LAST = private_last;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            ProxiedPlayer pSender = ProxyServer.getInstance().getPlayer(sender.getName());
            if (!PRIVATE_LAST.containsKey(pSender)) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cAucun correspondant récent trouvé."));
                return;
            }
            ProxiedPlayer pDest = PRIVATE_LAST.get(pSender);
            Profile profile = CoreUtils.getProfile(sender.getName());
            if (profile.isMute()) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cTu es mute !"));
                return;
            }
            if (args.length < 1) {
                sendHelp(sender);
                return;
            }
            if (pDest==null || !pDest.isConnected()) {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§cLe joueur n'est connecté."));
                return;
            }
            ChatUtils.sendPrivate(pSender, pDest, CoreUtils.getArgsText(0, args));
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
        sender.sendMessage(new TextComponent(MainBungee.PREFIX));
        sender.sendMessage(new TextComponent("§6/r <Message>:§r envoyer message privé au dernier destinataire."));
    }
}
