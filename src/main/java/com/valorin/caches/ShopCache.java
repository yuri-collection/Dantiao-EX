package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.data.encapsulation.Good;
import com.valorin.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopCache {
    private List<Good> goodList = new ArrayList<>();

    public ShopCache() {
        try {
            goodList = Data.getGoodList();
            Debug.send("积分商城缓存已就绪", "The Point Shop cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c积分商城缓存未能加载", "§cThe Point Shop cache failed to initialize");
            e.printStackTrace();
        }
    }

    public List<Good> getList() {
        return goodList;
    }

    public void add(ItemStack item, double price) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            int num = Data.getHistoryGood();
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                goodList.add(new Good(num, item, price, null, null, 0, null, null));
                Data.addGood(num, item, price);
                Data.updateHistoryGood();
            });
        });
    }

    public void remove(int num) {
        Good selectedGood = null;
        for (Good good : goodList) {
            if (good.getNum() == (num)) {
                selectedGood = good;
                break;
            }
        }
        if (selectedGood != null) {
            goodList.remove(selectedGood);
            Data.removeGood(selectedGood.getNum());
        }
    }

    public int size() {
        return goodList.size();
    }

    public Good get(int num) {
        for (int i = 0; i < goodList.size(); i++) {
            Good good = goodList.get(i);
            if (good.getNum() == num) {
                return good;
            }
        }
        return null;
    }

    public int getIndexByNum(int num) {
        return goodList.indexOf(get(num));
    }

    public int getNumByIndex(int index) {
        return goodList.get(index).getNum();
    }

    public void setBroadcast(int num, String broadcast) {
        int index = -1;
        for (int i = 0; i < goodList.size(); i++) {
            Good good = goodList.get(i);
            if (good.getNum() == num) {
                index = i;
                break;
            }
        }
        goodList.get(index).setBroadcast(broadcast);
        Data.setBroadcastForGood(num, broadcast);
    }

    public void setDescription(int num, String description) {
        int index = -1;
        for (int i = 0; i < goodList.size(); i++) {
            Good good = goodList.get(i);
            if (good.getNum() == num) {
                index = i;
                break;
            }
        }
        goodList.get(index).setDescription(description);
        Data.setDescriptionForGood(num, description);
    }

    public void setDan(int num, String dan) {
        int index = -1;
        for (int i = 0; i < goodList.size(); i++) {
            Good good = goodList.get(i);
            if (good.getNum() == num) {
                index = i;
                break;
            }
        }
        goodList.get(index).setDan(dan);
        Data.setDanForGood(num, dan);
    }

    public void setCommands(int num, List<String> commands) {
        int index = -1;
        for (int i = 0; i < goodList.size(); i++) {
            Good good = goodList.get(i);
            if (good.getNum() == num) {
                index = i;
                break;
            }
        }
        goodList.get(index).setCommands(commands);
        Data.setCommandsForGood(num, commands);
    }

    public void updateSalesVolume(int num) {
        goodList.get(goodList.indexOf(get(num))).updateSalesVolume();
        Data.updateSalesVolumn(num);
    }
}
