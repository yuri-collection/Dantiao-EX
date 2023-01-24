package com.valorin.task;

import com.valorin.arenas.Arena;
import com.valorin.caches.*;
import com.valorin.configuration.ConfigManager;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.ranking.RankingData;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.*;
import static com.valorin.dan.ExpChange.changeExp;
import static com.valorin.util.SyncBroadcast.bc;

public class SettleEnd {
    public static void settle(Arena arena, Player winner, Player loser, boolean isDraw) {
        int time = arena.getTime();
        int startWay = arena.getStartWay();
        String winnerName = winner.getName();
        String loserName = loser.getName();

        double player1Damage = arena.getDamage(true);
        double player1MaxDamage = arena.getMaxDamage(true);
        double player2Damage = arena.getDamage(false);
        double player2MaxDamage = arena.getMaxDamage(false);

        double winnerDamage, winnerMaxDamage, loserDamage, loserMaxDamage;
        if (arena.isp1(winnerName)) {
            winnerDamage = player1Damage;
            winnerMaxDamage = player1MaxDamage;
            loserDamage = player2Damage;
            loserMaxDamage = player2MaxDamage;
        } else {
            winnerDamage = player2Damage;
            winnerMaxDamage = player2MaxDamage;
            loserDamage = player1Damage;
            loserMaxDamage = player1MaxDamage;
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(cal.getTime());
        ConfigManager configManager = getInstance().getConfigManager();
        DanCache danCache = getInstance().getCacheHandler().getDan();
        PointCache pointCache = getInstance().getCacheHandler().getPoint();
        RecordCache recordCache = getInstance().getCacheHandler().getRecord();

        String server = getInstance().getConfigManager().getServerName();
        int loserResult = 0, winnerResult = 0;

        double pointsAwarded = 0, pointsDeducted = 0;
        int winExpAwarded = 0, drawExpAwarded = 0, maxExpAwarded = 0;
        // 匹配赛
        if (startWay == 1) {
            pointsAwarded = configManager.getPointsAwardedByPanel();
            pointsDeducted = configManager.getPointsDeductedByPanel();
            winExpAwarded = configManager.getExpAwardedByPanel();
            drawExpAwarded = configManager.getDrawExpAwardedByPanel();
            maxExpAwarded = configManager.getMaxExpAwardedByPanel();
        }
        // 邀请赛
        if (startWay == 2) {
            pointsAwarded = configManager.getPointsAwardedByInviting();
            pointsDeducted = configManager.getPointsDeductedByInviting();
            winExpAwarded = configManager.getExpAwardedByInviting();
            drawExpAwarded = configManager.getDrawExpAwardedByInviting();
            maxExpAwarded = configManager.getMaxExpAwardedByInviting();
        }
        // 强制赛
        if (startWay == 3) {
            pointsAwarded = configManager.getPointsAwardedByCompulsion();
            pointsDeducted = configManager.getPointsDeductedByCompulsion();
            winExpAwarded = configManager.getExpAwardedByCompulsion();
            drawExpAwarded = configManager.getDrawExpAwardedByCompulsion();
            maxExpAwarded = configManager.getMaxExpAwardedByCompulsion();
        }

        if (!isDraw) {
            loserResult = 1;
            winnerResult = 0;

            if (pointsAwarded != 0) {
                double now = pointCache.get(winnerName);
                pointCache.set(winnerName, now + pointsAwarded);
                sm("&b做的不错！奖励你 &d{points} &b点单挑积分", winner, "points",
                        new String[]{"" + pointsAwarded});
            }
            if (pointsDeducted != 0) {
                double now = pointCache.get(loserName);
                double nowShowed;
                double pointsDeductedShowed = pointsDeducted;
                if (now > 0) {
                    if (now - pointsDeducted >= 0) {
                        pointCache.set(loserName, now - pointsDeducted);
                        nowShowed = now - pointsDeducted;
                    } else {
                        pointCache.set(loserName, 0);
                        nowShowed = 0;
                        pointsDeductedShowed = now;
                    }
                } else {
                    nowShowed = 0;
                    pointsDeductedShowed = 0;
                }
                sm("&7你被扣除了 &d{points} &7点单挑积分，现在还剩 &6{now} &7点", loser, "points now",
                        new String[]{"" + pointsDeductedShowed, "" + nowShowed});
            }

            recordCache.addWins(winnerName);
            recordCache.addLoses(loserName);
        } else {
            recordCache.addDraws(winnerName);
            recordCache.addDraws(loserName);
        }

        ArenaInfoCache arenaInfoCache = getInstance().getCacheHandler()
                .getArenaInfo();
        String displayName = arenaInfoCache.get(arena.getName())
                .getDisplayName();
        String editName = arena.getName();
        int winnerExpChange = 0;
        int loserExpChange = 0;
        // 有赢家产生，刷新排名，给予经验
        if (!isDraw) {
            RankingCache rankingCache = getInstance().getCacheHandler()
                    .getRanking();
            RankingData rankingData = getInstance().getRanking();

            int winnerRank = rankingData.getWinRank(winnerName);
            int loserRank = rankingData.getWinRank(loserName);

            int winnerRank2 = rankingData.getKDRank(winnerName);
            int loserRank2 = rankingData.getKDRank(loserName);

            int winnerOrder = 0;
            if (rankingCache.getWin() != null) {
                for (int i = 0; i < rankingCache.getWin().size(); i++) {
                    if (rankingCache.getWin().get(i).split("\\|")[0]
                            .equals(winnerName)) {
                        winnerOrder = i;
                    }
                }
            }
            int loserOrder = 0;
            if (rankingCache.getWin() != null) {
                for (int i = 0; i < rankingCache.getWin().size(); i++) {
                    if (rankingCache.getWin().get(i).split("\\|")[0]
                            .equals(loserName)) {
                        loserOrder = i;
                    }
                }
            }

            if (winnerOrder <= loserOrder) {// winner本来就强过loser
                // 等号则代表没有排行数据，winner优先
                rankingData.rank(winnerName + "|" + recordCache.getWins(winnerName), true);
                rankingData.rank(loserName + "|" + recordCache.getWins(loserName), true);
            } else {
                rankingData.rank(loserName + "|" + recordCache.getWins(loserName), true);
                rankingData.rank(winnerName + "|" + recordCache.getWins(winnerName), true);
            }

            int winnerOrder2 = 0;
            if (rankingCache.getKD() != null) {
                for (int i = 0; i < rankingCache.getKD().size(); i++) {
                    if (rankingCache.getKD().get(i).split("\\|")[0]
                            .equals(winnerName)) {
                        winnerOrder2 = i;
                    }
                }
            }
            int loserOrder2 = 0;
            if (rankingCache.getKD() != null) {
                for (int i = 0; i < rankingCache.getKD().size(); i++) {
                    if (rankingCache.getKD().get(i).split("\\|")[0]
                            .equals(loserName)) {
                        loserOrder2 = i;
                    }
                }
            }
            if (winnerOrder2 <= loserOrder2) {// winner本来就强过loser
                // 等号则代表没有排行数据，winner优先
                if (recordCache.getLoses(winnerName) != 0) {
                    rankingData.rank(
                            winnerName
                                    + "|"
                                    + ((double) recordCache.getWins(winnerName) / (double) recordCache
                                    .getLoses(winnerName)), false);
                } else {
                    rankingData.rank(
                            winnerName + "|"
                                    + ((double) recordCache.getWins(winnerName)),
                            false);
                }

                if (recordCache.getLoses(loserName) != 0) {
                    rankingData.rank(
                            loserName
                                    + "|"
                                    + ((double) recordCache.getWins(loserName) / (double) recordCache
                                    .getLoses(loserName)), false);
                } else {
                    rankingData.rank(
                            loserName + "|" + ((double) recordCache.getWins(loserName)),
                            false);
                }
            } else {
                if (recordCache.getLoses(loserName) != 0) {
                    rankingData.rank(
                            loserName
                                    + "|"
                                    + ((double) recordCache.getWins(loserName) / (double) recordCache
                                    .getLoses(loserName)), false);
                } else {
                    rankingData.rank(
                            loserName + "|" + ((double) recordCache.getWins(loserName)),
                            false);
                }
                if (recordCache.getLoses(winnerName) != 0) {
                    rankingData.rank(
                            winnerName
                                    + "|"
                                    + ((double) recordCache.getWins(winnerName) / (double) recordCache
                                    .getLoses(winnerName)), false);
                } else {
                    rankingData.rank(
                            winnerName + "|"
                                    + ((double) recordCache.getWins(winnerName)),
                            false);
                }
            }

            if (winnerRank != rankingData.getWinRank(winnerName)
                    && rankingData.getWinRank(winnerName) != 0) {
                sm("&b胜场排名发生变更！&e{before}->{now}",
                        winner,
                        "before now",
                        new String[]{"" + winnerRank,
                                "" + rankingData.getWinRank(winnerName)});
            }
            if (loserRank != rankingData.getWinRank(loserName)
                    && rankingData.getWinRank(loserName) != 0) {
                sm("&b胜场排名发生变更！&e{before}->{now}",
                        loser,
                        "before now",
                        new String[]{"" + loserRank,
                                "" + rankingData.getWinRank(loserName)});
            }
            if (winnerRank2 != rankingData.getKDRank(winnerName)
                    && rankingData.getWinRank(winnerName) != 0) {
                sm("&bKD排名发生变更！&e{before}->{now}",
                        winner,
                        "before now",
                        new String[]{"" + winnerRank2,
                                "" + rankingData.getKDRank(winnerName)});
            }
            if (loserRank2 != rankingData.getKDRank(loserName)
                    && rankingData.getWinRank(loserName) != 0) {
                sm("&bKD排名发生变更！&e{before}->{now}",
                        loser,
                        "before now",
                        new String[]{"" + loserRank2,
                                "" + rankingData.getKDRank(loserName)});
            }

            boolean b1 = arena.isp1(winnerName);
            int winnerExp;
            int loserExp;
            winnerExp = Math.min(arena.getExp(b1), maxExpAwarded);
            winnerExp = winnerExp + winExpAwarded;
            loserExp = winnerExp / 3;

            DanHandler dh = getInstance().getDanHandler();

            boolean protection;
            int protectionExp = configManager.getProtectionExp();
            int winnerExpNow = danCache.get(winnerName);
            int loserExpNow = danCache.get(loserName);
            if (protectionExp == 0) { // 有保护措施
                protection = false;
            } else {
                protection = winnerExpNow - loserExpNow >= protectionExp
                        || loserExpNow - winnerExpNow >= protectionExp;
            }

            int loserExpShow;
            int winnerExpShow;
            if (protection) {
                loserExpShow = 0;
                winnerExpShow = 0;
                sm("&c双方段位差距过大，段位经验不会变更", winner, loser);
            } else {
                changeExp(winner, winnerExpNow + winnerExp);
                winnerExpChange = winnerExp;
                winnerExpShow = winnerExp;
                loserExpShow = loserExp;
                if (loserExpNow - loserExp > dh.getThreshold()) {
                    changeExp(loser, loserExpNow - loserExp); // 正常扣除经验
                    loserExpChange = -loserExp;
                } else {
                    if (loserExpNow > dh.getThreshold()) {
                        loserExpShow = loserExpNow - dh.getThreshold();
                        changeExp(loser, dh.getThreshold()); // 设置为保底经验
                        loserExpChange = -(loserExpNow - dh.getThreshold());
                    } else {
                        loserExpShow = 0; // 不扣经验
                    }
                }
            }

            sml("&7============================================================| |                       &b比赛结束！|        &7恭喜获得了胜利，期待你下一次更加精彩得表现！|                  &7同时获得了 &a{exp} &7经验| |&7============================================================",
                    winner, "exp", new String[]{"" + winnerExpShow});
            sml("&7============================================================| |                     &b比赛结束！|           &7你没有获胜，不要灰心，再接再厉！|                &7同时损失了 &c{exp} &7经验| |&7============================================================",
                    loser, "exp", new String[]{"" + loserExpShow});
            if (displayName == null) {
                displayName = arena.getName();
            } else {
                displayName = displayName.replace("&", "§");
            }
            bc(gm("&b[战报]: &7玩家 &e{winner} &7在单挑赛场&r{arenaname}&r&7上以 &6{time}秒 &7击败玩家 &e{loser}",
                    null, "winner arenaname time loser", new String[]{winnerName,
                            displayName, arena.getTime() + "", loserName}),
                    startWay);

            dh.refreshPlayerDan(winnerName);
            dh.refreshPlayerDan(loserName);
            if (arena.getDan(arena.isp1(winnerName)) != null) {
                CustomDan danBefore = arena.getDan(arena.isp1(winnerName));
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (!danBefore.equals(danNow)) {
                    bc(gm("&a[恭喜]: &7玩家 &e{player} &7的单挑段位成功升到了&r{dan}", null,
                            "player dan",
                            new String[]{winnerName, danNow.getDisplayName()}),
                            startWay);
                }
            } else {
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (danNow != null) {
                    bc(gm("&a[恭喜]: &7玩家 &e{player} &7突破了无段位的身份，首次获得了段位：&r{dan}&7！祝TA在单挑战斗的路上越走越远！",
                            null, "player dan",
                            new String[]{winnerName, danNow.getDisplayName()}),
                            startWay);
                }
            }

            // 连胜
            int winTime = recordCache.getWinningStreakTimes(winnerName);
            int maxWinTime = recordCache.getMaxWinningStreakTimes(winnerName);
            recordCache.addWinningStreakTimes(winnerName);
            if (winTime + 1 > maxWinTime) {
                recordCache.addMaxWinningStreakTimes(winnerName);
            }
            recordCache.clearWinningStreakTimes(loserName);
            int reportTimes = configManager.getBroadcastWinningStreakTimes();
            if (reportTimes == 0) {
                reportTimes = 3;
            }
            if (winTime + 1 >= reportTimes) {
                bc(gm("&a[恭喜]: &7玩家 &e{player} &7在竞技场上完成了 &b{times} &7连胜！",
                        null, "player times", new String[]{winnerName,
                                (winTime + 1) + ""}), startWay);
            }
        } else { // 平局
            changeExp(winner, danCache.get(winnerName) + drawExpAwarded);
            changeExp(loser, danCache.get(loserName) + drawExpAwarded);
            winnerExpChange = drawExpAwarded;
            loserExpChange = winnerExpChange;
            sml("&7============================================================| |                    &b比赛结束！|          &7比赛超时！未决出胜负，判定为平局！|          &7同时获得了 &a{exp} &7经验| |&7============================================================",
                    winner, "exp", new String[]{"" + drawExpAwarded});
            sml("&7============================================================| |                    &b比赛结束！|          &7比赛超时！未决出胜负，判定为平局！|          &7同时获得了 &a{exp} &7经验| |&7============================================================",
                    loser, "exp", new String[]{"" + drawExpAwarded});
            if (displayName == null) {
                displayName = editName;
            } else {
                displayName = displayName.replace("&", "§");
            }
            bc(gm("&b[战报]: &7玩家 &e{p1} &7与 &e{p2} &7在单挑赛场&r{arenaname}&r&7上打成平手，实为精妙！",
                    null, "p1 arenaname p2", new String[]{winnerName,
                            displayName, loserName}), startWay);
        }

        getInstance().getHologramManager().setIsNeedRefresh(true);
        recordCache
                .add(loserName, date, winnerName, server, time, loserDamage,
                        loserMaxDamage, loserResult, startWay, loserExpChange,
                        editName);
        recordCache.add(winnerName, date, loserName, server, time, winnerDamage,
                winnerMaxDamage, winnerResult, startWay, winnerExpChange,
                editName);
        recordCache.refreshServerTotalGameTimes();
    }
}
