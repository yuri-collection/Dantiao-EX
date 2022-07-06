package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventTypeCommands implements Listener {
    @EventHandler
    public void onTypeCommand(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (arenaManager.isPlayerBusy(playerName)) {
            String commandTyped = e.getMessage();
            boolean isQuitCommand = false;
            if (!commandTyped.equals("/dantiao quit") && !commandTyped.equals("dantiao q")) {
                List<String> commandAliases = Main.getInstance().getCommand("dantiao").getAliases();
                for (String commandAlias : commandAliases) {
                    if (commandTyped.equals("/" + commandAlias + " quit") || commandTyped.equals("/" + commandAlias + " q")) {
                        isQuitCommand = true;
                        break;
                    }
                }
            } else {
                isQuitCommand = true;
            }
            if (!isQuitCommand) {
                if (!player.isOp()) {
                    List<String> commandWhitelist = Main.getInstance().getConfigManager().getCommandWhitelist();
                    boolean isCommandInWhitelist = false;
                    for (String commandInWhitelist : commandWhitelist) {
                        if (commandTyped.contains(commandInWhitelist)) {
                            isCommandInWhitelist = true;
                            break;
                        }
                    }
                    if (!isCommandInWhitelist) {
                        e.setCancelled(true);
                        sm("&c[x]比赛时禁用这条指令！", player);
                    }
                }
            } else {
                Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
                if (arena.getStage() == 0) {
                    e.setCancelled(true);
                    sm("&c[x]还未正式开赛，请不要立刻认输，请保持信心，不要打退堂鼓！", player);
                }
            }
        }
    }
}
