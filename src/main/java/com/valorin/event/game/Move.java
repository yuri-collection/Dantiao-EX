package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.util.TeleportUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Move implements Listener {
    @EventHandler
    public void onMoveInStageI(PlayerMoveEvent e) {// 阶段一：禁止移动！
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {
            Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
            if (arena.getStage() == 0) {
                if ((e.getTo().getX() != e.getFrom().getX())
                        || (e.getTo().getY() != e.getFrom().getY())
                        || (e.getTo().getZ() != e.getFrom().getZ())) {
                    TeleportUtil.deal(player, e.getFrom());
                }
            }
        }
    }
}
