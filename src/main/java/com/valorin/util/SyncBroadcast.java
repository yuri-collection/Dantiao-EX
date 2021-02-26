package com.valorin.util;

import com.valorin.Main;
import com.valorin.configuration.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class SyncBroadcast {
    public static void bc(String message, int startWay) {
        new BukkitRunnable() {
            public void run() {
                try {
                    boolean isBroadcast = false;
                    ConfigManager configManager = Main.getInstance().getConfigManager();
                    if (startWay == 1) {
                        if (configManager.isBroadcastByPanel()) {
                            isBroadcast = true;
                        }
                    }
                    if (startWay == 2) {
                        if (configManager.isBroadcastByInviting()) {
                            isBroadcast = true;
                        }
                    }
                    if (startWay == 3) {
                        if (configManager.isBroadcastByCompulsion()) {
                            isBroadcast = true;
                        }
                    }
                    if (isBroadcast) {
                        Bukkit.broadcastMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTask(Main.getInstance());
    }
}
