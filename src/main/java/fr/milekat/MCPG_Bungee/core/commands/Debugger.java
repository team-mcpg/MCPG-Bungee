package fr.milekat.MCPG_Bungee.core.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;

public class Debugger extends Command implements TabExecutor {
    public Debugger() {
        super("debugger", "admin.core.command.debugger");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            sendHelp(sender);
            return;
        }
        switch (args[0].toLowerCase())
        {
            case "exceptions":
            {
                MainBungee.DEBUG_ERRORS = !MainBungee.DEBUG_ERRORS;
                sender.sendMessage(new TextComponent("Java exceptions debug: " + MainBungee.DEBUG_ERRORS));
                break;
            }
            case "jedis":
            {
                MainBungee.DEBUG_JEDIS = !MainBungee.DEBUG_JEDIS;
                sender.sendMessage(new TextComponent("Jedis debug: " + MainBungee.DEBUG_JEDIS));
                break;
            }
            default: sendHelp(sender);
        }
    }

    /**
     *      Envoie la liste d'help de la commande
     * @param sender joueur qui exécute la commande
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6/debugger <debugger>:§r Switch un débugger."));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Arrays.asList("exceptions", "jedis"));
        }
        return new ArrayList<>();
    }
}
