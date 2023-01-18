package com.valorin.ranking.hologram;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.caches.RankingCache;
import com.valorin.ranking.hologram.type.TypeHolographicDisplays;
import com.valorin.ranking.hologram.type.TypeTrHologram;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.valorin.configuration.languagefile.MessageBuilder.gmLog;
import static com.valorin.configuration.languagefile.MessageSender.gm;

public class HologramManager {
    private boolean hasHD = false;
    private int useHD;// 1-HolographicDisplays 2-TrHologram
    private boolean isInitialized = false;
    private boolean isNeedRefresh = false;
    private int refreshTaskId;

    public HologramManager() {
        initialize();
    }

    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public int getRefreshTaskId() {
        return refreshTaskId;
    }

    public void setIsNeedRefresh(boolean isNeedRefresh) {
        this.isNeedRefresh = isNeedRefresh;
    }

    public void checkHologramPlugin() {
        String hd = Main.getInstance().getConfigManager()
                .getHologramPluginUsed();
        if (hd != null) {
            switch (hd) {
                case "HolographicDisplays":
                    if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                        if (Bukkit.getPluginManager()
                                .getPlugin("HolographicDisplays").isEnabled()) {
                            useHD = 1;
                            hasHD = true;
                            break;
                        }
                    }
                case "TrHologram":
                    if (Bukkit.getPluginManager().getPlugin("TrHologram") != null) {
                        if (Bukkit.getPluginManager().getPlugin("TrHologram")
                                .isEnabled()) {
                            useHD = 2;
                            hasHD = true;
                            break;
                        }
                    }
                default:
                    if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                        if (Bukkit.getPluginManager()
                                .getPlugin("HolographicDisplays").isEnabled()) {
                            useHD = 1;
                            hasHD = true;
                            break;
                        }
                    }
                    if (Bukkit.getPluginManager().getPlugin("TrHologram") != null) {
                        if (Bukkit.getPluginManager().getPlugin("TrHologram")
                                .isEnabled()) {
                            useHD = 2;
                            hasHD = true;
                            break;
                        }
                    }
            }
        }
    }

    public void refresh(boolean auto) {
        unload(0);
        load(0);
        if (auto) {
            Bukkit.getConsoleSender().sendMessage(gm("&7全息图已自动刷新..."));
        }
    }

    public void initialize() {
        if (!isInitialized) {
            checkHologramPlugin();
            if (!hasHD) {
                return;
            }

            int interval = Main.getInstance().getConfigManager()
                    .getHologramRefreshInterval();
            if (interval < 10) {
                interval = 10 * 20;
            } else {
                interval = interval * 20;
            }

            refresh(false);

            new BukkitRunnable() {
                public void run() {
                    refreshTaskId = this.getTaskId();
                    if (isNeedRefresh) {
                        refresh(true);
                        isNeedRefresh = false;
                    }
                }
            }.runTaskTimer(Main.getInstance(), interval, interval);

            isInitialized = true;
        }
    }

    public void reload() {
        checkHologramPlugin();
        if (!hasHD) {
            return;
        }
        isInitialized = false;
        initialize();
    }

    public void load(int n) {
        Main plugin = Main.getInstance();
        RankingCache rankingCache = plugin.getCacheHandler().getRanking();
        AreaCache areaCache = plugin.getCacheHandler().getArea();

        List<String> winList = new ArrayList<>();
        List<String> winListData = rankingCache.getWin();
        int max = Math.min(winListData.size(), 10);
        winList.add(gm("&b[star1]单挑-胜场排行榜[star2]"));
        for (int i = 0; i < max; i++) {
            String rankingString = getRankingString(i, winListData, true);
            if (rankingString != null) {
                winList.add(rankingString);
            }
        }
        List<String> KDList = new ArrayList<>();
        List<String> KDListData = rankingCache.getKD();
        int max2 = Math.min(KDListData.size(), 10);
        KDList.add(gm("&b[star1]单挑-KD比值排行榜[star2]"));
        for (int i = 0; i < max2; i++) {
            String rankingString = getRankingString(i, KDListData, false);
            if (rankingString != null) {
                KDList.add(rankingString);
            }
        }

        if (!hasHD) {
            return;
        }
        if (n == 0) {
            if (useHD == 1) {
                Bukkit.getScheduler()
                        .runTask(
                                plugin,
                                () -> {
                                    for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : com.gmail.filoghost.holographicdisplays.api.HologramsAPI
                                            .getHolograms(plugin)) {
                                        hologram.clearLines();
                                    }
                                });
            }
            if (useHD == 2) {
                if (TypeTrHologram.hologramWin != null) {
                    TypeTrHologram.hologramWin.destroy();
                }
                if (TypeTrHologram.hologramKD != null) {
                    TypeTrHologram.hologramKD.destroy();
                }
            }
        }

        if (n == 0 || n == 1) {
            if (useHD == 1) {
                if (TypeHolographicDisplays.hologramWin != null) {
                    TypeHolographicDisplays.hologramWin.delete();
                    TypeHolographicDisplays.hologramWin = null;
                }
            }
            if (useHD == 2) {
                if (TypeTrHologram.hologramWin != null) {
                    TypeTrHologram.hologramWin.destroy();
                    TypeTrHologram.hologramWin = null;
                }
            }

            Location winRankingLocation = areaCache.getWinRankingLocation();
            if (winRankingLocation != null) {
                if (useHD == 1) { // HolographicDisplays需要同步执行刷新操作
                    Bukkit.getScheduler()
                            .runTask(
                                    plugin,
                                    () -> {
                                        TypeHolographicDisplays.hologramWin = com.gmail.filoghost.holographicdisplays.api.HologramsAPI
                                                .createHologram(plugin,
                                                        winRankingLocation);
                                        TypeHolographicDisplays.hologramWin.appendItemLine(new ItemStack(
                                                ViaVersion
                                                        .getGoldenSwordMaterial()));
                                        for (String rankingString : winList) {
                                            TypeHolographicDisplays.hologramWin
                                                    .appendTextLine(rankingString);
                                        }
                                    });
                }
                if (useHD == 2) {
                    me.arasple.mc.trhologram.api.hologram.HologramBuilder builder = me.arasple.mc.trhologram.api.TrHologramAPI
                            .builder(winRankingLocation);
                    builder.append(viewer -> new ItemStack(ViaVersion.getGoldenSwordMaterial()));
                    for (String winListString : winList) {
                        builder.append(winListString);
                    }
                    TypeTrHologram.hologramWin = builder.build();
                }
            }  /*
             * Bukkit.getConsoleSender().sendMessage(
             * gm("&c检测到[胜场排行榜]的全息图所在的世界不存在，全息图加载失败，建议将该全息图换个位置"));
             */
        }
        if (n == 0 || n == 2) {
            if (useHD == 1) {
                if (TypeHolographicDisplays.hologramKD != null) {
                    TypeHolographicDisplays.hologramKD.delete();
                }
                TypeHolographicDisplays.hologramKD = null;
            }
            if (useHD == 2) {
                if (TypeTrHologram.hologramKD != null) {
                    TypeTrHologram.hologramKD.destroy();
                    TypeTrHologram.hologramKD = null;
                }
            }
            Location KDRankingLocation = areaCache.getKDRankingLocation();
            if (KDRankingLocation != null) {
                if (useHD == 1) {
                    Bukkit.getScheduler()
                            .runTask(
                                    plugin,
                                    () -> {
                                        TypeHolographicDisplays.hologramKD = com.gmail.filoghost.holographicdisplays.api.HologramsAPI
                                                .createHologram(plugin,
                                                        KDRankingLocation);
                                        TypeHolographicDisplays.hologramKD.appendItemLine(new ItemStack(
                                                ViaVersion
                                                        .getGoldenAxeMaterial()));
                                        for (String rankingString : KDList) {
                                            TypeHolographicDisplays.hologramKD
                                                    .appendTextLine(rankingString);
                                        }
                                    });
                }
                if (useHD == 2) {
                    me.arasple.mc.trhologram.api.hologram.HologramBuilder builder = me.arasple.mc.trhologram.api.TrHologramAPI
                            .builder(KDRankingLocation);
                    builder.append(viewer -> new ItemStack(ViaVersion.getGoldenAxeMaterial()));
                    for (String KDListString : KDList) {
                        builder.append(KDListString);
                    }
                    TypeTrHologram.hologramKD = builder.build();
                }
            }  /*
             * Bukkit.getConsoleSender().sendMessage(
             * gm("&c检测到[KD排行榜]的全息图所在的世界不存在，全息图加载失败，建议将该全息图换个位置"));
             */
        }
    }

    public void unload(int n) {
        if (!hasHD) {
            return;
        }
        if (n == 0 || n == 1) {
            if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                if (TypeHolographicDisplays.hologramWin != null) {
                    TypeHolographicDisplays.hologramWin.delete();
                    TypeHolographicDisplays.hologramWin = null;
                }
            }
            if (Bukkit.getPluginManager().getPlugin("TrHologram") != null) {
                if (TypeTrHologram.hologramWin != null) {
                    TypeTrHologram.hologramWin.destroy();
                    TypeTrHologram.hologramWin = null;
                }
            }
        }
        if (n == 0 || n == 2) {
            if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                if (TypeHolographicDisplays.hologramKD != null) {
                    TypeHolographicDisplays.hologramKD.delete();
                    TypeHolographicDisplays.hologramKD = null;
                }
            }
            if (Bukkit.getPluginManager().getPlugin("TrHologram") != null) {
                if (TypeTrHologram.hologramKD != null) {
                    TypeTrHologram.hologramKD.destroy();
                    TypeTrHologram.hologramKD = null;
                }
            }
        }
    }

    public boolean isEnabled() {
        return hasHD;
    }

    private String getRankingString(int rank, List<String> dataList,
                                    boolean isWin) {
        if (rank + 1 <= dataList.size()) {
            String playerName = dataList.get(rank).split("\\|")[0];
            BigDecimal bg = BigDecimal.valueOf(Double.parseDouble(dataList.get(rank)
                    .split("\\|")[1]));
            double value = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();

            switch (rank) {
                case 0:
                    if (isWin)
                        return gmLog("&b&l[n1] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b&l[n1] &f{player} &7[right] &a{value}",
                                null, "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 1:
                    if (isWin)
                        return gmLog("&e&l[n2] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&e&l[n2] &f{player} &7[right] &a{value}",
                                null, "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 2:
                    if (isWin)
                        return gmLog("&6&l[n3] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&6&l[n3] &f{player} &7[right] &a{value}",
                                null, "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 3:
                    if (isWin)
                        return gmLog("&b[n4] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n4] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 4:
                    if (isWin)
                        return gmLog("&b[n5] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n5] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 5:
                    if (isWin)
                        return gmLog("&b[n6] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n6] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 6:
                    if (isWin)
                        return gmLog("&b[n7] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n7] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 7:
                    if (isWin)
                        return gmLog("&b[n8] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n8] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 8:
                    if (isWin)
                        return gmLog("&b[n9] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n9] &f{player} &7[right] &a{value}", null,
                                "player value", new String[]{playerName,
                                        "" + value}, false, true);
                case 9:
                    if (isWin)
                        return gmLog("&b[n10] &f{player} &7[right] &a{value}场",
                                null, "player value", new String[]{playerName,
                                        "" + (int) value}, false, true);
                    else
                        return gmLog("&b[n10] &f{player} &7[right] &a{value}",
                                null, "player value", new String[]{playerName,
                                        "" + value}, false, true);
            }
        }
        return null;
    }
}
