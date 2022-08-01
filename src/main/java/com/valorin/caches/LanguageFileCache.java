package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LanguageFileCache {
    private final Map<String, String> map = new HashMap<>();

    public LanguageFileCache() {
        try {
            for (Player player : ViaVersion.getOnlinePlayers()) {
                load(player.getName());
            }
            Debug.send("语言文件数据缓存已就绪",
                    "The language file cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c语言数据数据缓存未能加载",
                    "§cThe language file cache failed to initialize");
            e.printStackTrace();
        }
    }

    public String get(String name) {
        return map.get(name);
    }

    public void load(String name) {
        if (!map.containsKey(name)) {
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                    () -> {
                        String language = Data.getLanguageFile(name);
                        map.put(name, language);
                    });
        }
    }

    public void set(String name, String language) {
        map.put(name, language);
        Data.setLanguageFile(name, map.get(name), true);
    }
}
