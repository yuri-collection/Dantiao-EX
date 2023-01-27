package com.valorin.arenas;

import com.valorin.Main;
import com.valorin.api.event.ArenaEventAbs;
import com.valorin.api.event.arena.ArenaDrawFinishEvent;
import com.valorin.api.event.arena.ArenaFinishEvent;
import com.valorin.caches.EnergyCache;
import com.valorin.configuration.ConfigManager;
import com.valorin.data.encapsulation.ArenaInfo;
import com.valorin.effect.WinFirework;
import com.valorin.event.game.EventCompulsoryTeleport;
import com.valorin.task.SettleEnd;
import com.valorin.teleport.ToLobby;
import com.valorin.teleport.ToLogLocation;
import com.valorin.util.Debug;
import com.valorin.util.ItemTaker;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

import static com.valorin.Main.getInstance;
import static com.valorin.arenas.ArenaManager.busyArenasName;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class FinishGame {
    public static void normalEnd(String name, String winnerName, String loserName,
                                 boolean isDraw) {// 正常结束
        Arena arena = getInstance().getArenaManager().getArena(name);
        if (arena == null) {
            return;
        }
        if (!arena.getEnable()) {
            return;
        }

        Player winner = Bukkit.getPlayerExact(winnerName);
        Player loser = Bukkit.getPlayerExact(loserName);

        ArenaInfo arenaInfo = getInstance().getCacheHandler().getArenaInfo().get(name);
        if (arenaInfo.isKitMode()) {
            winner.closeInventory();
            ItemTaker.cleanInventory(winner);
            loser.closeInventory();
            ItemTaker.cleanInventory(loser);

            for (int i = 0; i < arena.getPlayerInventoryItems(arena.isp1(winnerName)).size(); i++) {
                winner.getInventory().setItem(i, arena.getPlayerInventoryItems(arena.isp1(winnerName)).get(i));
            }
            for (int i = 0; i < arena.getPlayerInventoryItems(arena.isp1(loserName)).size(); i++) {
                loser.getInventory().setItem(i, arena.getPlayerInventoryItems(arena.isp1(loserName)).get(i));
            }
            winner.getInventory().setItem(36, arena.getPlayerHelmet(arena.isp1(winnerName)));
            loser.getInventory().setItem(36, arena.getPlayerHelmet(arena.isp1(loserName)));
            winner.getInventory().setItem(37, arena.getPlayerChestPlate(arena.isp1(winnerName)));
            loser.getInventory().setItem(37, arena.getPlayerChestPlate(arena.isp1(loserName)));
            winner.getInventory().setItem(38, arena.getPlayerLeggings(arena.isp1(winnerName)));
            loser.getInventory().setItem(38, arena.getPlayerLeggings(arena.isp1(loserName)));
            winner.getInventory().setItem(39, arena.getPlayerBoots(arena.isp1(winnerName)));
            loser.getInventory().setItem(39, arena.getPlayerBoots(arena.isp1(loserName)));
            if (ViaVersion.isHasOffHandMethod()) {
                ViaVersion.setItemInOffHand(winner, arena.getPlayerOffHandItem(arena.isp1(winnerName)));
                ViaVersion.setItemInOffHand(loser, arena.getPlayerOffHandItem(arena.isp1(loserName)));
            }
        }

        ConfigManager configManager = getInstance().getConfigManager();
        ConfigManager.AutoRespawnWay autoRespawnWay = configManager.getAutoRespawnWay();
        if (winner != null) {
            try {
                if (winner.isDead()) {
                    if (configManager.isAutoRespawnEnable()) {
                        if (autoRespawnWay.equals(ConfigManager.AutoRespawnWay.SPIGOT)) {
                            winner.spigot().respawn();
                        }
                        if (autoRespawnWay.equals(ConfigManager.AutoRespawnWay.SETHEALTH)) {
                            winner.setHealth(winner.getMaxHealth());
                            EventCompulsoryTeleport.players.put(winnerName,
                                    arena.getLocation(arena.isp1(winnerName)));
                            EventCompulsoryTeleport.back(winner);
                        }
                    } else {
                        EventCompulsoryTeleport.players.put(winnerName,
                                arena.getLocation(arena.isp1(winnerName)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                EventCompulsoryTeleport.players.put(winnerName,
                        arena.getLocation(arena.isp1(winnerName)));
            }
        }
        if (loser != null) {

            Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                    loser.getWorld().strikeLightningEffect(loser.getLocation()));

            try {
                if (loser.isDead()) {
                    if (configManager.isAutoRespawnEnable()) {
                        if (autoRespawnWay.equals(ConfigManager.AutoRespawnWay.SPIGOT)) {
                            loser.spigot().respawn();
                        }
                        if (autoRespawnWay.equals(ConfigManager.AutoRespawnWay.SETHEALTH)) {
                            loser.setHealth(loser.getMaxHealth());
                            EventCompulsoryTeleport.players.put(loserName,
                                    arena.getLocation(arena.isp1(winnerName)));
                            EventCompulsoryTeleport.back(loser);
                        }
                    } else {
                        EventCompulsoryTeleport.players.put(loserName,
                                arena.getLocation(arena.isp1(winnerName)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                EventCompulsoryTeleport.players.put(loserName,
                        arena.getLocation(arena.isp1(loserName)));
            }
        }
        try {
            winner.setHealth(winner.getMaxHealth());
            winner.setFoodLevel(20);
            if (loser != null && !loser.isDead()) {
                loser.setHealth(loser.getMaxHealth());
                loser.setFoodLevel(20);
            }
            ViaVersion.sendTitle(
                    winner,
                    gm("&a&l胜利", winner),
                    gm("&f你以 &f&l{time} &f秒击败了 &f&l{player}", winner, "time player",
                            new String[]{arena.getTime() + "", loserName}), 20, 60,
                    0);

            EnergyCache energyCache = getInstance().getCacheHandler()
                    .getEnergy();
            if (energyCache.isEnable()) {
                double energyNeeded = energyCache.getEnergyNeeded();
                energyCache.set(winnerName, energyCache.get(winnerName) - energyNeeded);
                energyCache.set(loserName, energyCache.get(loserName) - energyNeeded);
            }

            arena.setCanTeleport(true);
            arena.setWatchersTeleport(true);
            List<String> watchers = arena.getWatchers();
            Location lobbyLocation = getInstance().getCacheHandler().getArea()
                    .getLobby();
            if (lobbyLocation != null) {
                ToLobby.to(winner, false);
                ToLobby.to(loser, true);
                sm("&b已将你带回单挑大厅！", winner);
                if (loser != null) {
                    sm("&b已将你带回单挑大厅！", loser);
                }
                for (String watcher : watchers) {
                    if (Bukkit.getPlayerExact(watcher) != null) {
                        ToLobby.to(Bukkit.getPlayerExact(watcher), true);
                        sm("&b已将你带回单挑大厅！", loser);
                    }
                }
            } else {
                Location winnerLocation = arena.getLocation(arena.isp1(winnerName));
                Location loserLocation = arena.getLocation(arena.isp1(loserName));
                ToLogLocation.to(winner, loser, winnerLocation, loserLocation, false);
                for (String watcher : watchers) {
                    if (Bukkit.getPlayerExact(watcher) != null) {
                        sm("&b[报告] &7你所观战的竞技场上的比赛已结束，请自行传送回家...",
                                Bukkit.getPlayerExact(watcher), false);
                    }
                }
            }// 回到原处
            boolean isFirework = false;
            int startWay = arena.getStartWay();
            if (startWay == 1) {
                isFirework = configManager.isFireworkAwardedByPanel();
            }
            if (startWay == 2) {
                isFirework = configManager.isFireworkAwardedByInviting();
            }
            if (startWay == 3) {
                isFirework = configManager.isFireworkAwardedByCompulsion();
            }
            if (isFirework) {
                WinFirework.setFirework(winner.getLocation());
                sm("&a[v]WOW！服务器专门为你的获胜放了一朵烟花~", winner);
            }

            busyArenasName.remove(arena.getName());
            getInstance().getSingleLineChartData().addGameTimes();

            ArenaEventAbs event;
            if (isDraw) {
                event = new ArenaDrawFinishEvent(winner, loser, arena);
            } else {
                event = new ArenaFinishEvent(winner, loser, arena);
            }
            Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                    Bukkit.getServer().getPluginManager().callEvent(event)
            );

            Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
                try {
                    SettleEnd.settle(arena, winner, loser, isDraw);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                arena.finish();
            });
        } catch (Exception exception) {
            exception.printStackTrace();
            busyArenasName.remove(arena.getName());
            arena.finish();
            Debug.send("§c比赛结束时出现异常！请将报错信息截图反馈给本插件作者",
                    "Some errors occurred when the game finished!");
        }
    }

    public static void compulsoryEnd(String name, Player finisher, CompulsoryEndCause cause) {// 强制结束，不予记录
        Arena arena = getInstance().getArenaManager().getArena(name);
        if (arena == null) {
            sm("&c[x]不存在的竞技场，请检查输入", finisher);
            return;
        }
        if (!arena.getEnable()) {
            sm("&c[x]这个竞技场还没有比赛呢！", finisher);
            return;
        }

        Player player1 = Bukkit.getPlayerExact(arena.getp1());
        String playerName1 = player1.getName();
        Player player2 = Bukkit.getPlayerExact(arena.getp2());
        String playerName2 = player2.getName();

        ArenaInfo arenaInfo = getInstance().getCacheHandler().getArenaInfo().get(name);
        if (arenaInfo.isKitMode()) {
            ItemTaker.cleanInventory(player1);
            ItemTaker.cleanInventory(player2);

            for (int i = 0; i < arena.getPlayerInventoryItems(arena.isp1(playerName1)).size(); i++) {
                player1.getInventory().setItem(i, arena.getPlayerInventoryItems(arena.isp1(playerName1)).get(i));
            }
            for (int i = 0; i < arena.getPlayerInventoryItems(arena.isp1(playerName2)).size(); i++) {
                player2.getInventory().setItem(i, arena.getPlayerInventoryItems(arena.isp1(playerName2)).get(i));
            }
            player1.getInventory().setItem(36, arena.getPlayerHelmet(arena.isp1(playerName1)));
            player2.getInventory().setItem(36, arena.getPlayerHelmet(arena.isp1(playerName2)));
            player1.getInventory().setItem(37, arena.getPlayerChestPlate(arena.isp1(playerName1)));
            player2.getInventory().setItem(37, arena.getPlayerChestPlate(arena.isp1(playerName2)));
            player1.getInventory().setItem(38, arena.getPlayerLeggings(arena.isp1(playerName1)));
            player2.getInventory().setItem(38, arena.getPlayerLeggings(arena.isp1(playerName2)));
            player1.getInventory().setItem(39, arena.getPlayerBoots(arena.isp1(playerName1)));
            player2.getInventory().setItem(39, arena.getPlayerBoots(arena.isp1(playerName2)));
            if (ViaVersion.isHasOffHandMethod()) {
                ViaVersion.setItemInOffHand(player1, arena.getPlayerOffHandItem(arena.isp1(playerName1)));
                ViaVersion.setItemInOffHand(player2, arena.getPlayerOffHandItem(arena.isp1(playerName2)));
            }
        }

        arena.setWatchersTeleport(true);
        List<String> watchers = arena.getWatchers();
        Location lobbyLocation = getInstance().getCacheHandler().getArea()
                .getLobby();
        if (lobbyLocation != null) {
            ToLobby.to(player1, true);
            sm("&b已将你带回单挑大厅！", player1);
            ToLobby.to(player2, true);
            sm("&b已将你带回单挑大厅！", player2);
            for (String watcher : watchers) {
                if (Bukkit.getPlayerExact(watcher) != null) {
                    ToLobby.to(Bukkit.getPlayerExact(watcher), true);
                    sm("&b已将你带回单挑大厅！", player2);
                }
            }
        } else {
            Location winnerLocation = arena
                    .getLocation(arena.isp1(player1.getName()));
            Location loserLocation = arena
                    .getLocation(arena.isp1(player2.getName()));
            ToLogLocation.to(player1, player2, winnerLocation, loserLocation, true);
            for (String watcher : watchers) {
                if (Bukkit.getPlayerExact(watcher) != null) {
                    sm("&b[报告] &7你所观战的竞技场上的比赛已结束，请自行传送回家...",
                            Bukkit.getPlayerExact(watcher));
                }
            }
        }// 回到原处

        arena.finish();
        busyArenasName.remove(arena.getName());

        switch (cause) {
            case COMMAND_STOP:
                sm("&a[v]已强制停止", finisher);
                sm("&b&l比赛被管理员强制结束！本局比赛不会被记录！", player1, player2);
                break;
            case COMMAND_STOP_ALL:
                sm("&b&l比赛被管理员强制结束！本局比赛不会被记录！", player1, player2);
                break;
            case RELOAD_CONFIG:
                sm("&a由于重载，强制结束了竞技场 &2{editname} &a上正在进行的比赛", finisher, "editname", new String[]{arena.getName()});
                sm("&b&l比赛因为插件配置文件的重载而强制结束！本局比赛不会被记录！", player1, player2);
                break;
            case RELOAD_PLUGIN:
                sm("&a由于插件停止运行，强制结束了竞技场 &2{editname} &a上正在进行的比赛", finisher, "editname", new String[]{arena.getName()});
                sm("&b&l比赛因为插件停止运行而强制结束！本局比赛不会被记录！", player1, player2);
        }
    }

    public enum CompulsoryEndCause {
        COMMAND_STOP,
        COMMAND_STOP_ALL,
        RELOAD_CONFIG,
        RELOAD_PLUGIN
    }
}
