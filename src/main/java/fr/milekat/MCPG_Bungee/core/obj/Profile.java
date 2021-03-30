package fr.milekat.MCPG_Bungee.core.obj;

import java.util.UUID;

public class Profile {
    private final String name;
    private final UUID uuid;
    private final int team;
    private String muted;
    private String banned;
    private String reason;
    private final boolean maintenance;

    public Profile(String name, UUID uuid, int team, String muted, String banned, String reason, boolean maintenance) {
        this.name = name;
        this.uuid = uuid;
        this.team = team;
        this.muted = muted;
        this.banned = banned;
        this.reason = reason;
        this.maintenance = maintenance;
    }

    public String getName() { return name; }

    public UUID getUuid() { return uuid; }

    public int getTeam() { return team; }

    public String getMuted() { return muted; }

    public boolean isMute() { return this.muted!=null; }

    public void setMuted(String muted) { this.muted = muted; }

    public String getBanned() { return banned; }

    public boolean isBan() { return this.banned!=null; }

    public void setBanned(String banned) { this.banned = banned; }

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public boolean notMaintenance() { return !maintenance; }
}
