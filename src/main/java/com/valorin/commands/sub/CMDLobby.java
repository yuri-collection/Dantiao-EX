package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.commands.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDLobby extends SubCommand {

    public CMDLobby() {
        super("lobby", "l");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        AreaCache cache = Main.getInstance().getCacheHandler().getArea();
        if (args.length == 1) {
            if (p == null) {
                sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", p);
                return true;
            }
            if (cache.getLobby() == null) {
                sm("&c[x]服务器未设置大厅", p);
                return true;
            }
            Location location = cache.getLobby();
            p.teleport(location);
            sm("&b传送至单挑大厅...", p);
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (!sender.hasPermission("dt.admin")) {
                sm("&c[x]无权限！", p);
                return true;
            }
            if (p == null) {
                sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", p);
                return true;
            }
            Location location = p.getLocation();
            cache.setLobby(location);
            sm("&a[v]单挑大厅设置完毕！玩家每次比赛结束后都会自动传送回到单挑大厅", p);
            return true;
        }
        if (args[1].equalsIgnoreCase("delete")) {
            if (!sender.hasPermission("dt.admin")) {
                sm("&c[x]无权限！", p);
                return true;
            }
            if (cache.getLobby() == null) {
                sm("&c[x]不存在单挑大厅！", p);
            }
            cache.setLobby(null);
            sm("&a[v]单挑大厅删除完毕", p);
            return true;
        }
        sm("", p);
        sm("&b/dt lobby(l) set &f- &a设置服务器的单挑大厅传送点", p, false);
        sm("&b/dt lobby(l) delete &f- &a取消大厅传送点", p, false);
        sm("", p);
        return true;
    }

}
