package com.valorin.event;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.valorin.Main;
import com.valorin.network.Update;
import com.valorin.specialtext.ClickableText;

public class EventCheckVersion implements Listener {
    @EventHandler
    public void check(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.isOp()) {
            return;
        }
        if (!Main.getInstance().getConfigManager().isCheckVersion()) {
            return;
        }
        Update update = Main.getInstance().getUpdate();
        new BukkitRunnable() {
            public void run() {
                sendUpdateInfo(update, p);
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 60);
    }

    public static void sendUpdateInfo(Update update, CommandSender sender) {
        if (update.getState() == Update.UpdateState.SUCCESS) {
            int version = update.getVersion();
            int versionNow = Integer.parseInt(Main.getVersion().replace("EX-",""));
            List<String> context = update.getContext();
            if (!update.isNew()) {
                sender.sendMessage("");
                sender.sendMessage("§8§l[§b§lDantiao-EX§8§l]");
                sender.sendMessage("§3单挑插件拓展版检测到有新版本！§7(本段消息仅管理员可见)");
                sender.sendMessage("§f最新版本► §aEX-"+version);
                sender.sendMessage("§f当前版本► §cEX-"+versionNow);
                if (context.size() != 0) {
                    sender.sendMessage("§d更新内容如下：");
                    for (String message : context) {
                        sender.sendMessage(message);
                    }
                }
            } else {
                sender.sendMessage("");
                sender.sendMessage("§8§l[§b§lDantiao-EX§8§l] §7单挑插件拓展版已更新到最新版本：§aEX-"+versionNow+" §8(本条消息仅管理员可见)");
                sender.sendMessage("");
            }
        }
        if (update.getState() == Update.UpdateState.FAILURE_TIMEOUT) {
            sm("&7版本更新内容因为超时而获取失败，建议输入/dt checkv手动获取", null);
        }
        if (update.getState() == Update.UpdateState.FAILURE_OTHER) {
            sm("&7版本更新内容因为某些未知原因（详见后台报错信息）而获取失败，建议输入/dt checkv手动获取", null);
        }
    }
}
