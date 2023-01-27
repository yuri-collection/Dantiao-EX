package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.arenas.ArenaManager;
import com.valorin.arenas.FinishGame;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.data.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDStopAll extends SubCommand implements AdminCommand {

    public CMDStopAll() {
        super("stopall");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = sender instanceof Player ? (Player) sender : null;
        ArenaInfoCache cache = Main.getInstance().getCacheHandler().getArenaInfo();
        if (cache.size() == 0) {
            sm("&c服务器内没有设置任何竞技场！请联系管理员！", player);
            return true;
        }
        sm("&3正在尝试停止所有竞技场的比赛...", player, false);
        int number = 1, stop = 0;
        for (String editName : Data.getArenas()) {
            if (ArenaManager.busyArenasName.contains(editName)) {
                FinishGame.compulsoryEnd(editName, player, FinishGame.CompulsoryEndCause.COMMAND_STOP_ALL);
                sm("&7{number}.竞技场{editname}:&a已停止", player, "number editname", new String[]{"" + number, editName}, false);
                stop++;
            } else {
                sm("&7{number}.竞技场{editname}:没有进行中的比赛", player, "number editname", new String[]{"" + number, editName}, false);
            }
            number++;
        }
        number--;
        sm("&b已对全部 &e{total} &b个竞技场进行关闭操作，其中 &6{success} &b个竞技场的比赛已终止", player, "total success", new String[]{"" + number, "" + stop}, false);
        return true;
    }
}
