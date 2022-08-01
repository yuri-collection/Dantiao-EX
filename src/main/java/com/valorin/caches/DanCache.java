package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class DanCache {
    private final Map<String, Integer> map = new HashMap<>();

    public DanCache() {
        try {
            for (Player player : ViaVersion.getOnlinePlayers()) {
                load(player.getName());
            }
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                load(player.getName());
            }
            Debug.send("段位经验数据缓存已就绪", "The Dan Exp cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c段位经验数据缓存未能加载",
                    "§cThe Dan Exp cache failed to initialize");
            e.printStackTrace();
        }
    }

    public int get(String name) {
        try {
            return map.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public void load(String name) {
        if (!map.containsKey(name)) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                    () -> {
                        int exp = Data.getDanExp(name);
                        map.put(name, exp);
                    });
        }
    }

    public void set(String name, int exp) {
        map.put(name, exp);
        Main.getInstance().getDanHandler().refreshPlayerDan(name);
        Data.setDanExp(name, exp, true);
    }
}
