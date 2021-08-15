package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Interact implements Listener {
    @EventHandler
    public void interact(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {
            Arena arena = ah.getArena(ah.getPlayerOfArena(pn));
            if (arena.getStage() == 0) {
                e.setCancelled(true);
            }
        }
    }
}
