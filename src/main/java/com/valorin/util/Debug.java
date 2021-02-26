package com.valorin.util;

import com.valorin.Main;
import org.bukkit.Bukkit;

public class Debug {
    public static void send(String Chinese, String English) {
        if (Main.getInstance().getConfigManager().isDebug()) {
            Bukkit.getConsoleSender().sendMessage("§e[Dantiao Debug]");
            Bukkit.getConsoleSender().sendMessage("§7" + English);
            Bukkit.getConsoleSender().sendMessage("§7" + Chinese);
        }
    }
}
