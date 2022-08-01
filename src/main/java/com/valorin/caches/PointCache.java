package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PointCache {
    private final Map<String, Double> map = new HashMap<>();

    public PointCache() {
        try {
            for (Player player : ViaVersion.getOnlinePlayers()) {
                load(player.getName());
            }
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                load(player.getName());
            }
            Debug.send("积分数据缓存已就绪", "The points cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c积分数据缓存未能加载", "§cThe points cache failed to initialize");
            e.printStackTrace();
        }
    }

    public double get(String name) {
        try {
            return map.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public void load(String name) {
        if (!map.containsKey(name)) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                    () -> {
                        double points = Data.getPoint(name);
                        map.put(name, points);
                    });
        }
    }

    public void set(String name, double points) {
        map.put(name, points);
        Data.setPoint(name, map.get(name), true);
    }
}
