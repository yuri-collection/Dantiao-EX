package com.valorin.commands.sub;

import com.valorin.caches.DanCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.dan.DanHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDDan extends SubCommand implements InServerCommand {

    public CMDDan() {
        super("dan");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        DanHandler dh = getInstance().getDanHandler();
        DanCache cache = getInstance().getCacheHandler().getDan();
        String danDisplayName;
        if (dh.getPlayerDan(p.getName()) == null) {
            danDisplayName = getInstance().getConfigManager()
                    .getInitialDanName();
        } else {
            danDisplayName = dh.getPlayerDan(p.getName()).getDisplayName();
        }
        sm("", p);
        sm("&b我的段位信息 [right]", p, false);
        sm("", p);
        sm("   &e&l> &r{dan} &e&l<", p, "dan", new String[]{danDisplayName.replace("&", "§")},
                false);
        sm("", p);
        sm("&a[v]现有经验：{exp}", p, "exp",
                new String[]{"" + cache.get(p.getName())}, false);
        sm("&a[v]升级所差：{needexp}", p, "needexp",
                new String[]{"" + dh.getNeedExpToLevelUp(p.getName())},
                false);
        sm("", p);
        return true;
    }
}
