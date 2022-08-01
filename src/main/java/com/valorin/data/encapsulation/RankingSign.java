package com.valorin.data.encapsulation;

import org.bukkit.Location;

import java.util.List;

public class RankingSign {
    private final String editName;
    private final String rankingType;
    private final int ranking;
    private final Location location;
    private final List<String> text;

    public RankingSign(String editName, String rankingType, int ranking, Location location, List<String> text) {
        this.editName = editName;
        this.rankingType = rankingType;
        this.ranking = ranking;
        this.location = location;
        this.text = text;
    }

    public Location getLocation() {
        return location;
    }

    public String getEditName() {
        return editName;
    }

    public int getRanking() {
        return ranking;
    }

    public String getRankingType() {
        return rankingType;
    }

    public List<String> getText() {
        return text;
    }
}
