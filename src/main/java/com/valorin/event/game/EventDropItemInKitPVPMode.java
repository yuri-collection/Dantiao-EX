package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class EventDropItemInKitPVPMode implements Listener {
    @EventHandler
    public void onPickUpItem(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {
            Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
            if (Main.getInstance().getCacheHandler()
                    .getArenaInfo().get(arena.getName()).isKitMode()) {
                e.setCancelled(true);
            }
        }
    }
}
