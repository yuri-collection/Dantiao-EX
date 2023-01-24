package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventProtection implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void routineProtection(EntityDamageByEntityEvent e) { // 玩家：常规保护
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) { // 判断互打双方都是玩家
            Player bearer = (Player) e.getEntity(); // 确定受击者
            Player attacker = (Player) e.getDamager(); // 确定攻击者
            String bearerName = bearer.getName();
            ArenaManager arenaManager = Main.getInstance().getArenaManager();
            if (!arenaManager.isPlayerBusy(bearerName)) { // 如果这个受击者不是正在比赛的玩家，那就看看攻击者是不是比赛中的选手
                if (arenaManager.isPlayerBusy(attacker.getName())) { // 是比赛选手，设置禁止伤害场外玩家
                    e.setCancelled(true);
                }
            } else {
                Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(bearerName)); // 获取竞技场
                String theOther = arena.getTheOtherPlayer(bearerName); // 获取受击者的对手

                if (attacker.getName().equals(theOther)) { // 判定：攻击者就是受击者的对手
                    if (arena.getStage() == 0) {
                        sm("&7战斗未开始...", attacker);
                        e.setCancelled(true);
                    }
                } else { // 攻击者不是自己的对手，是别人的对手
                    sm("&c[x]请勿干扰他人比赛！", attacker);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void projectileProtection(EntityDamageByEntityEvent e) { // 所有实体类型：弹射物保护
        if (!(e.getEntity() instanceof Player)) { // 确认受击者是玩家
            return;
        }
        Player bearer = (Player) e.getEntity(); // 确认受击者
        String bearerName = bearer.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (!Main.getInstance().getConfigManager().isProjectileProtectionEnabled()) {
            if (arenaManager.isPlayerBusy(bearerName)) {
                Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(bearerName));
                if (arena.getStage() == 1) {
                    //如果不开启弹射物保护选项，在正式开赛后直接不处理这个事件
                    return;
                }
            }
        }

        ProjectileSource shooter = null;
        Entity projectileEntity = e.getDamager();
        if (projectileEntity instanceof Arrow) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "Arrow");
        } else if (projectileEntity instanceof FishHook) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "FishHook");
        } else if (projectileEntity instanceof Snowball) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "Snowball");
        } else if (projectileEntity instanceof Fireball) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "Fireball");
        } else if (projectileEntity.toString().equals("CraftSplashPotion")) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "ThrownPotion");
        } else if (projectileEntity.toString().equals("CraftTrident")) {
            shooter = ViaVersion.getProjectileSource(projectileEntity, "Trident");
        }
        if (shooter == null) { // 不是弹射物伤害，就跟这个事件无关了
            return;
        }
        if (!arenaManager.isPlayerBusy(bearerName)) { // 受击者是场外玩家，可能是场内玩家误伤观众
            if (shooter instanceof Player) { // 确定射击者为玩家
                Player shooterPlayer = (Player) shooter;
                String shooterPlayerName = shooterPlayer.getName();
                if (arenaManager.isPlayerBusy(shooterPlayerName)) { //确认射击玩家为场内玩家
                    //取消事件，避免误伤
                    e.setCancelled(true);
                }
            }
        } else { //受击者是场内玩家
            if (!(shooter instanceof Player)) { // 射击者为非玩家实体时，比如说怪物，则取消事件以防止怪物射击场内玩家
                e.setCancelled(true);
            } else { // 射击者为玩家时。接下来要讨论射击者是对手还是观众
                Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(bearerName));
                String theOther = arena.getTheOtherPlayer(bearerName);

                Player shooterPlayer = (Player) shooter;
                if (shooterPlayer.getName().equals(theOther)) { //说明是对手发射
                    if (arena.getStage() == 0) { // 阻止对手在倒计时期间的攻击行为
                        sm("&7战斗未开始...", shooterPlayer);
                        e.setCancelled(true);
                    }
                } else { // 说明是观众发射（其实也有可能是自己）
                    if (!shooterPlayer.getName().equals(bearerName)) { // 进一步确认是观众发射而不是自己再取消事件，自己射中自己也算伤害
                        sm("&c[x]请勿干扰他人比赛！", shooterPlayer);
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    /*
    @EventHandler(priority = EventPriority.HIGHEST)
    public void protection3(EntityDamageByEntityEvent e) { // 非玩家实体：常规保护
        if (!(e.getEntity() instanceof Player)) { // 受击者若不是玩家，return
            return;
        }
        Player bearer = (Player) e.getEntity(); // 确认受击者
        String bearerName = bearer.getName();
        ArenaManager arenaManager = Main.getInstance().getArenaManager();
        if (!Main.getInstance().getConfigManager().isProjectileProtectionEnabled()) {
            if (arenaManager.isPlayerBusy(bearerName)) {
                Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(bearerName));
                if (arena.getStage() == 1) {
                    //如果不开启弹射物保护选项，在正式开赛后直接不处理这个事件
                    return;
                }
            }
        }
        Entity projectileEntity = e.getDamager();
        if (projectileEntity instanceof Player) { // 攻击者是玩家，则不算弹射物实体，不处理事件
            return;
        }
        boolean legal = true;
        if (projectileEntity instanceof Arrow) {
            legal = false;
        } else if (projectileEntity instanceof FishHook) {
            legal = false;
        } else if (projectileEntity instanceof Snowball) {
            legal = false;
        } else if (projectileEntity instanceof Fireball) {
            legal = false;
        } else if (projectileEntity.toString().equals("CraftSplashPotion")) {
            legal = false;
        } else if (projectileEntity.toString().equals("CraftTrident")) {
            legal = false;
        }
        if (!legal) {
            return;
        }
        if (arenaManager.isPlayerBusy(bearerName)) { // 受击者为比赛选手玩家
            e.setCancelled(true);
        }
    }
     */
}