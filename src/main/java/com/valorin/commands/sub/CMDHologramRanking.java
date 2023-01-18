package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import com.valorin.ranking.hologram.HologramManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.AreaCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.data.Data;

public class CMDHologramRanking extends SubCommand implements AdminCommand {

	public CMDHologramRanking() {
		super("hd");
	}

	public void sendHelp(Player player) {
		sm("", player);
		sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：排行榜全息图操作", player, false);
		sm("&b/dt hd win &f- &a创建/移动：全息图-胜场排行榜", player, false);
		sm("&b/dt hd winremove &f- &a删除：全息图-胜场排行榜", player, false);
		sm("&b/dt hd kd &f- &a创建/移动：全息图-KD值排行榜", player, false);
		sm("&b/dt hd kdremove &f- &a删除：全息图-KD值排行榜", player, false);
		sm("&b/dt hd refresh &f- &a强制刷新：所有全息图", player, false);
		sm("", player);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (args.length == 1) {
			sendHelp(player);
			return true;
		}
		HologramManager hologramManager = getInstance().getHologramManager();
		if (!hologramManager.isEnabled()) {
			sm("&c[x]未发现HD全息插件！无法使用此功能！", player);
			return true;
		}
		AreaCache cache = Main.getInstance().getCacheHandler().getArea();
		if (args[1].equalsIgnoreCase("win")) {
			if (player == null) {
				sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", player);
				return true;
			}
			if (Data.getHologramLocation(0) != null) {
				sm("&b移动全息图...", player);
			} else {
				sm("&b创建全息图...", player);
			}
			Location location = player.getLocation();
			cache.setWinRanking(location);
			hologramManager.load(1);
			return true;
		}
		if (args[1].equalsIgnoreCase("winremove")) {
			if (Data.getHologramLocation(0) == null) {
				sm("&c[x]该全息图本来就不存在", player);
				return true;
			}
			cache.setWinRanking(null);
			hologramManager.unload(1);
			sm("&a[v]全息图删除完毕", player);
			return true;
		}
		if (args[1].equalsIgnoreCase("kd")) {
			if (player == null) {
				sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", player);
				return true;
			}
			if (Data.getHologramLocation(1) != null) {
				sm("&b移动全息图...", player);
			} else {
				sm("&b创建全息图...", player);
			}
			Location location = player.getLocation();
			cache.setKDRanking(location);
			hologramManager.load(2);
			return true;
		}
		if (args[1].equalsIgnoreCase("kdremove")) {
			if (Data.getHologramLocation(1) == null) {
				sm("&c[x]该全息图本来就不存在", player);
				return true;
			}
			cache.setKDRanking(null);
			hologramManager.unload(2);
			sm("&a[v]全息图删除完毕", player);
			return true;
		}
		if (args[1].equalsIgnoreCase("refresh")) {
			if (cache.getWinRankingLocation() == null && cache.getKDRankingLocation() == null) {
				sm("&c[x]无任何全息图！", player);
				return true;
			}
			if (cache.getWinRankingLocation() != null) {
				hologramManager.unload(1);
				hologramManager.load(1);
			}
			if (cache.getKDRankingLocation() != null) {
				hologramManager.unload(2);
				hologramManager.load(2);
			}
			if (player != null) {
				sm("&a[v]全息图刷新完毕！", player);
			}
			return true;
		}
		sendHelp(player);
		return true;
	}

}
