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

public class EndGame implements Listener {
    // 一方选手死亡，无论因素，判定“非平局结束比赛”
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDTDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {// 属于比赛玩家，对方取胜
            String an = ah.getPlayerOfArena(pn);
            Arena a = ah.getArena(an);
            String winner = a.getTheOtherPlayer(pn);
            FinishGame.normalEnd(an, winner, pn, false);
        }
    }

    // 一方玩家下线，无论因素，判定“非平局结束比赛”
    @EventHandler
    public void onDTLeaveGame(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {// 属于比赛玩家，对方取胜
            String an = ah.getPlayerOfArena(pn);
            Arena a = ah.getArena(an);
            String winner = a.getTheOtherPlayer(pn);
            sm("&b对方下线了！系统判定你胜利！", Bukkit.getPlayerExact(winner));
            FinishGame.normalEnd(an, winner, pn, false);
            CompulsoryTeleport.players.put(pn, a.getLoaction(a.isp1(pn)));
        }
    }
}
