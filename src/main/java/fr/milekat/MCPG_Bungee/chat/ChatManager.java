package fr.milekat.MCPG_Bungee.chat;

import fr.milekat.MCPG_Bungee.chat.commands.*;
import fr.milekat.MCPG_Bungee.chat.engine.Announces;
import fr.milekat.MCPG_Bungee.chat.engine.Spam;
import fr.milekat.MCPG_Bungee.chat.events.Chat;
import fr.milekat.MCPG_Bungee.chat.events.JoinLeaveMessages;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager {
    public ChatManager(Plugin plugin, PluginManager pm) {
        HashMap<UUID, String> msg_last = new HashMap<>();
        HashMap<UUID, Integer> msg_recent = new HashMap<>();
        HashMap<ProxiedPlayer, ProxiedPlayer> private_last = new HashMap<>();
        ArrayList<UUID> chat_team = new ArrayList<>();
        pm.registerListener(plugin, new JoinLeaveMessages(msg_last, msg_recent));
        pm.registerListener(plugin, new Chat(msg_last, msg_recent, chat_team));
        pm.registerCommand(plugin, new ChatMode(chat_team));
        new Announces(10L);
        new Spam(msg_recent, 500L);
        pm.registerCommand(plugin, new PrivateMessage(private_last));
        pm.registerCommand(plugin, new Reply(private_last));
        pm.registerCommand(plugin, new Announce());
        pm.registerCommand(plugin, new ChatTeam());
    }
}
