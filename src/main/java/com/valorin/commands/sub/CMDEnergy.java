package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.caches.EnergyCache;
import com.valorin.commands.SubCommand;
import com.valorin.util.PlayerSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDEnergy extends SubCommand {

    public CMDEnergy() {
        super("energy", "e");
    }

    public void sendHelp(Player p) {
        sm("", p);
        sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：单挑精力值操作", p, false);
        sm("&b/dt energy(e) add <玩家名> <数额> &f- &a为某玩家增加精力值", p, false);
        sm("&b/dt energy(e) set <玩家名> <数额> &f- &a设定某玩家的精力值", p, false);
        sm("", p);
    }

    public boolean isNum(String str) {
        if (str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            if (Double.valueOf(str) < 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.length == 1) {
            sendHelp(p);
            return true;
        }
        EnergyCache cache = Main.getInstance().getCacheHandler().getEnergy();
        if (!cache.isEnable()) {
            sm("&c[x]精力值系统已被禁用！", p);
            return true;
        }
        if (args[1].equalsIgnoreCase("me")) {
            if (!(sender instanceof Player)) {
                sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", p);
                return true;
            }
            BigDecimal bg = new BigDecimal(cache.get(p.getName()));
            double energy = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
                    .doubleValue();
            sm("&6我的精力值 [right] &b{energy}/&3{maxenergy}", p,
                    "energy maxenergy",
                    new String[]{"" + energy, "" + cache.getMaxEnergy()});
            return true;
        }
        if (!sender.hasPermission("dt.admin")) {
            sm("&c[x]无权限！", p);
            return true;
        }
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt e add <玩家名> <数额>", p);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", p);
                return true;
            }
            if (!PlayerSet.get().contains(args[2])) {
                sm("&c[x]该玩家不存在！", p);
                return true;
            }
            cache.set(args[2], cache.get(args[2]) + Double.parseDouble(args[3]));
            sm("&a[v]精力值增添成功", p);
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt e set <玩家名> <数额>", p);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", p);
                return true;
            }
            if (!PlayerSet.get().contains(args[2])) {
                sm("&c[x]该玩家不存在！", p);
                return true;
            }
            cache.set(args[2], Double.parseDouble(args[3]));
            sm("&a[v]精力值设置成功", p);
            return true;
        }
        sendHelp(p);
        return true;
    }
}
