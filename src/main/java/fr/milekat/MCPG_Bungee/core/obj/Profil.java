package fr.milekat.MCPG_Bungee.core.obj;

import java.util.Date;
import java.util.UUID;

public class Profil {
    private final UUID uuid;
    private final String name;
    private final int team;
    private String muted;
    private String banned;
    private String reason;
    private boolean mods;
    private final boolean maintenance;
    private int chatSpam;
    private String chatLast;
    private Date chatLastDate;

    public Profil(UUID uuid, String name, int team, String muted, String banned, String reason, boolean maintenance) {
        this.uuid = uuid;
        this.name = name;
        this.team = team;
        this.muted = muted;
        this.banned = banned;
        this.reason = reason;
        this.maintenance = maintenance;
        this.chatSpam = 0;
        this.chatLast = "";
        this.chatLastDate = new Date();
    }

    public UUID getUuid() { return uuid; }

    public String getName() { return name; }

    public int getTeam() { return team; }

    public String getMuted() { return muted; }

    public boolean isMute() { return this.muted!=null; }

    public void setMuted(String muted) { this.muted = muted; }

    public String getBanned() { return banned; }

    public boolean isBan() { return this.banned!=null; }

    public void setBanned(String banned) { this.banned = banned; }

    public boolean isMods() { return mods; }

    public void setMods(boolean mods) { this.mods = mods; }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public boolean notMaintenance() { return !maintenance; }

    public void setChatSpam(int chatSpam) { this.chatSpam = chatSpam; }

    public int getChatSpam() { return chatSpam; }

    public void setChatLast(String chatLast) { this.chatLast = chatLast; }

    public String getChatLast() { return chatLast; }

    public void setChatLastDate(Date chatLastDate) { this.chatLastDate = chatLastDate; }

    public Date getChatLastDate() { return chatLastDate; }
}
