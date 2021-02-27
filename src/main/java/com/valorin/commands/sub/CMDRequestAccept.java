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
		Player a = (Player) sender;
		String an = a.getName();
		RequestsHandler rh = getInstance().getRequestsHandler();
		if (rh.getSenders(an).size() == 0) {
			sm("&c[x]你没有任何未处理的请求！", a);
			return true;
		}
		EnergyCache cache = Main.getInstance().getCacheHandler().getEnergy();
		if (cache.isEnable()) {
			if (cache.get(an) < cache.getEnergyNeeded()) {
				sm("&c[x]你的精力值不足！请休息一会", a);
				return true;
			}
		}
		if (args.length == 1) {
			if (rh.getSenders(an).size() == 1) {
				String sn = rh.getSenders(an).get(0);
				Player s = Bukkit.getPlayerExact(sn);
				rh.removeRequest(sn, an);
				sm("&a[v]已接受请求", a);
				sm("&a[v]对方接受了你的请求！", s);
				new StartGame(a, s, null, null, 2);
			} else {
				sm("&6发现有&e多个&6待处理的请求，请选择处理 [right]", a);
				for (String sn : rh.getSenders(an)) {
					BigDecimal bg = new BigDecimal(
							(rh.getTime(sn, an) - System.currentTimeMillis()) / 1000);
					double time = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					a.sendMessage("§b" + sn + " §d" + (0 - time) + gm("秒前", a));
				}
				sm("&6输入 &f/dt accept <玩家名> &6选择处理", a);
			}
			return true;
		} else {
			String sn = args[1];
			Player s = Bukkit.getPlayerExact(sn);
			if (!rh.getSenders(an).contains(sn)) {
				sm("&c[x]不存在的请求", a);
				return true;
			} else {
				sm("&a[v]已接受请求", a);
				sm("&a[v]对方接受了你的请求！", s);
				new StartGame(a, s, rh.getRequest(sn, an).getArenaEditName(), null, 2);
				rh.removeRequest(sn, an);
			}
		}
		return true;
	}

}
