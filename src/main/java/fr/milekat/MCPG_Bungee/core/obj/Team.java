package fr.milekat.MCPG_Bungee.core.obj;

import java.util.ArrayList;

public class Team {
    private final String name;
    private final int money;
    private final ArrayList<Profile> members;

    public Team(String name, int money, ArrayList<Profile> members) {
        this.name = name;
        this.money = money;
        this.members = members;
    }

    public String getName() { return name; }

    public ArrayList<Profile> getMembers() { return members; }

    public int getMoney() { return money; }
}

