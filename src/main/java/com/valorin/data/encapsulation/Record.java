package com.valorin.data.encapsulation;

public class Record {
    private final String name;
    private final String date;
    private final String opponent;
    private final String server;
    private final String arenaEditName;
    private final int time;
    private final int result;
    private final int startWay;
    private final int expChange;
    private final double damage;
    private double maxDamage;

    public Record(String name, String date, String opponent, String server,
                  int time, double damage, double maxDamage, int result,
                  int startWay, int expChange, String arenaEditName) {
        this.name = name;
        this.date = date;
        this.opponent = opponent;
        this.server = server;
        this.time = time;
        this.damage = damage;
        this.maxDamage = maxDamage;
        this.result = result;
        this.startWay = startWay;
        this.expChange = expChange;
        this.arenaEditName = arenaEditName;
    }

    public String getPlayerName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getOpponentName() {
        return opponent;
    }

    public String getServerName() {
        return server;
    }

    public int getTime() {
        return time;
    }

    public double getDamage() {
        return damage;
    }

    public double getMaxDamage() {
        return maxDamage;
    }

    public int getResult() {
        return result;
    }

    public int getStartWay() {
        return startWay;
    }

    public int getExpChange() {
        return expChange;
    }

    public String getArenaEditName() {
        return arenaEditName;
    }
}
