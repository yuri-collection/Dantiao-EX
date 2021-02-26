package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class AreaCache {
    private Location lobbyLocation;
    private Location winRankingLocation;
    private Location KDRankingLocation;

    public AreaCache() {
        try {
            lobbyLocation = Data.getLobbyLocation();
            winRankingLocation = Data.getHologramLocation(0);
            KDRankingLocation = Data.getHologramLocation(1);
            Debug.send("大厅、全息图位置缓存已就绪",
                    "The lobby and hologram location cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c大厅、全息图位置缓存未能加载",
                    "§cThe lobby and hologram location cache failed to initialize");
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
}
