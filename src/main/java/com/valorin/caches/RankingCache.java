package com.valorin.caches;

import com.valorin.data.Data;
import com.valorin.util.Debug;

import java.util.List;

public class RankingCache {
    private List<String> win;
    private List<String> KD;

    public RankingCache() {
        try {
            win = Data.getWinRanking();
            KD = Data.getKDRanking();
            Debug.send("排行榜缓存已就绪", "The ranking cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c排行榜缓存未能就绪", "§cThe ranking cache failed to initialize");
            e.printStackTrace();
        }
    }

    public List<String> getWin() {
        return win;
    }

    public void setWin(List<String> list) {
        win = list;
        Data.setRanking(0, list);
    }

    public List<String> getKD() {
        return KD;
    }

    public void setKD(List<String> list) {
        KD = list;
        Data.setRanking(1, list);
    }
}
