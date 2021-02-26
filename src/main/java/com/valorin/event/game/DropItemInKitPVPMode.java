package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItemInKitPVPMode implements Listener {
    @EventHandler
    public void onPickUpItem(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {
            Arena arena = ah.getArena(ah.getPlayerOfArena(pn));
            if (Main.getInstance().getCacheHandler()
                    .getArenaInfo().get(arena.getName()).isKitMode()) {
                e.setCancelled(true);
            }
        }
    }
}
