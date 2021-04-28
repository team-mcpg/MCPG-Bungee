package fr.milekat.MCPG_Bungee.chat.commands;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PrefixNode;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class VipColor extends Command implements TabExecutor {
    public VipColor() {
        super("color", "vip.command.rank.color");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) return;
        if (args.length==1) {
            if (new ArrayList<>(Arrays.asList("jaune", "rose", "cyan")).contains(args[0])) {
                LuckPerms luckPerms = LuckPermsProvider.get();
                CompletableFuture<User> userFuture = luckPerms.getUserManager().loadUser(((ProxiedPlayer) sender).getUniqueId());
                userFuture.thenAcceptAsync(user -> {
                    user.getNodes().stream().filter(NodeType.PREFIX::matches).forEach(node -> user.data().remove(node));
                    switch (args[0].toLowerCase(Locale.ROOT)) {
                        case "jaune": {
                            user.data().add(PrefixNode.builder("&e[VIP]", 5).build());
                            break;
                        }
                        case "cyan": {
                            user.data().add(PrefixNode.builder("&3[VIP]", 5).build());
                            break;
                        }
                    }
                    luckPerms.getUserManager().saveUser(user);
                    sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Préfix: " +
                            ChatColor.translateAlternateColorCodes('&', user.getCachedData().getMetaData().getPrefix())));
                });
            } else {
                sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6Couleurs: §eJaune§6, §dRose§6, §3Cyan"));
            }
        } else sendHelp(sender);
    }

    /**
     * Help infos
     */
    private void sendHelp(CommandSender sender){
        sender.sendMessage(new TextComponent(MainBungee.PREFIX + "§6" + getClass().getSimpleName()));
        sender.sendMessage(new TextComponent("§6/color <couleur>:§r Change la couleur de votre grade !."));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            return McTools.getTabArgs(args[0], new ArrayList<>(Arrays.asList("jaune", "rose", "cyan")));
        }
        return new ArrayList<>();
    }
}
