package com.valorin.ranking.sign;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.data.encapsulation.RankingSign;
import com.valorin.ranking.RankingData;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class SignManager {
    public static int refreshTaskId = -1;

    public static void initialize() {
        int interval = Main.getInstance().getConfigManager()
                .getSignRefreshInterval();
        if (interval < 10) {
            interval = 10 * 20;
        } else {
            interval = interval * 20;
        }

        new BukkitRunnable() {
            public void run() {
                refreshTaskId = this.getTaskId();
                refresh(true);
            }
        }.runTaskTimer(Main.getInstance(), 100, interval);
    }

    public static void refresh(boolean auto) {
        AreaCache cache = Main.getInstance().getCacheHandler().getArea();
        List<RankingSign> rankingSignList = cache.getRankingSignList();
        int signNumber = rankingSignList.size();
        if (signNumber == 0) {
            return;
        }
        int successes = 0;
        RankingData rankingData = Main.getInstance().getRanking();
        for (RankingSign rankingSign : rankingSignList) {
            //若所在世界被删了，位置就是空值，则暂不更新
            Location location = rankingSign.getLocation();
            if (location == null) {
                continue;
            }
            //如果不是木牌方块，则暂不更新
            Block signBlock = location.getBlock();
            Material blockType = signBlock.getType();
            if (!ViaVersion.isSignPostMaterial(blockType) && !ViaVersion.isWallSignMaterial(blockType)) {
                continue;
            }
            String rankingType = rankingSign.getRankingType();
            int ranking = rankingSign.getRanking();
            String playerName;
            Object value;
            if (rankingType.equalsIgnoreCase("kd")) {
                playerName = rankingData.getPlayerByKDRank(ranking);
                value = rankingData.getKDValue(playerName);
            } else {
                playerName = rankingData.getPlayerByWinRank(ranking);
                value = rankingData.getWinValue(playerName);
            }
            Sign sign = (Sign) signBlock.getState();
            String[] lines = sign.getLines();
            if (playerName != null) {
                for (int i = 0; i < 4; i++) {
                    String line = lines[i];
                    sign.setLine(i, line.replace("%p", playerName).replace("%r", "" + ranking).replace("%v", "" + value));
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    String line = lines[i];
                    sign.setLine(i, line.replace("%p", gm("虚位以待")).replace("%r", "-").replace("%v", "-"));
                }
            }
            sign.update();
            successes++;
        }
        if (auto) {
            Bukkit.getConsoleSender().sendMessage(gm("&7排行木牌已自动刷新...成功刷新数:{successes}/{total}", null, "total successes", new String[]{"" + signNumber, "" + successes}));
        }
    }

    public static void reload() {
        if (refreshTaskId != -1) {
            Bukkit.getScheduler().cancelTask(refreshTaskId);
        }
        initialize();
    }
}
