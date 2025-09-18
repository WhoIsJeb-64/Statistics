package com.flashbackmc.statistics.data;

public class Group {

    private String name;
    private int requiredHours;
    private int requiredXp;

    public Group(String name, int requiredHours, int requiredXp) {
        this.name = name;
        this.requiredHours = requiredHours;
        this.requiredXp = requiredXp;
    }

    public String getName() {
        return this.name;
    }

    public int getRequiredHours() {
        return this.requiredHours;
    }

    public int getRequiredXp() {
        return this.requiredXp;
    }
}
