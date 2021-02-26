package com.valorin.dan;

import com.valorin.Main;
import com.valorin.api.event.DanExpChangedEvent;
import com.valorin.caches.DanCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ExpChange {
    public static void changeExp(Player player, int exp) {
        DanCache cache = Main.getInstance().getCacheHandler().getDan();
        int before = cache.get(player.getName());
        Bukkit.getScheduler().runTask(
                Main.getInstance(),
                () -> {
                    DanExpChangedEvent event = new DanExpChangedEvent(player,
                            before, exp);
                    Bukkit.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        cache.set(player.getName(), exp);
                    }
                });
    }
}
