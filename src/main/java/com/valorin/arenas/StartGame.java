package com.valorin.arenas;

import com.valorin.api.event.arena.ArenaStartEvent;
import com.valorin.configuration.ConfigManager;
import com.valorin.dan.DanHandler;
import com.valorin.data.Data;
import com.valorin.request.RequestsHandler;
import com.valorin.util.ItemChecker;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

import static com.valorin.Main.getInstance;
import static com.valorin.arenas.ArenaManager.arenas;
import static com.valorin.arenas.ArenaManager.busyArenasName;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class StartGame {

    /*
     * player1：选手1号 player2：选手2号 arenaName：指定竞技场，null则为随机 starter：强制开启比赛的管理员，null则为正常情况的开赛
     */
    public static void start(Player player1, Player player2, String editName, Player starter, int startWay) {
        if (player1 == null || player2 == null) {// 玩家遁地了？！
            sm("&c[x]警告：开赛时发生异常，不予开赛！", starter);
            return;
        }
        if (Data.getArenas().size() == 0) {// 居然不设置竞技场？！
            sm("&c服务器内没有设置任何竞技场！请联系管理员！", player1, player2);
            return;
        }
        ConfigManager configManager = getInstance().getConfigManager();
        if (configManager.isWorldWhitelistEnabled()) {
            List<String> worldList = configManager.getWorldWhitelist();
            if (worldList != null) {
                if (!worldList.contains(player1.getWorld().getName())) {
                    sm("&c[x]你所在的世界已被禁止比赛，请移动到允许比赛的世界再开赛", player1);
                    sm("&c[x]对手{player}所在的世界已被禁止比赛，请等待TA移动到允许比赛的世界再开赛", player2,
                            "player", new String[]{player1.getName()});
                    return;
                }
                if (!worldList.contains(player2.getWorld().getName())) {
                    sm("&c[x]你所在的世界已被禁止比赛，请移动到允许比赛的世界再开赛", player2);
                    sm("&c[x]对手{player}所在的世界已被禁止比赛，请等待TA移动到允许比赛的世界再开赛", player1,
                            "player", new String[]{player2.getName()});
                    return;
                }
            }
        }

        List<String> materialNameLimitedList = configManager.getItemLimitByMaterial();
        List<String> loreLimitedList = configManager.getItemLimitByLore();
        if (ItemChecker.check(player1, materialNameLimitedList, loreLimitedList)) {
            sm("&c[x]你的背包里携带有违禁品！不予开赛", player1);
            sm("&c[x]对手{player}的背包里携带有违禁品！不予开赛", player2, "player",
                    new String[]{player1.getName()});
            player1.closeInventory();
            player2.closeInventory();
            return;
        }
        if (ItemChecker.check(player2, materialNameLimitedList, loreLimitedList)) {
            sm("&c[x]你的背包里携带有违禁品！不予开赛", player2);
            sm("&c[x]对手{player}的背包里携带有违禁品！不予开赛", player1, "player",
                    new String[]{player2.getName()});
            player1.closeInventory();
            player2.closeInventory();
            return;
        }

        if (editName == null) { // 非指定竞技场，即随机
            if (busyArenasName.size() == arenas.size()) {// 竞技场都满了
                player1.closeInventory();
                player2.closeInventory();
                sm("&c所有竞技场都满了！请稍后再试~&e(小提示：输入/dt ainfo可以查看所有竞技场的实时信息)", player1, player2);
                return;
            }
        } else {
            if (!arenas.contains(getInstance().getArenaManager().getArena(
                    editName))) {
                sm("&c不存在这个竞技场，请检查输入", starter);
                return;
            }
            if (busyArenasName.contains(editName)) {// 竞技场满了
                sm("&c这个竞技场正在比赛中！请换一个试试", starter);
                return;
            }
        }
        Arena arena;
        if (editName == null) {
            arena = getRandomArena();
        }// 若没有玩家指定的竞技场，则随机获取一个竞技场
        else {
            arena = getInstance().getArenaManager().getArena(editName);
        }// 获取OP强制开始时指定的竞技场
        if (arena == null) {
            sm("&c[x]警告：开赛时发生异常，不予开赛！", player1, player2);
            player1.closeInventory();
            player2.closeInventory();
            return;
        }
        String pn1 = player1.getName(), pn2 = player2.getName();

        ArenaStartEvent event = new ArenaStartEvent(player1, player2, arena);

        editName = arena.getName();

        arena.setLocation(player1, player2);

        DanHandler dh = getInstance().getDanHandler();
        arena.setDan(true, dh.getPlayerDan(pn1));
        arena.setDan(false, dh.getPlayerDan(pn2));

        player1.teleport(Data.getArenaPointA(editName));
        player2.teleport(Data.getArenaPointB(editName));
        if (player1.isFlying()) {
            if (!player1.isOp()) {
                player1.setFlying(false);
            }
        }
        if (player2.isFlying()) {
            if (!player2.isOp()) {
                player2.setFlying(false);
            }
        }
        player1.setHealth(player1.getMaxHealth());
        player2.setHealth(player2.getMaxHealth());
        player1.setFoodLevel(20);
        player2.setFoodLevel(20);
        if (configManager.isChangeGameModeWhenGameStarts()) {
            player1.setGameMode(GameMode.SURVIVAL);
            player2.setGameMode(GameMode.SURVIVAL);
        }

        arena.start(pn1, pn2, startWay);
        busyArenasName.add(editName);

        String arenaDisplayName = getInstance().getCacheHandler()
                .getArenaInfo().get(editName).getDisplayName();
        if (arenaDisplayName == null) {
            arenaDisplayName = "";
        }
        sm("&b您已进入竞技场&r{arenaname}", player1, "arenaname",
                new String[]{arenaDisplayName});
        sm("&b您已进入竞技场&r{arenaname}", player2, "arenaname",
                new String[]{arenaDisplayName});

        RequestsHandler rh = getInstance().getRequestsHandler();
        rh.clearRequests(pn1, 0, pn2);
        rh.clearRequests(pn2, 0, pn1);

        Bukkit.getServer().getPluginManager().callEvent(event);

        if (starter != null) {
            sm("&a[v]已强制开始", starter);
        }
    }

    private static Arena getRandomArena() {
        if (Data.getArenas().size() == 0) {
            return null;
        }
        List<String> freeArenasName = Data.getArenas();
        freeArenasName.removeAll(busyArenasName);
        if (freeArenasName.size() == 0) {
            return null;
        }
        Random random = new Random();
        return getInstance().getArenaManager().getArena(
                freeArenasName.get(random.nextInt(freeArenasName.size())));
    }
}
