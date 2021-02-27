package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Move implements Listener {
    @EventHandler
    public void onMoveInStageI(PlayerMoveEvent e) {// 阶段一：禁止移动！
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {
            Arena a = ah.getArena(ah.getPlayerOfArena(pn));
            if (a.getStage() == 0) {
                if ((e.getTo().getX() != e.getFrom().getX())
                        || (e.getTo().getY() != e.getFrom().getY())
                        || (e.getTo().getZ() != e.getFrom().getZ())) {
                    p.teleport(e.getFrom());
                }
            }
        }
    }
}
