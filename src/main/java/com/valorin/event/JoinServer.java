package com.valorin.event;

import com.valorin.caches.CacheHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static com.valorin.Main.getInstance;

public class JoinServer implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        //载入缓存
        String name = e.getPlayer().getName();
        CacheHandler cacheHandler = getInstance().getCacheHandler();
        cacheHandler.getDan().load(name);
        cacheHandler.getEnergy().load(name);
        cacheHandler.getLanguageFile().load(name);
        cacheHandler.getPoint().load(name);
        cacheHandler.getRecord().load(name, null);
        //首次有玩家进入，初始化全息图
        getInstance().getHD().initial();
    }
}
