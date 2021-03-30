package fr.milekat.MCPG_Bungee.chat.events;

import fr.milekat.MCPG_Bungee.MainBungee;
import fr.milekat.MCPG_Bungee.chat.ChatManager;
import fr.milekat.MCPG_Bungee.core.CoreManager;
import fr.milekat.MCPG_Bungee.core.events.CustomJedisSub;
import fr.milekat.MCPG_Bungee.core.obj.Profile;
import fr.milekat.MCPG_Bungee.data.jedis.JedisManager;
import fr.milekat.MCPG_Bungee.utils.DateMilekat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class Chat implements Listener {
    @EventHandler
    public void onDiscordChat(CustomJedisSub event) {
        String[] raw = event.getRedisMsg().split("\\|");
        String message = cleanMessages(event.getRedisMsg().replaceAll(raw[0] + "\\|", ""), UUID.fromString(raw[0]));
        if (message!=null) sendChat(UUID.fromString(raw[0]), message);
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        if (event.isCommand()) return;
        event.setCancelled(true);
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String message = cleanMessages(event.getMessage(), player.getUniqueId());
        if (message==null) return;
        if (ChatManager.CHAT_TEAM.contains(player.getUniqueId())) {
            // TODO: 31/03/2021 Message que pour la team
        } else {
            sendChat(player.getUniqueId(), message);
        }
    }

    private void sendChat(UUID uuid, String message) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        try {
            Profile profil = CoreManager.getProfile(uuid.toString());
            // TODO: 31/03/2021 Get du prefix
            String prefix = "";
            if (profil.isMute()) {
                warnMute(player, profil);
                // TODO: 31/03/2021 Send du msg au staff + joueur avec [mute]
            } else {
                // Redis
                JedisManager.sendRedis(prefix + player.getDisplayName() + message);
            }
            // Log du message dans la console, comme un message normal
            MainBungee.log("<" + player.getDisplayName() + "> " + message);
        } catch (SQLException throwable) {
            if (MainBungee.DEBUG_ERRORS) throwable.printStackTrace();
        }
    }

    private void warnMute(ProxiedPlayer player, Profile profil) {
        String str = DateMilekat.reamingToString(DateMilekat.getDate(profil.getMuted()));
        TextComponent Mute = new TextComponent(MainBungee.PREFIX + "§6Vous serez unmute dans §b" + str + "§c.");
        Mute.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new Text("§cLes modos peuvent" +
                System.lineSeparator() + "encore voir vos" + System.lineSeparator() + "messages")));
        player.sendMessage(Mute);
    }

    /**
     *      If safe_chat is enable, filter words, spamming and upercases
     * @param message message à check
     * @param sender qui envoi le msg
     * @return message check ou null !
     */
    private String cleanMessages(String message, UUID sender){
        // Évite le spam chat
        int msg_recent = ChatManager.MSG_RECENT.getOrDefault(sender,0) + 1;
        ChatManager.MSG_RECENT.put(sender, msg_recent);
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
        if (ChatManager.MSG_LAST.getOrDefault(sender,"").equalsIgnoreCase(message)) {
            if (isOnline(sender)) {
                ProxyServer.getInstance().getPlayer(sender)
                        .sendMessage(new TextComponent(MainBungee.PREFIX + "§cMessage déjà envoyé."));
            }
            return null;
        }
        ChatManager.MSG_LAST.put(sender, message);
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
        String[] messages = message.split(" ");
        for (String word : messages) {
            if (MainBungee.getConfig().getList("chat.banned_words").contains(word.toLowerCase(Locale.ROOT))) {
                message = message.replaceAll(word, word.replaceAll("\\.","*"));
            }
        }
        return message;
    }

    private boolean isOnline(UUID uuid){
        return ProxyServer.getInstance().getPlayer(uuid) != null;
    }
}
