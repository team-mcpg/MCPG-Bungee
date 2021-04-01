package fr.milekat.MCPG_Bungee.chat;

import fr.milekat.MCPG_Bungee.chat.commands.ChatTeam;
import fr.milekat.MCPG_Bungee.chat.commands.MuteCmd;
import fr.milekat.MCPG_Bungee.chat.commands.UnMuteCmd;
import fr.milekat.MCPG_Bungee.chat.engine.Announces;
import fr.milekat.MCPG_Bungee.chat.engine.Mute;
import fr.milekat.MCPG_Bungee.chat.engine.Spam;
import fr.milekat.MCPG_Bungee.chat.events.Chat;
import fr.milekat.MCPG_Bungee.chat.events.JoinLeaveMessages;
import fr.milekat.MCPG_Bungee.chat.events.MuteEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager {
    public ChatManager(Plugin plugin, PluginManager pm) {
        HashMap<UUID, String> msg_last = new HashMap<>();
        HashMap<UUID, Integer> msg_recent = new HashMap<>();
        ArrayList<UUID> chat_team = new ArrayList<>();
        pm.registerListener(plugin, new JoinLeaveMessages(msg_last, msg_recent));
        pm.registerListener(plugin, new Chat(msg_last, msg_recent, chat_team));
        pm.registerCommand(plugin, new ChatTeam(chat_team));
        new Announces(10L);
        new Spam(msg_recent, 500L);
        new Mute();
        pm.registerListener(plugin, new MuteEvent());
        pm.registerCommand(plugin, new MuteCmd());
        pm.registerCommand(plugin, new UnMuteCmd());
    }
}
