package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.util.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventTeleport implements Listener {
    /*
    @EventHandler
    public void onLeaveGameWorld(PlayerTeleportEvent e) {// 突然传送到别的世界去了
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (playerName == null) {
            return;
        }
        if (arenaManager.isPlayerBusy(playerName)) {
            Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
            if (!arena.canTeleport()) {
                if (!e.getTo().getWorld().equals(player.getLocation().getWorld())) {
                    e.setCancelled(true);
                    sm("&c[x]发生非法传送，已制止", player);
                }
            }
        }
    }
     */

    @EventHandler
    public void onTpToGamer(PlayerTeleportEvent e) {// 场外玩家企图传送到场内玩家身边给TA武器什么的
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (playerName == null) {
            return;
        }
        if (player.isOp()) {
            return;
        }
        Location to = e.getTo();
        for (String arenaEditName : ArenaManager.busyArenasName) {
            Arena arena = arenaManager.getArena(arenaEditName);
            if (arena.getp1() != null && arena.getp2() != null) {
                if (player.getName().equals(arena.getp1()) || player.getName().equals(arena.getp2())) {
                    //如果传送者是场内玩家，则不处理
                    return;
                }
                Location player1Location = Bukkit.getPlayerExact(arena.getp1())
                        .getLocation();
                Location player2Location = Bukkit.getPlayerExact(arena.getp2())
                        .getLocation();
                String targetPlayerName = null;
                if ((Math.abs(player1Location.getBlockX() - to.getBlockX()) <= 2
                        && Math.abs(player1Location.getBlockY() - to.getBlockY()) <= 2 && Math
                        .abs(player1Location.getBlockZ() - to.getBlockZ()) <= 2)) {
                    targetPlayerName = Bukkit.getPlayerExact(arena.getp1()).getName();
                }
                if (Math.abs(player2Location.getBlockX() - to.getBlockX()) <= 2
                        && Math.abs(player2Location.getBlockY()
                        - to.getBlockY()) <= 2 && Math
                        .abs(player2Location.getBlockZ() - to.getBlockZ()) <= 2) {
                    targetPlayerName = Bukkit.getPlayerExact(arena.getp2()).getName();
                }
                if (targetPlayerName != null) {
                    //当传送地点与任意一方玩家很靠近时，则判定为传送到场内玩家身边
                    e.setCancelled(true);
                    sm("&c[x]玩家{player}正在单挑竞技场比赛中，现在不能传送到TA身旁", player, "player", new String[]{targetPlayerName});
                }
            }
        }
    }

    @EventHandler
    public void onGamerTpViaOtherPlugin(PlayerTeleportEvent e) {// 场内玩家企图借助其他插件的传送功能传送到别处
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (playerName == null) {
            return;
        }
        if (arenaManager.isPlayerBusy(playerName)) {
            if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.PLUGIN)) {
                if (!TeleportUtil.legalTeleportPlayer.contains(playerName)) {
                    if (!Main.getInstance().getConfigManager().isTeleportationTriggeredByOtherPluginAllowed()) {
                        sm("&c[x]发生非法传送，已制止", player);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void useEnderPearl(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(playerName)) {
            Arena arena = ah.getArena(ah.getPlayerOfArena(playerName));
            if (arena.getStage() == 0) {
                if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
                    e.setCancelled(true);
                    sm("&c[x]还未正式开赛，请不要使用末影珍珠！", player);
                }
            }
        }
    }
}
