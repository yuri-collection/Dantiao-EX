package com.valorin.papi;

import com.valorin.Main;
import com.valorin.caches.*;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.data.encapsulation.Record;
import com.valorin.ranking.RankingData;
import com.valorin.request.RequestsHandler;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;

public class RegPAPI extends PlaceholderExpansion {
    @Override
    public String getAuthor() {
        return "Valorin";
    }

    @Override
    public String getIdentifier() {
        return "dantiao";
    }

    @Override
    public String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String i) {
        if (player == null) {
            return null;
        }
        CacheHandler cacheHandler = Main.getInstance().getCacheHandler();
        RankingData rankingData = Main.getInstance().getRanking();
        ArenaInfoCache arenaInfoCache = cacheHandler.getArenaInfo();
        EnergyCache energyCache = cacheHandler.getEnergy();
        PointCache pointCache = cacheHandler.getPoint();
        RecordCache recordCache = cacheHandler.getRecord();
        String playerName = player.getName();
        if (i.equalsIgnoreCase("points")) {
            return "" + pointCache.get(playerName);
        }
        if (i.equalsIgnoreCase("win")) {
            return "" + recordCache.getWins(playerName);
        }
        if (i.equalsIgnoreCase("lose")) {
            return "" + recordCache.getLoses(playerName);
        }
        if (i.equalsIgnoreCase("draw")) {
            return "" + recordCache.getDraws(playerName);
        }
        if (i.equalsIgnoreCase("total")) {
            int winTime = recordCache.getWins(playerName);
            int loseTime = recordCache.getLoses(playerName);
            int drawTime = recordCache.getDraws(playerName);
            int totalTime = winTime + loseTime + drawTime;
            return "" + totalTime;
        }
        if (i.equalsIgnoreCase("isWin")) {
            int times = recordCache.getGameTimes(playerName);
            if (times == 0) {
                return gm("&7无数据", player);
            }
            try {
                Record record = recordCache.getRecords(playerName).get(times - 1);
                if (record.getResult() == 0) {
                    return gm("&a[v]胜利", player);
                }
                if (record.getResult() == 1) {
                    return gm("&c[x]败北", player);
                }
                if (record.getResult() == 2) {
                    return gm("&6[=]平局", player);
                }
            } catch (Exception e) {
                return "Loading...";
            }
        }
        if (i.equalsIgnoreCase("energy")) {
            if (energyCache.isEnable()) {
                BigDecimal bg = new BigDecimal(energyCache.get(playerName));
                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                return "" + value;
            }
        }
        if (i.equalsIgnoreCase("maxenergy")) {
            if (energyCache.isEnable()) {
                return "" + energyCache.getMaxEnergy();
            }
        }
        if (i.equalsIgnoreCase("kd")) {
            int wins = recordCache.getWins(playerName);
            int loses = recordCache.getLoses(playerName);
            if (loses == 0) {
                return "" + wins;
            } else {
                BigDecimal bg = BigDecimal.valueOf((double) wins / (double) loses);
                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                return "" + value;
            }
        }
        if (i.equalsIgnoreCase("winrank")) {
            return "" + rankingData.getWinRank(playerName);
        }
        if (i.equalsIgnoreCase("kdrank")) {
            return "" + rankingData.getKDRank(playerName);
        }
        if (i.equalsIgnoreCase("isInvited")) {
            RequestsHandler request = getInstance().getRequestsHandler();
            if (request.getSenders(player.getName()).size() != 0) {
                return gm("&a{amount}条", player, "amount", new String[]{request
                        .getSenders(player.getName()).size() + ""});
            } else {
                return gm("&7暂无", player);
            }
        }
        DanHandler dh = getInstance().getDanHandler();
        if (i.equalsIgnoreCase("duanwei")) {
            String danDisplayName;
            if (dh.getPlayerDan(player.getName()) == null) {
                danDisplayName = getInstance().getConfigManager()
                        .getInitialDanName();
            } else {
                danDisplayName = dh.getPlayerDan(player.getName()).getDisplayName();
            }
            return danDisplayName;
        }
        if (i.equalsIgnoreCase("exptolevelup")) {
            return "" + dh.getNeedExpToLevelUp(player.getName());
        }
        Record lastRecord = recordCache.getLast(playerName);
        if (i.equalsIgnoreCase("lastdamage")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            if (lastRecord.getDamage() == 0) {
                return gm("&7无数据", player);
            }
            BigDecimal bg = new BigDecimal(
                    (lastRecord.getDamage()));
            double value = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            return "" + value;
        }
        if (i.equalsIgnoreCase("lastmaxdamage")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            if (lastRecord.getMaxDamage() == 0) {
                return gm("&7无数据", player);
            }
            BigDecimal bg = BigDecimal.valueOf(lastRecord.getMaxDamage());
            double value = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            return "" + value;
        }
        if (i.equalsIgnoreCase("lastopponent")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            if (lastRecord.getOpponentName() == null) {
                return gm("&7无数据", player);
            }
            return lastRecord.getOpponentName();
        }
        if (i.equalsIgnoreCase("lasttime")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            return "" + lastRecord.getTime();
        }
        if (i.equalsIgnoreCase("lastdate")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            if (lastRecord.getDate() == null) {
                return gm("&7无数据", player);
            }
            return lastRecord.getDate();
        }
        if (i.equalsIgnoreCase("lastserver")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            if (lastRecord.getServerName() == null) {
                return gm("&7无数据", player);
            }
            return lastRecord.getServerName();
        }
        if (i.equalsIgnoreCase("lastexpchange")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            String expChangeStr = gm("&7无数据", player);
            if (lastRecord.getExpChange() != 0) {
                int expChange = lastRecord.getExpChange();
                if (expChange > 0) {
                    expChangeStr = gm("获得", player) + expChange;
                } else {
                    expChangeStr = gm("损失", player) + (-expChange);
                }
            }
            return expChangeStr;
        }
        if (i.equalsIgnoreCase("lastarenaname")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            String arenaName = gm("&7无数据", player);
            if (lastRecord.getArenaEditName() != null) {
                String editName = lastRecord.getArenaEditName();
                if (arenaInfoCache.get(editName) != null) {
                    if (arenaInfoCache.get(editName).getDisplayName() != null) {
                        arenaName = arenaInfoCache.get(editName)
                                .getDisplayName().replace("&", "§");
                    } else {
                        arenaName = editName;
                    }
                }
            }
            return arenaName;
        }
        if (i.equalsIgnoreCase("laststartway")) {
            if (lastRecord == null) {
                return gm("&7无数据", player);
            }
            String startWayStr = gm("&7无数据", player);
            int startWay = lastRecord.getStartWay();
            if (startWay == 1) {
                startWayStr = gm("匹配赛", player);
            }
            if (startWay == 2) {
                startWayStr = gm("邀请赛", player);
            }
            if (startWay == 3) {
                startWayStr = gm("强制赛", player);
            }
            return startWayStr;
        }
        if (i.equalsIgnoreCase("winning-streak")) {
            return "" + recordCache.getWinningStreakTimes(playerName);
        }
        if (i.equalsIgnoreCase("max-winning-streak")) {
            return "" + recordCache.getMaxWinningStreakTimes(playerName);
        }
        if (i.startsWith("winrank")) {
            if (!i.replace("winrank", "").startsWith("value")) {
                String numberString = i.replace("winrank", "");
                int ranking = -1;
                try {
                    ranking = Integer.parseInt(numberString);
                } catch (Exception ignored) {
                }
                if (ranking == -1) {
                    return gm("&7无数据", player);
                } else {
                    String rankingPlayerName = rankingData.getPlayerByWinRank(ranking);
                    if (rankingPlayerName == null) {
                        return gm("&7无数据", player);
                    } else {
                        return rankingPlayerName;
                    }
                }
            } else {
                String numberString = i.replace("winrankvalue", "");
                int ranking;
                try {
                    ranking = Integer.parseInt(numberString);
                } catch (Exception e) {
                    ranking = -1;
                }
                if (ranking == -1) {
                    return gm("&7无数据", player);
                } else {
                    String rankingPlayerName = rankingData.getPlayerByWinRank(ranking);
                    if (rankingPlayerName == null) {
                        return gm("&7无数据", player);
                    } else {
                        return "" + rankingData.getWinValue(rankingPlayerName);
                    }
                }
            }
        }
        if (i.startsWith("kdrank")) {
            if (!i.replace("kdrank", "").startsWith("value")) {
                String numberString = i.replace("kdrank", "");
                int ranking = -1;
                try {
                    ranking = Integer.parseInt(numberString);
                } catch (Exception ignored) {
                }
                if (ranking == -1) {
                    return gm("&7无数据", player);
                } else {
                    String rankingPlayerName = rankingData.getPlayerByKDRank(ranking);
                    if (rankingPlayerName == null) {
                        return gm("&7无数据", player);
                    } else {
                        return rankingPlayerName;
                    }
                }
            } else {
                String numberString = i.replace("kdrankvalue", "");
                int ranking;
                try {
                    ranking = Integer.parseInt(numberString);
                } catch (Exception e) {
                    ranking = -1;
                }
                if (ranking == -1) {
                    return gm("&7无数据", player);
                } else {
                    String rankingPlayerName = rankingData.getPlayerByKDRank(ranking);
                    if (rankingPlayerName == null) {
                        return gm("&7无数据", player);
                    } else {
                        return "" + rankingData.getKDValue(rankingPlayerName);
                    }
                }
            }
        }
        if (i.equalsIgnoreCase("servertotalgametimes")) {
            int value = recordCache.getServerTotalGameTimes();
            if (value == -1) {
                return "Loading...";
            }
            return "" + value;
        }
        if (i.equalsIgnoreCase("globaltotalgametimes")) {
            int value = Main.getInstance().getGlobalGameTimes().getValue();
            if (value == -1) {
                return "Loading...";
            }
            return "" + value;
        }
        if (i.equalsIgnoreCase("gametimesproportion")) {
            int serverValue = recordCache.getServerTotalGameTimes();
            int globalValue = Main.getInstance().getGlobalGameTimes()
                    .getValue();
            if (serverValue != -1 && globalValue != -1) {
                BigDecimal bg = BigDecimal.valueOf(((double) serverValue / (double) globalValue) * 100);
                double value = bg.setScale(4, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                return "" + value + "%";
            } else {
                return "Loading...";
            }
        }
        DanCache danCache = cacheHandler.getDan();
        if (i.equalsIgnoreCase("expnow")) {
            return "" + danCache.get(playerName);
        }
        DanHandler danHandler = getInstance().getDanHandler();
        if (i.equalsIgnoreCase("expneededtolevelup")) {
            return "" + danHandler.getNeedExpToLevelUp(playerName);
        }
        if (i.startsWith("expprogressbar1")) {
            List<String> bar = getExpProgressBar(player, "|", 20);
            if (i.contains("former")) {
                return bar.get(0);
            }
            if (i.contains("latter")) {
                return bar.get(1);
            }
        }
        if (i.startsWith("expprogressbar2")) {
            List<String> bar = getExpProgressBar(player, "■", 10);
            if (i.contains("former")) {
                return bar.get(0);
            }
            if (i.contains("latter")) {
                return bar.get(1);
            }
        }
        if (i.equalsIgnoreCase("expprogress")) {
            return "" + getExpProgress(player);
        }
        return "?";
    }

    private List<String> getExpProgressBar(Player player, String block,
                                           int totalNumber) {
        DanHandler dh = Main.getInstance().getDanHandler();
        String playerName = player.getName();
        int expNow = Main.getInstance().getCacheHandler().getDan()
                .get(playerName);
        int totalExpToNext, nowExpThisLevel;
        CustomDan playerDan = dh.getPlayerDan(playerName);
        if (playerDan == null) {
            totalExpToNext = dh.getThreshold();
            nowExpThisLevel = expNow;
        } else {
            int index = dh.getCustomDans().indexOf(playerDan);
            if (index == dh.getCustomDans().size() - 1) {
                totalExpToNext = -1;
                nowExpThisLevel = -1;
            } else {
                totalExpToNext = dh.getCustomDans().get(index + 1).getExp()
                        - playerDan.getExp();
                nowExpThisLevel = expNow - playerDan.getExp();
            }
        }
        StringBuilder barFormer = new StringBuilder();
        StringBuilder barLatter = new StringBuilder();
        int blockNumber;
        if (totalExpToNext == -1) {
            blockNumber = totalNumber;
        } else {
            blockNumber = (int) (((double) nowExpThisLevel / (double) totalExpToNext) * (double) totalNumber);
        }
        for (int n = 0; n < blockNumber; n++) {
            barFormer.append(block);
        }
        for (int n = 0; n < (totalNumber - blockNumber); n++) {
            barLatter.append(block);
        }
        List<String> bar = new ArrayList<>();
        bar.add(barFormer.toString());
        bar.add(barLatter.toString());
        return bar;
    }

    private double getExpProgress(Player player) {
        DanHandler dh = Main.getInstance().getDanHandler();
        String playerName = player.getName();
        int expNow = Main.getInstance().getCacheHandler().getDan()
                .get(playerName);
        int totalExpToNext, nowExpThisLevel;
        CustomDan playerDan = dh.getPlayerDan(playerName);
        if (playerDan == null) {
            totalExpToNext = dh.getThreshold();
            nowExpThisLevel = expNow;
            BigDecimal bg = BigDecimal.valueOf(((double) nowExpThisLevel / (double) totalExpToNext) * 100.0);
            return bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
        } else {
            int index = dh.getCustomDans().indexOf(playerDan);
            if (index == dh.getCustomDans().size() - 1) {
                return 100.0;
            } else {
                totalExpToNext = dh.getCustomDans().get(index + 1).getExp()
                        - playerDan.getExp();
                nowExpThisLevel = expNow - playerDan.getExp();
                BigDecimal bg = new BigDecimal(
                        ((double) nowExpThisLevel / (double) totalExpToNext) * 100.0);
                return bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
            }
        }
    }
}