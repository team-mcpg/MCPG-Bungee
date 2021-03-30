package fr.milekat.MCPG_Bungee.core.obj;

import java.util.ArrayList;

public class Team {
    private final int id;
    private final String name;
    private final String tag;
    private final int money;
    private final ArrayList<Profile> members;

    public Team(int id, String name, String tag, int money, ArrayList<Profile> members) {
        this.id = id;
        this.name = name;
        this.tag = tag;
        this.money = money;
        this.members = members;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public int getMoney() {
        return money;
    }

    public ArrayList<Profile> getMembers() {
        return members;
    }

    public int getSize() {
        return this.members.size();
    }
}

