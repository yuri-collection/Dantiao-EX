package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventInteract implements Listener {
    @EventHandler
    public void onInteractInGameOfStageOfPreparing(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {
            Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
            if (arena.getStage() == 0) {
                e.setCancelled(true);
            }
        }
    }
}
