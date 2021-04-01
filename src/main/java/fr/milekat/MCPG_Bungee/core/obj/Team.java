package fr.milekat.MCPG_Bungee.core.obj;

import java.util.ArrayList;

public class Team {
    private final String name;
    private final ArrayList<Profile> members;

    public Team(String name, ArrayList<Profile> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() { return name; }

    public ArrayList<Profile> getMembers() { return members; }
}

