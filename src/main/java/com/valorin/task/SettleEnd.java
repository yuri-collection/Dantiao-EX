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
        // ?????????
        if (startWay == 1) {
            pointsAwarded = configManager.getPointsAwardedByPanel();
            pointsDeducted = configManager.getPointsDeductedByPanel();
            winExpAwarded = configManager.getExpAwardedByPanel();
            drawExpAwarded = configManager.getDrawExpAwardedByPanel();
            maxExpAwarded = configManager.getMaxExpAwardedByPanel();
        }
        // ?????????
        if (startWay == 2) {
            pointsAwarded = configManager.getPointsAwardedByInviting();
            pointsDeducted = configManager.getPointsDeductedByInviting();
            winExpAwarded = configManager.getExpAwardedByInviting();
            drawExpAwarded = configManager.getDrawExpAwardedByInviting();
            maxExpAwarded = configManager.getMaxExpAwardedByInviting();
        }
        // ?????????
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
                sm("&b???????????????????????? &d{points} &b???????????????", winner, "points",
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
                sm("&7??????????????? &d{points} &7?????????????????????????????? &6{now} &7???", loser, "points now",
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
        // ?????????????????????????????????????????????
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

            if (winnerOrder <= loserOrder) {// winner???????????????loser
                // ????????????????????????????????????winner??????
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
            if (winnerOrder2 <= loserOrder2) {// winner???????????????loser
                // ????????????????????????????????????winner??????
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
                sm("&b???????????????????????????&e{before}->{now}",
                        winner,
                        "before now",
                        new String[]{"" + winnerRank,
                                "" + rankingData.getWinRank(winnerName)});
            }
            if (loserRank != rankingData.getWinRank(loserName)
                    && rankingData.getWinRank(loserName) != 0) {
                sm("&b???????????????????????????&e{before}->{now}",
                        loser,
                        "before now",
                        new String[]{"" + loserRank,
                                "" + rankingData.getWinRank(loserName)});
            }
            if (winnerRank2 != rankingData.getKDRank(winnerName)
                    && rankingData.getWinRank(winnerName) != 0) {
                sm("&bKD?????????????????????&e{before}->{now}",
                        winner,
                        "before now",
                        new String[]{"" + winnerRank2,
                                "" + rankingData.getKDRank(winnerName)});
            }
            if (loserRank2 != rankingData.getKDRank(loserName)
                    && rankingData.getWinRank(loserName) != 0) {
                sm("&bKD?????????????????????&e{before}->{now}",
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
            if (protectionExp == 0) { // ???????????????
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
                sm("&c???????????????????????????????????????????????????", winner, loser);
            } else {
                changeExp(winner, winnerExpNow + winnerExp);
                winnerExpChange = winnerExp;
                winnerExpShow = winnerExp;
                loserExpShow = loserExp;
                if (loserExpNow - loserExp > dh.getThreshold()) {
                    changeExp(loser, loserExpNow - loserExp); // ??????????????????
                    loserExpChange = -loserExp;
                } else {
                    if (loserExpNow > dh.getThreshold()) {
                        loserExpShow = loserExpNow - dh.getThreshold();
                        changeExp(loser, dh.getThreshold()); // ?????????????????????
                        loserExpChange = -(loserExpNow - dh.getThreshold());
                    } else {
                        loserExpShow = 0; // ????????????
                    }
                }
            }

            sml("&7============================================================| |                       &b???????????????|        &7??????????????????????????????????????????????????????????????????|                  &7??????????????? &a{exp} &7??????| |&7============================================================",
                    winner, "exp", new String[]{"" + winnerExpShow});
            sml("&7============================================================| |                     &b???????????????|           &7????????????????????????????????????????????????|                &7??????????????? &c{exp} &7??????| |&7============================================================",
                    loser, "exp", new String[]{"" + loserExpShow});
            if (displayName == null) {
                displayName = arena.getName();
            } else {
                displayName = displayName.replace("&", "??");
            }
            bc(gm("&b[??????]: &7?????? &e{winner} &7???????????????&r{arenaname}&r&7?????? &6{time}??? &7???????????? &e{loser}",
                    null, "winner arenaname time loser", new String[]{winnerName,
                            displayName, arena.getTime() + "", loserName}),
                    startWay);

            dh.refreshPlayerDan(winnerName);
            dh.refreshPlayerDan(loserName);
            if (arena.getDan(arena.isp1(winnerName)) != null) {
                CustomDan danBefore = arena.getDan(arena.isp1(winnerName));
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (!danBefore.equals(danNow)) {
                    bc(gm("&a[??????]: &7?????? &e{player} &7??????????????????????????????&r{dan}", null,
                            "player dan",
                            new String[]{winnerName, danNow.getDisplayName()}),
                            startWay);
                }
            } else {
                CustomDan danNow = dh.getDanByExp(winnerExpNow + winnerExpShow);
                if (danNow != null) {
                    bc(gm("&a[??????]: &7?????? &e{player} &7??????????????????????????????????????????????????????&r{dan}&7??????TA???????????????????????????????????????",
                            null, "player dan",
                            new String[]{winnerName, danNow.getDisplayName()}),
                            startWay);
                }
            }

            // ??????
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
                bc(gm("&a[??????]: &7?????? &e{player} &7???????????????????????? &b{times} &7?????????",
                        null, "player times", new String[]{winnerName,
                                (winTime + 1) + ""}), startWay);
            }
        } else { // ??????
            changeExp(winner, danCache.get(winnerName) + drawExpAwarded);
            changeExp(loser, danCache.get(loserName) + drawExpAwarded);
            winnerExpChange = drawExpAwarded;
            loserExpChange = winnerExpChange;
            sml("&7============================================================| |                    &b???????????????|          &7???????????????????????????????????????????????????|          &7??????????????? &a{exp} &7??????| |&7============================================================",
                    winner, "exp", new String[]{"" + drawExpAwarded});
            sml("&7============================================================| |                    &b???????????????|          &7???????????????????????????????????????????????????|          &7??????????????? &a{exp} &7??????| |&7============================================================",
                    loser, "exp", new String[]{"" + drawExpAwarded});
            if (displayName == null) {
                displayName = editName;
            } else {
                displayName = displayName.replace("&", "??");
            }
            bc(gm("&b[??????]: &7?????? &e{p1} &7??? &e{p2} &7???????????????&r{arenaname}&r&7?????????????????????????????????",
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
