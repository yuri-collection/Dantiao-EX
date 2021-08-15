package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.commands.SubCommand;
import com.valorin.request.RequestsHandler;
import com.valorin.commands.way.InServerCommand;

public class CMDRequestDeny extends SubCommand implements InServerCommand {

	public CMDRequestDeny() {
		super("deny");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player playerDenying = (Player) sender;
		String playerDenyingName = playerDenying.getName();
		RequestsHandler rh = getInstance().getRequestsHandler();
		if (rh.getSenders(playerDenyingName).size() == 0) {
			sm("&c[x]你没有任何未处理的请求！", playerDenying);
			return true;
		}
		if (args.length == 1) {
			if (rh.getSenders(playerDenyingName).size() == 1) {
				String sn = rh.getSenders(playerDenyingName).get(0);
				Player s = Bukkit.getPlayerExact(sn);
				rh.removeRequest(sn, playerDenyingName);
				sm("&a[v]已拒绝请求", playerDenying);
				sm("&c[x]对方拒绝了你的请求", s);
			} else {
				sm("&6发现有&e多个&6待处理的请求，请选择处理 [right]", playerDenying);
				for (String sn : rh.getSenders(playerDenyingName)) {
					BigDecimal bg = new BigDecimal(
							(rh.getTime(sn, playerDenyingName) - System.currentTimeMillis()) / 1000);
					double time = bg.setScale(1, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					playerDenying.sendMessage("§b" + sn + " §7(" + time + gm("秒前", playerDenying) + ")");
				}
				sm("&6输入 &f/dt deny <玩家名> &6选择处理", playerDenying);
			}
			return true;
		} else {
			String sn = args[1];
			Player s = Bukkit.getPlayerExact(sn);
			if (!rh.getSenders(playerDenyingName).contains(sn)) {
				sm("&c[x]不存在的请求", playerDenying);
				return true;
			} else {
				rh.removeRequest(sn, playerDenyingName);
				sm("&a[v]已拒绝请求", playerDenying);
				sm("&c[x]对方拒绝了你的请求", s);
			}
		}
		return true;
	}

}
