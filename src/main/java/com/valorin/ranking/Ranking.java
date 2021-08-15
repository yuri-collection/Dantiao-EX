package com.valorin.ranking;

import com.valorin.Main;
import com.valorin.caches.RankingCache;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Ranking {
    public void rank(String s, boolean isWin) {// 添加排行榜元素
        RankingCache cache = Main.getInstance().getCacheHandler().getRanking();
        List<String> ranking;
        if (isWin) {
            ranking = cache.getWin();
        } else {
            ranking = cache.getKD();
        }
        if (ranking.size() != 0) {
            for (int i = 0; i < ranking.size(); i++) {
                if (ranking.get(i).split("\\|")[0].equals(s.split("\\|")[0])) {
                    ranking.remove(i);
                }
            }
            double n = Double.parseDouble(s.split("\\|")[1]);
            int originalSize = ranking.size();
            ranking.add("");
            for (int i = (originalSize - 1); i >= 0; i--) {
                try {
                    double a = Double.parseDouble(ranking.get(i).split("\\|")[1]);
                    if (n <= a) {// 如果小于等于比较对象，终止
                        for (int i2 = (originalSize - 1); i2 > i; i2--) {
                            ranking.set(i2 + 1, ranking.get(i2));
                        }
                        ranking.set(i + 1, s);
                        return;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
            for (int i2 = (originalSize - 1); i2 >= 0; i2--) {
                ranking.set(i2 + 1, ranking.get(i2));
            }
            ranking.set(0, s);
            if (isWin) {
                cache.setWin(ranking);
            } else {
                cache.setKD(ranking);
            }
        } else {
            List<String> list = new ArrayList<>();
            list.add(s);
            if (isWin) {
                cache.setWin(list);
            } else {
                cache.setKD(list);
            }
        }
    }

    public int getWin(String playerName) {// 获取胜场榜排名
        int n = 0;
        List<String> ranking = Main.getInstance().getCacheHandler()
                .getRanking().getWin();
        if (ranking.size() == 0) {
            return n;
        }
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).split("\\|")[0].equals(playerName)) {
                n = i + 1;
            }
        }
        return n;
    }

    public int getWinValue(String playerName) {// 获取某玩家的胜场数
        int n = 0;
        List<String> ranking = Main.getInstance().getCacheHandler()
                .getRanking().getWin();
        if (ranking.size() == 0) {
            return n;
        }
        for (String s : ranking) {
            if (s.split("\\|")[0].equals(playerName)) {
                return Integer.parseInt(s.split("\\|")[1]);
            }
        }
        return -1;
    }

    public String getPlayerByWin(int rank) {// 获取胜场榜排名对应的玩家
        List<String> rankingList = Main.getInstance().getCacheHandler()
                .getRanking().getWin();
        if (rankingList.size() <= rank) {
            return null;
        }
        return rankingList.get(rank).split("\\|")[0];
    }

    public int getKD(String playerName) {// 获取KD榜排名
        List<String> ranking = Main.getInstance().getCacheHandler()
                .getRanking().getKD();
        int n = 0;
        if (ranking.size() == 0) {
            return n;
        }
        for (int i = 0; i < ranking.size(); i++) {
            if (ranking.get(i).split("\\|")[0].equals(playerName)) {
                n = i + 1;
            }
        }
        return n;
    }

    public double getKDValue(String playerName) {// 获取某玩家的KD值
        int n = 0;
        List<String> ranking = Main.getInstance().getCacheHandler()
                .getRanking().getKD();
        if (ranking.size() == 0) {
            return n;
        }
        for (String s : ranking) {
            if (s.split("\\|")[0].equals(playerName)) {
                BigDecimal bg = BigDecimal.valueOf(Double.parseDouble(s
                        .split("\\|")[1]));
                return bg.setScale(2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
            }
        }
        return -1;
    }

    public String getPlayerByKD(int rank) {// 获取KD榜排名对应的玩家
        List<String> rankingList = Main.getInstance().getCacheHandler()
                .getRanking().getKD();
        if (rankingList.size() <= rank) {
            return null;
        }
        return rankingList.get(rank).split("\\|")[0];
    }
}