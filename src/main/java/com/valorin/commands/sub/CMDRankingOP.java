package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.data.Data;
import com.valorin.ranking.HD;

public class CMDRankingOP extends SubCommand implements AdminCommand {

	public CMDRankingOP() {
		super("hd");
	}

	public void sendHelp(Player p) {
		sm("", p);
		sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：排行榜全息图操作", p, false);
		sm("&b/dt hd win &f- &a创建/移动：全息图-胜场排行榜", p, false);
		sm("&b/dt hd winremove &f- &a删除：全息图-胜场排行榜", p, false);
		sm("&b/dt hd kd &f- &a创建/移动：全息图-KD值排行榜", p, false);
		sm("&b/dt hd kdremove &f- &a删除：全息图-KD值排行榜", p, false);
		sm("&b/dt hd refresh &f- &a强制刷新：所有全息图", p, false);
		sm("", p);
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
		HD hd = getInstance().getHD();
		if (!hd.isEnabled()) {
			sm("&c[x]未发现HD全息插件！无法使用此功能！", p);
			return true;
		}
		AreaCache cache = Main.getInstance().getCacheHandler().getArea();
		if (args[1].equalsIgnoreCase("win")) {
			if (p == null) {
				sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", p);
				return true;
			}
			if (Data.getHologramLocation(0) != null) {
				sm("&b移动全息图...", p);
			} else {
				sm("&b创建全息图...", p);
			}
			Location location = p.getLocation();
			cache.setWinRanking(location);
			hd.load(1);
			return true;
		}
		if (args[1].equalsIgnoreCase("winremove")) {
			if (Data.getHologramLocation(0) == null) {
				sm("&c[x]该全息图本来就不存在", p);
				return true;
			}
			cache.setWinRanking(null);
			hd.unload(1);
			sm("&a[v]全息图删除完毕", p);
			return true;
		}
		if (args[1].equalsIgnoreCase("kd")) {
			if (p == null) {
				sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", p);
				return true;
			}
			if (Data.getHologramLocation(1) != null) {
				sm("&b移动全息图...", p);
			} else {
				sm("&b创建全息图...", p);
			}
			Location location = p.getLocation();
			cache.setKDRanking(location);
			hd.load(2);
			return true;
		}
		if (args[1].equalsIgnoreCase("kdremove")) {
			if (Data.getHologramLocation(1) == null) {
				sm("&c[x]该全息图本来就不存在", p);
				return true;
			}
			cache.setKDRanking(null);
			hd.unload(2);
			return true;
		}
		if (args[1].equalsIgnoreCase("refresh")) {
			if (cache.getWinRankingLocation() == null && cache.getKDRankingLocation() == null) {
				sm("&c[x]无任何全息图！", p);
				return true;
			}
			if (cache.getWinRankingLocation() != null) {
				hd.unload(1);
				hd.load(1);
			}
			if (cache.getKDRankingLocation() != null) {
				hd.unload(2);
				hd.load(2);
			}
			if (p != null) {
				sm("&a[v]全息图刷新完毕！", p);
			}
			return true;
		}
		sendHelp(p);
		return true;
	}

}
