package com.valorin.data.encapsulation;

public class Record {
    private String name, date, opponent, server, arenaEditName;
    private int time, result, startWay, expChange;
    private double damage, maxDamage;

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
