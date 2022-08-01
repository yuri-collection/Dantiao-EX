package com.valorin.caches;

import com.valorin.Main;
import com.valorin.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CacheHandler {
    private AreaCache areaCache;
    private ArenaInfoCache arenaInfoCache;
    private BlacklistCache blacklistCache;
    private DanCache danCache;
    private EnergyCache energyCache;
    private LanguageFileCache languageFileCache;
    private PointCache pointCache;
    private RankingCache rankingCache;
    private RecordCache recordCache;
    private ShopCache shopCache;

    private int taskId = -1;

    public CacheHandler(Action action) {
        load(action);
    }

    public void load(Action action) {
        Bukkit.getScheduler().runTaskAsynchronously(
                Main.getInstance(),
                () -> {
                    long start = System.currentTimeMillis();
                    Debug.send("开始载入缓存", "The Caches loading...");
                    areaCache = new AreaCache();
                    arenaInfoCache = new ArenaInfoCache();
                    blacklistCache = new BlacklistCache();
                    danCache = new DanCache();
                    energyCache = new EnergyCache();
                    languageFileCache = new LanguageFileCache();
                    pointCache = new PointCache();
                    rankingCache = new RankingCache();
                    recordCache = new RecordCache();
                    shopCache = new ShopCache();
                    long end = System.currentTimeMillis();
                    Debug.send("所有缓存已就绪，耗时" + (end - start) + "ms",
                            "All the caches was loaded successfully in "
                                    + (end - start) + "ms");
                    Bukkit.getScheduler().runTask(Main.getInstance(),
                            action::run);
                });
        int interval = Main.getInstance().getConfigManager()
                .getAutoSaveCachesTime();
        if (interval <= 10) { // 最低10秒
            interval = 10 * 20;
        } else {
            interval = interval * 20;
        }
        new BukkitRunnable() {
            public void run() {
                if (taskId == -1) {
                    taskId = this.getTaskId();
                } else {
                    energyCache.save(true);
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, interval);
    }

    public void unload() {
        try {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;

            energyCache.cancelTimer();
            energyCache.save(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AreaCache getArea() {
        return areaCache;
    }

    public ArenaInfoCache getArenaInfo() {
        return arenaInfoCache;
    }

    public BlacklistCache getBlacklist() {
        return blacklistCache;
    }

    public DanCache getDan() {
        return danCache;
    }

    public EnergyCache getEnergy() {
        return energyCache;
    }

    public LanguageFileCache getLanguageFile() {
        return languageFileCache;
    }

    public PointCache getPoint() {
        return pointCache;
    }

    public RankingCache getRanking() {
        return rankingCache;
    }

    public RecordCache getRecord() {
        return recordCache;
    }

    public ShopCache getShop() {
        return shopCache;
    }

    public interface Action {
        void run();
    }
}
