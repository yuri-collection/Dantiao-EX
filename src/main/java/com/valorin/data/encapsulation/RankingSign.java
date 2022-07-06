package com.valorin.data.encapsulation;

import org.bukkit.Location;

public class RankingSign {
    private final String editName;
    private final String rankingType;
    private final int ranking;
    private final Location location;

    public RankingSign(String editName, String rankingType, int ranking, Location location) {
        this.editName = editName;
        this.rankingType = rankingType;
        this.ranking = ranking;
        this.location = location;
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
}
