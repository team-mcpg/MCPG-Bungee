package fr.milekat.MCPG_Bungee.chat;

import fr.milekat.MCPG_Bungee.chat.events.Chat;
import fr.milekat.MCPG_Bungee.chat.events.JoinLeaveMessages;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ChatManager {
    public static HashMap<UUID, String> MSG_LAST = new HashMap<>();
    public static HashMap<UUID, Integer> MSG_RECENT = new HashMap<>();
    public static ArrayList<UUID> CHAT_TEAM = new ArrayList<>();

    public ChatManager(Plugin plugin, PluginManager pm) {
        pm.registerListener(plugin, new JoinLeaveMessages());
        pm.registerListener(plugin, new Chat());
    }
}
