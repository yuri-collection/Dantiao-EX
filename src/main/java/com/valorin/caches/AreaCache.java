package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.data.encapsulation.RankingSign;
import com.valorin.data.encapsulation.RankingSkull;
import com.valorin.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;

public class AreaCache {
    private Location lobbyLocation;
    private Location winRankingLocation;
    private Location KDRankingLocation;
    private List<RankingSkull> rankingSkullList;
    private List<RankingSign> rankingSignList;

    public AreaCache() {
        try {
            lobbyLocation = Data.getLobbyLocation();
            winRankingLocation = Data.getHologramLocation(0);
            KDRankingLocation = Data.getHologramLocation(1);
            rankingSkullList = Data.getRankingSkullList();
            rankingSignList = Data.getRankingSignList();
            Debug.send("大厅、全息图、排行头颅、排行木牌位置缓存已就绪",
                    "The caches of location of the lobby,hologram,skull for ranking and signs for ranking have been initialized");
        } catch (Exception e) {
            Debug.send("§c大厅、全息图、排行头颅、排行木牌位置缓存未能加载",
                    "§cThe caches of location of the lobby,hologram,skull for ranking and signs for ranking failed to initialize");
            e.printStackTrace();
        }
    }

    public Location getLobby() {
        return lobbyLocation;
    }

    public void setLobby(Location location) {
        this.lobbyLocation = location;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.setLobbyLocation(location);
        });
    }

    public Location getWinRankingLocation() {
        return winRankingLocation;
    }

    public Location getKDRankingLocation() {
        return KDRankingLocation;
    }

    public void setWinRanking(Location location) {
        this.winRankingLocation = location;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.setHologramLocation(0, location);
        });
    }

    public void setKDRanking(Location location) {
        this.KDRankingLocation = location;
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.setHologramLocation(1, location);
        });
    }

    public List<RankingSkull> getRankingSkullList() {
        return rankingSkullList;
    }

    public void addRankingSkull(String editName, String rankingType, int ranking, Location location) {
        rankingSkullList.add(new RankingSkull(editName, rankingType, ranking, location));
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.addRankingSkull(editName, rankingType, ranking, location);
        });
    }

    public void removeRankingSkull(String editName) {
        RankingSkull rankingSkullSelected = null;
        for (RankingSkull rankingSkull : rankingSkullList) {
            if (rankingSkull.getEditName().equals(editName)) {
                rankingSkullSelected = rankingSkull;
                break;
            }
        }
        if (rankingSkullSelected == null) {
            return;
        }
        rankingSkullList.remove(rankingSkullSelected);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.removeRankingSkull(editName);
        });
    }

    public List<RankingSign> getRankingSignList() {
        return rankingSignList;
    }

    public void addRankingSign(String editName, String rankingType, int ranking, Location location) {
        rankingSignList.add(new RankingSign(editName, rankingType, ranking, location));
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.addRankingSign(editName, rankingType, ranking, location);
        });
    }

    public void removeRankingSign(String editName) {
        RankingSign rankingSignSelected = null;
        for (RankingSign rankingSign : rankingSignList) {
            if (rankingSign.getEditName().equals(editName)) {
                rankingSignSelected = rankingSign;
                break;
            }
        }
        if (rankingSignSelected == null) {
            return;
        }
        rankingSignList.remove(rankingSignSelected);
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            Data.removeRankingSign(editName);
        });
    }
}
