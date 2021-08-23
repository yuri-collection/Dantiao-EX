package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.arenas.StartGame;
import com.valorin.caches.EnergyCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.request.RequestsHandler;

public class CMDRequestAccept extends SubCommand implements InServerCommand {

	public CMDRequestAccept() {
		super("accept");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player playerAccepting = (Player) sender;
		String playerAcceptingName = playerAccepting.getName();
		RequestsHandler rh = getInstance().getRequestsHandler();
		if (rh.getSenders(playerAcceptingName).size() == 0) {
			sm("&c[x]你没有任何未处理的请求！", playerAccepting);
			return true;
		}
		EnergyCache cache = Main.getInstance().getCacheHandler().getEnergy();
		if (cache.isEnable()) {
			if (cache.get(playerAcceptingName) < cache.getEnergyNeeded()) {
				sm("&c[x]你的精力值不足！请休息一会", playerAccepting);
				return true;
			}
		}
		if (args.length == 1) {
			if (rh.getSenders(playerAcceptingName).size() == 1) {
				String sn = rh.getSenders(playerAcceptingName).get(0);
				Player s = Bukkit.getPlayerExact(sn);
				rh.removeRequest(sn, playerAcceptingName);
				sm("&a[v]已接受请求", playerAccepting);
				sm("&a[v]对方接受了你的请求！", s);
				StartGame.start(playerAccepting, s, null, null, 2);
			} else {
				sm("&6发现有&e多个&6待处理的请求，请选择处理 [right]", playerAccepting);
				for (String sn : rh.getSenders(playerAcceptingName)) {
					BigDecimal bg = new BigDecimal(
							(rh.getTime(sn, playerAcceptingName) - System.currentTimeMillis()) / 1000);
					double time = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					playerAccepting.sendMessage("§b" + sn + " §d" + (0 - time) + gm("秒前", playerAccepting));
				}
				sm("&6输入 &f/dt accept <玩家名> &6选择处理", playerAccepting);
			}
			return true;
		} else {
			String sn = args[1];
			Player s = Bukkit.getPlayerExact(sn);
			if (!rh.getSenders(playerAcceptingName).contains(sn)) {
				sm("&c[x]不存在的请求", playerAccepting);
				return true;
			} else {
				sm("&a[v]已接受请求", playerAccepting);
				sm("&a[v]对方接受了你的请求！", s);
				StartGame.start(playerAccepting, s, rh.getRequest(sn, playerAcceptingName).getArenaEditName(), null, 2);
				rh.removeRequest(sn, playerAcceptingName);
			}
		}
		return true;
	}

}
