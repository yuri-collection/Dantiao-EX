package com.valorin.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerSet {
    public static List<String> get() {
        List<String> nameList = new ArrayList<>();
        for (Player onlinePlayer : ViaVersion.getOnlinePlayers()) {
            nameList.add(onlinePlayer.getName());
        }
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            nameList.add(offlinePlayer.getName());
        }
        return nameList;
    }
}
