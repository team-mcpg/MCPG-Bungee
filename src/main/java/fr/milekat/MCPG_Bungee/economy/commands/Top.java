package fr.milekat.MCPG_Bungee.economy.commands;

import fr.milekat.MCPG_Bungee.economy.EconomyUtils;
import fr.milekat.MCPG_Bungee.utils.McTools;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Top extends Command implements TabExecutor {
    public Top() {
        super("top", "modo.command.baltop.send", "baltop", "moneytop");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            if (args.length == 1 && args[0].equalsIgnoreCase("broadcast")) {
                EconomyUtils.sendAllRankings();
            } else if (args.length >= 1) {
                top(sender, args[0], args.length >= 2 ? Integer.parseInt(args[1]) : 0);
            }
        } catch (SQLException throwable) {
            sender.sendMessage(new TextComponent("§cData error"));
            throwable.printStackTrace();
        }
    }

    private void top(CommandSender sender, String type, int min) throws SQLException {
        sender.sendMessage(new TextComponent("§6Classements des " + type + " [§b" + (min + 1) + " §6- §b" + (min + 8) + "§6]"));
        if (type.equalsIgnoreCase("solo")) {
            sender.sendMessage(new TextComponent(EconomyUtils.getBalTopSolo(min, min + 8)));
        } else if (type.equalsIgnoreCase("duo")) {
            sender.sendMessage(new TextComponent(EconomyUtils.getBalTopDuo(min, min + 8)));
        } else if (type.equalsIgnoreCase("team")) {
            sender.sendMessage(new TextComponent(EconomyUtils.getBalTopTeam(min, min + 8)));
        }
        TextComponent component = new TextComponent();
        if (min > 0) {
            TextComponent left = new TextComponent("<<< ");
            left.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/top " + type + " " + ((min > 8) ? min - 8 : 0)));
            component.addExtra(left);
        } else {
            component.addExtra(new TextComponent("XXX "));
        }
        component.addExtra(new TextComponent(" -------------------------------- "));
        TextComponent left = new TextComponent(" >>>");
        left.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/top " + type + " " + (min + 8)));
        component.addExtra(left);
        sender.sendMessage(component);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return McTools.getTabArgs(args[0], new ArrayList<>(Arrays.asList("broadcast", "solo", "duo", "team")));
        }
        return new ArrayList<>();
    }
}
