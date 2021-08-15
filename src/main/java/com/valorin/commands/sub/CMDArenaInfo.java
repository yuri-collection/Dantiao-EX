package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.arenas.ArenaManager;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.commands.SubCommand;
import com.valorin.data.Data;

public class CMDArenaInfo extends SubCommand {

	public CMDArenaInfo() {
		super("arenainfo", "ainfo", "ai");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		ArenaInfoCache cache = Main.getInstance().getCacheHandler().getArenaInfo();
		if (cache.size() == 0) {
			sm("&c服务器内没有设置任何竞技场！请联系管理员！", player);
			return true;
		}
		sm("&b各竞技场实时信息如下：", player);
		for (String editName : Data.getArenas()) {
			String displayName;
			if (cache.get(editName) != null) {
				displayName = cache.get(editName).getDisplayName().replace("&", "§");
			} else {
				displayName = gm("&7未命名", player);
			}
			String state;
			if (ArenaManager.busyArenasName.contains(editName)) {
				state = gm("&c正在比赛中...", player);
			} else {
				state = gm("&a空闲", player);
			}
			String watch;
			if (cache.get(editName).getWatchingPoint() == null) {
				watch = gm("&c不支持观战", player);
			} else {
				watch = gm("&a可观战", player);
			}
			sm("&f[&r{arenadisplayname}&r&f] &2编号:{arenaeditname} {state} {watch}",
					player, "arenadisplayname arenaeditname state watch",
					new String[] { displayName, editName, state,
							watch }, false);
		}
		if (player != null) {
			sm("&b输入 &e/dt watch <编号> &b即可观战", player);
		}
		return true;
	}
}
