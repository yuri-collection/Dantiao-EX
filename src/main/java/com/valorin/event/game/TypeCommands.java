package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class TypeCommands implements Listener {
    @EventHandler
    public void onLevelGameWorld(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {
            Arena arena = ah.getArena(ah.getPlayerOfArena(pn));
            if (!e.getMessage().equals("/dt quit")
                    && !(e.getMessage().equals("/dt q"))) {
                if (!p.isOp()) {
                    e.setCancelled(true);
                    sm("&c[x]比赛时禁用指令！", p);
                }
            } else {
                if (arena.getStage() == 0) {
                    e.setCancelled(true);
                    sm("&c[x]还未正式开赛，请不要立刻认输，请保持信心，不要打退堂鼓！", p);
                }
            }
        }
    }
}
