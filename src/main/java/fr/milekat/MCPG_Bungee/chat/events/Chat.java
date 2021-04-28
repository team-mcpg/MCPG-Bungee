package fr.milekat.MCPG_Bungee.chat.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatUtils;
import fr.milekat.MCPG_Bungee.core.CoreUtils;
import fr.milekat.MCPG_Bungee.core.events.CustomJedisSub;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.data.jedis.JedisPub;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Chat implements Listener {
    private final HashMap<UUID, String> MSG_LAST;
    private final HashMap<UUID, Integer> MSG_RECENT;
    private final ArrayList<ProxiedPlayer> CHAT_TEAM;

    public Chat(HashMap<UUID, String> msg_last, HashMap<UUID, Integer> msg_recent, ArrayList<ProxiedPlayer> chat_team) {
        this.MSG_LAST = msg_last;
        this.MSG_RECENT = msg_recent;
        this.CHAT_TEAM = chat_team;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) return;
        event.setCancelled(true);
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if (CHAT_TEAM.contains(player)) {
            try {
                ChatUtils.sendChatTeam(player, event.getMessage());
            } catch (SQLException throwable) {
                player.sendMessage(new TextComponent(MainBungee.PREFIX + "§cData error."));
                throwable.printStackTrace();
            }
        } else {
            String message = cleanMessages(event.getMessage(), player.getUniqueId());
            if (message==null) return;
            sendChat(player.getUniqueId(), message);
        }
    }

    @EventHandler
    public void onDiscordChat(CustomJedisSub event) {
        String[] raw = event.getRedisMsg().split("\\|");
        String message = cleanMessages(event.getRedisMsg().replace(raw[0] + "|", ""),
                UUID.fromString(raw[0]));
        if (message!=null) sendChat(UUID.fromString(raw[0]), message);
    }

    /**
     * Send message in chat
     */
    private void sendChat(UUID uuid, String message) {
        try {
            Profile profile = CoreUtils.getProfile(uuid);
            String prefix = CoreUtils.getPrefix(uuid);
            prefix =  prefix == null ? "" : prefix + " ";
            String msg = ChatColor.translateAlternateColorCodes('&',
                    prefix + profile.getName() + " §b»§r " + message);
            if (profile.isMute()) {
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if (player.isConnected()) {
                    player.sendMessage(new TextComponent("§c§l[MUTE]§r " + msg));
                    warnMute(player, profile);
                }
                for (ProxiedPlayer mod : ProxyServer.getInstance().getPlayers()) {
                    if (!mod.getUniqueId().equals(profile.getUuid()) && mod.hasPermission("modo.mute.other.see")) {
                        mod.sendMessage(new TextComponent("§c§l[MUTE]§r " + msg));
                    }
                }
            } else {
                //  Global chat
                for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
                    players.sendMessage(uuid, new TextComponent(msg));
                }
                //  Redis
                JedisPub.sendRedisChat(ChatColor.stripColor(msg));
            }
            // Log du message dans la console, comme un message normal
            MainBungee.log(ChatColor.stripColor("<" + profile.getName() + "§r> " + message));
        } catch (SQLException throwable) {
            if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
        }
    }

    /**
     * Send time before unMute
     */
    private void warnMute(ProxiedPlayer player, Profile profile) {
        String str = DateMilekat.reamingToString(profile.getMuted());
        TextComponent Mute = new TextComponent(MainBungee.PREFIX + "§6Vous serez unmute dans §b" + str + "§c.");
        Mute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("§cLes modos peuvent" +
                System.lineSeparator() + "encore voir vos" + System.lineSeparator() + "messages")));
        player.sendMessage(Mute);
    }

    /**
     * If safe_chat is enable, filter words, spamming and upercases
     */
    private String cleanMessages(String message, UUID sender){
        // Évite le spam chat
        int msg_recent = MSG_RECENT.getOrDefault(sender,0) + 1;
        MSG_RECENT.put(sender, msg_recent);
        if (msg_recent > MainBungee.getConfig().getInt("chat.spam_limit_kick")) {
            if (isOnline(sender)) {
                ProxyServer.getInstance().getPlayer(sender)
                        .disconnect(new TextComponent(MainBungee.getConfig().getString("chat.kick_message")));
            }
            return null;
        }
        if (msg_recent > MainBungee.getConfig().getInt("chat.spam_limit_warn")) {
            if (isOnline(sender)) {
                ProxyServer.getInstance().getPlayer(sender)
                        .sendMessage(new TextComponent(MainBungee.PREFIX + "§cDoucement avec le chat !"));
            }
            return null;
        }
        if (MSG_LAST.getOrDefault(sender,"").equalsIgnoreCase(message)) {
            if (isOnline(sender)) {
                ProxyServer.getInstance().getPlayer(sender)
                        .sendMessage(new TextComponent(MainBungee.PREFIX + "§cMessage déjà envoyé."));
            }
            return null;
        }
        MSG_LAST.put(sender, message);
        // Réduction des messages en MAJ
        if (message.length() > MainBungee.getConfig().getInt("chat.min_lower_case_length")) {
            int upperCase = 0;
            int lowerCase = 0;
            for (int k = 0; k < message.length(); k++) {
                if (Character.isUpperCase(message.charAt(k))) upperCase++;
                if (Character.isLowerCase(message.charAt(k))) lowerCase++;
            }
            if (upperCase>lowerCase) {
                message = message.toLowerCase();
            }
        }
        // Retirer les mots interdits
        String[] messages = message.split("\\s+");
        for (String word : messages) {
            if (MainBungee.getConfig().getList("chat.banned_words").contains(word.toLowerCase())) {
                message = message.replace(word, word.replaceAll(".","*"));
            }
        }
        return message;
    }

    private boolean isOnline(UUID uuid){
        return ProxyServer.getInstance().getPlayer(uuid) != null;
    }
}
