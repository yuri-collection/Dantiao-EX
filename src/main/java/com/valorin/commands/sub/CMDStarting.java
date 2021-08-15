package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.EnergyCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.inventory.special.INVStart;
import com.valorin.timetable.TimeChecker;

public class CMDStarting extends SubCommand implements InServerCommand {

	public CMDStarting() {
		super("start", "st");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (getInstance().getArenaManager().isPlayerBusy(player.getName())) {// 防止玩家用vv进行二次匹配
			return true;
		}
		List<String> blist = Main.getInstance().getCacheHandler().getBlacklist().get();
		if (blist.contains(player.getName())) {
			sm("&c[x]您已被禁赛！", player);
			return true;
		}
		if (!TimeChecker.isInTheTime(player, true)) {
			sm("&c[x]此时间段不开放全服匹配功能，输入/dt timetable查看", player);
			return true;
		}
		if (getInstance().getConfig().getBoolean("WorldLimit.Enable")) {
			List<String> worldlist = getInstance().getConfig().getStringList(
					"WorldLimit.Worlds");
			if (worldlist != null) {
				if (!worldlist.contains(player.getWorld().getName())) {
					sm("&c[x]你所在的世界已被禁止比赛", player);
					return true;
				}
			}
			return true;
		}
		EnergyCache energyCache = Main.getInstance().getCacheHandler().getEnergy();
		if (energyCache.isEnable()) {
			if (energyCache.get(player.getName()) < energyCache.getEnergyNeeded()) {
				sm("&c[x]你的精力值不足！请休息一会", player);
				return true;
			}
		}
		INVStart.openInv(player.getName());
		sm("&a[v]已打开匹配面板..", player);
		return true;
	}

}
