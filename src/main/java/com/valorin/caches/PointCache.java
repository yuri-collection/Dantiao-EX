package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PointCache {
    private Map<String, Double> map = new HashMap<String, Double>();
    private List<String> changeList = new ArrayList<String>();

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
        if (!map.keySet().contains(name)) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                    () -> {
                        double points = Data.getPoint(name);
                        map.put(name, points);
                    });
        }
    }

    public void save(boolean isAsyn) {
        if (changeList.size() != 0) {
            for (String name : changeList) {
                Data.setPoint(name, map.get(name), isAsyn);
            }
            changeList.clear();
            Debug.send("竞技场积分数据已自动保存", "Points data saved automatically");
        }
    }

    public void set(String name, double points) {
        map.put(name, points);
        if (!changeList.contains(name)) {
            changeList.add(name);
        }
    }
}
