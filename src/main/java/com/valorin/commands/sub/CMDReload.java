package com.valorin.commands.sub;

import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDReload extends SubCommand implements AdminCommand {

    public CMDReload() {
        super("reload");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p;
        if (sender instanceof Player) {
            p = (Player) sender;
        } else {
            p = null;
        }
        if (args.length == 1) {
            sm("&a输入 &b/dt reload c &a重载配置config.yml", p);
            sm("&a输入 &b/dt reload l &a重载所有语言文件", p);
            return true;
        }
        if (p != null) {
            if (getInstance().getArenaManager().isPlayerBusy(p.getName())) {// OP比赛时输入
                return true;
            }
        }
        if (args[1].equalsIgnoreCase("c")) {
            try {
                long start = System.currentTimeMillis();
                getInstance().getConfigManager().reload();
                getInstance().getMySQL().close();
                getInstance().getMySQL().connect();
                getInstance().getCacheHandler().unload();
                getInstance().getCacheHandler().load(
                        () -> {
                            getInstance().getHD().reload();
                            getInstance().getDanHandler().loadCustomDanFromConfig();
                            getInstance().reloadTimeTable();
                            long end = System.currentTimeMillis();
                            sm("&a[v]config.yml:重载完毕！耗时&d{ms}毫秒", p, "ms",
                                    new String[]{"" + (end - start)});
                        });
            } catch (Exception e) {
                sm("&c[x]config.yml:重载时发生异常！", p);
                e.printStackTrace();
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("l")) {
            try {
                long start = System.currentTimeMillis();
                getInstance().reloadLanguageFileLoad();
                long end = System.currentTimeMillis();
                sm("&a[v]Language file:重载完毕！耗时&d{ms}毫秒", p, "ms",
                        new String[]{"" + (end - start)});
            } catch (Exception e) {
                sm("&c[x]Language file:重载时发生异常！建议重启本插件(若服务器装有具有重载其他插件功能的插件)或重启服务器",
                        p);
                e.printStackTrace();
            }
            return true;
        }
        return true;
    }
}
