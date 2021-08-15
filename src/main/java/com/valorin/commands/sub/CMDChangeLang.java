package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.caches.LanguageFileCache;
import com.valorin.commands.SubCommand;
import com.valorin.configuration.languagefile.LanguageFileLoader;

public class CMDChangeLang extends SubCommand {

    public CMDChangeLang() {
        super("changelang", "changelanguage", "language", "lang", "cl");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length == 1) {
            LanguageFileLoader lfl = getInstance().getLanguageFileLoader();
            List<File> fileList = lfl.getLanguagesList();
            if (fileList.size() == 0) {
                sm("&c[x]服务器不存在任何语言文件，如您有特殊需求请联系腐竹", player);
            } else {
                sm("&6语言文件如下 [right]", player);
                for (File file : fileList) {
                    sender.sendMessage("§e" + file.getName().replace(".txt", "")
                            + "§7(" + gm("简体中文", file, 233) + "§7)");
                }
                sm("&6共计 &f&l{amount} &6个", player, "amount", new String[]{""
                        + fileList.size()});
            }
            return true;
        }
        if (args.length == 2) {
            if (player == null) {
                sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", player);
                return true;
            }
            LanguageFileLoader lfl = getInstance().getLanguageFileLoader();
            List<File> fileList = lfl.getLanguagesList();
            if (fileList.size() != 0) {
                for (File file : fileList) {
                    if (args[1].equalsIgnoreCase(file.getName()
                            .replace(".txt", ""))) {
                        LanguageFileCache cache = getInstance()
                                .getCacheHandler().getLanguageFile();
                        cache.set(player.getName(), args[1]);
                        sm("&a[v]语言环境切换已完成", player);
                        return true;
                    }
                }
            }
            sm("&c[x]语言文件&e{name}&c不存在", player, "name", new String[]{""
                    + args[1]});
        }
        return true;
    }

}
