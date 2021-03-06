package fr.milekat.MCPG_Bungee.core.obj;

import java.util.Date;
import java.util.UUID;

public class Profile {
    private final String name;
    private final UUID uuid;
    private final int team;
    private final Date muted;
    private final Date banned;
    private final String reason;
    private final boolean maintenance;

    public Profile(String name, UUID uuid, int team, Date muted, Date banned, String reason, boolean maintenance) {
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

    public Date getMuted() { return muted; }

    public boolean isMute() { return this.muted!=null; }

    public Date getBanned() { return banned; }

    public boolean isBan() { return this.banned!=null; }

    public String getReason() { return reason; }

    public boolean notMaintenance() { return !maintenance; }
}
