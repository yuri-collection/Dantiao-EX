package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.arenas.FinishGame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventEndGame implements Listener {
    // 一方选手死亡，无论因素，判定“非平局结束比赛”
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeathInGame(PlayerDeathEvent e) {
        Player player = e.getEntity();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {// 属于比赛玩家，对方取胜
            e.setKeepLevel(true);
            e.setKeepInventory(true);
            e.getDrops().clear();
            String arenaName = arenaManager.getPlayerOfArena(playerName);
            Arena arena = arenaManager.getArena(arenaName);
            String winner = arena.getTheOtherPlayer(playerName);
            FinishGame.normalEnd(arenaName, winner, playerName, false);
        }
    }

    // 一方玩家下线，无论因素，判定“非平局结束比赛”
    @EventHandler
    public void onLeaveServerInGame(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {// 属于比赛玩家，对方取胜
            String arenaName = arenaManager.getPlayerOfArena(playerName);
            Arena arena = arenaManager.getArena(arenaName);
            String winner = arena.getTheOtherPlayer(playerName);
            sm("&b对方下线了！系统判定你胜利！", Bukkit.getPlayerExact(winner));
            FinishGame.normalEnd(arenaName, winner, playerName, false);
            EventCompulsoryTeleport.players.put(playerName, arena.getLocation(arena.isp1(playerName)));
        }
    }
}
