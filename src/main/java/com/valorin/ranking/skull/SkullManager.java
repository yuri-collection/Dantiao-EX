package com.valorin.ranking.skull;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.data.encapsulation.RankingSkull;
import com.valorin.ranking.RankingData;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class SkullManager {
    public static int refreshTaskId = -1;

    public static void initialize() {
        int interval = Main.getInstance().getConfigManager()
                .getSkullRefreshInterval();
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
        List<RankingSkull> rankingSkullList = cache.getRankingSkullList();
        int skullNumber = rankingSkullList.size();
        if (skullNumber == 0) {
            return;
        }
        int successes = 0;
        RankingData rankingData = Main.getInstance().getRanking();
        for (RankingSkull rankingSkull : rankingSkullList) {
            //若所在世界被删了，位置就是空值，则暂不更新
            Location location = rankingSkull.getLocation();
            if (location == null) {
                continue;
            }
            //如果不是头颅方块，则暂不更新
            Block skullBlock = location.getBlock();
            if (!ViaVersion.isPlayerSkull(skullBlock.getType())) {
                continue;
            }
            Skull skull = (Skull) skullBlock.getState();
            String rankingType = rankingSkull.getRankingType();
            int ranking = rankingSkull.getRanking();
            String playerName;
            if (rankingType.equalsIgnoreCase("kd")) {
                playerName = rankingData.getPlayerByKDRank(ranking);
            } else {
                playerName = rankingData.getPlayerByWinRank(ranking);
            }
            if (playerName != null) { //如果为空，说明那个排名虚位以待
                skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
                skull.update();
                successes++;
            }
        }
        if (auto) {
            Bukkit.getConsoleSender().sendMessage(gm("&7排行头颅已自动刷新...成功刷新数:{successes}/{total}", null, "total successes", new String[]{"" + skullNumber, "" + successes}));
        }
    }

    public static void reload() {
        if (refreshTaskId != -1) {
            Bukkit.getScheduler().cancelTask(refreshTaskId);
        }
        initialize();
    }
}
