package com.valorin.caches;

import com.valorin.Main;
import com.valorin.api.event.EnergyChangedEvent;
import com.valorin.data.Data;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class EnergyCache {
    private final Map<String, Double> map = new HashMap<>();
    private BukkitTask timer;
    private double maxEnergy;
    private double energyNeeded;
    private double energyResumePerSecond;
    private final boolean isEnable;

    public EnergyCache() {
        isEnable = Main.getInstance().getConfigManager().isEnergyEnabled();
        if (!isEnable) {
            return;
        }
        try {
            for (Player player : ViaVersion.getOnlinePlayers()) {
                load(player.getName());
            }
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                load(player.getName());
            }
            Debug.send("精力值数据缓存已就绪", "The energy cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c精力值数据缓存未能加载",
                    "§cThe energy cache failed to initialize");
            e.printStackTrace();
        }
        maxEnergy = Main.getInstance().getConfigManager().getMaxEnergy();
        if (maxEnergy <= 0) {
            maxEnergy = 300.0;
        }
        energyNeeded = Main.getInstance().getConfigManager().getEnergyNeeded();
        if (energyNeeded <= 0) {
            energyNeeded = 90.0;
        }
        energyResumePerSecond = Main.getInstance().getConfigManager()
                .getEnergyResumePerSecond();
        if (energyResumePerSecond <= 0) {
            energyResumePerSecond = 0.5;
        }
        timer = new BukkitRunnable() {
            @Override
            public void run() {
                for (String name : new HashMap<>(map).keySet()) {
                    if (!map.containsKey(name)) {
                        map.put(name, maxEnergy);
                    }
                    double energyNow = map.get(name);
                    if (maxEnergy - energyNow <= energyResumePerSecond) {
                        map.put(name, maxEnergy);
                    } else {
                        map.put(name, energyNow + energyResumePerSecond);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);
    }

    public double get(String name) {
        try {
            return map.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public void load(String name) {
        if (!map.containsKey(name)) { //服务器开启后首次进入，精力值加满
            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(),
                    () -> {
                        map.put(name, Main.getInstance().getConfigManager().getMaxEnergy());
                    });
        }
    }

    public void save(boolean isAsynchronously) {
        if (!isEnable) {
            return;
        }
        for (String name : map.keySet()) {
            Data.setEnergy(name, map.get(name), isAsynchronously);
        }
        Debug.send("精力值数据已自动保存", "The energy data saved automatically");
    }

    public void set(String name, double energy) {
        if (!map.containsKey(name)) {
            map.put(name, 0.0);
        }
        double log = map.get(name);
        map.put(name, energy);
        if (Bukkit.getPlayer(name) != null && Bukkit.getPlayer(name).isOnline()) {
            EnergyChangedEvent event = new EnergyChangedEvent(
                    Bukkit.getPlayer(name), log, energy);
            Bukkit.getServer().getPluginManager().callEvent(event);
        }
    }

    public double getMaxEnergy() { // 获取最大精力值
        return maxEnergy;
    }

    public double getEnergyNeeded() { // 获取每场所需精力值
        return energyNeeded;
    }

    public double getEnergyResumePerSecond() { // 获取每秒恢复的精力值
        return energyResumePerSecond;
    }

    public boolean isEnable() { // 本功能是否开启
        return isEnable;
    }

    public void cancelTimer() {
        if (!isEnable) {
            return;
        }
        timer.cancel();
    }
}
