package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventWatcherTeleport implements Listener {
    @EventHandler
    public void onChangeWorldWhileWatchingGame(PlayerTeleportEvent e) {// 观战者在观赛过程中自己打指令(或其他方法)发生了传送
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.getArena(arenaManager.getWatcherOfArena(playerName)) != null) {
            Arena arena = arenaManager.getArena(arenaManager.getWatcherOfArena(playerName));
            if (arena.getEnable()) {
                if (!arena.getWatchersTeleport()) {
                    arena.removeWatcher(playerName);
                    sm("&7由于传送，你退出了观战状态...", player);
                }
            }
        }
    }
}
