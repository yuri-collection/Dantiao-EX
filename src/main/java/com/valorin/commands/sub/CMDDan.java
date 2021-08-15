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
        Player player = (Player) sender;
        DanHandler dh = getInstance().getDanHandler();
        DanCache cache = getInstance().getCacheHandler().getDan();
        String danDisplayName;
        if (dh.getPlayerDan(player.getName()) == null) {
            danDisplayName = getInstance().getConfigManager()
                    .getInitialDanName();
        } else {
            danDisplayName = dh.getPlayerDan(player.getName()).getDisplayName();
        }
        sm("", player);
        sm("&b我的段位信息 [right]", player, false);
        sm("", player);
        sm("   &e&l> &r{dan} &e&l<", player, "dan", new String[]{danDisplayName.replace("&", "§")},
                false);
        sm("", player);
        sm("&a[v]现有经验：{exp}", player, "exp",
                new String[]{"" + cache.get(player.getName())}, false);
        sm("&a[v]升级所差：{needexp}", player, "needexp",
                new String[]{"" + dh.getNeedExpToLevelUp(player.getName())},
                false);
        sm("", player);
        return true;
    }
}
