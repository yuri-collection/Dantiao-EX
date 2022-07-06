package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.math.BigDecimal;
import java.util.List;

import com.valorin.ranking.RankingData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.caches.RankingCache;
import com.valorin.commands.SubCommand;

public class CMDRanking extends SubCommand {

	public CMDRanking() {
		super("rank");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		RankingData r = getInstance().getRanking();
		RankingCache cache = getInstance().getCacheHandler().getRanking();
		if (args.length == 1) {
			if (player == null) {
				sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", null);
				return true;
			}
			String playerName = player.getName();
			sm("&6单挑排行榜信息 [right]", player, false);
			sm("&b胜场数排名：第&d{ranking}&b名", player, "ranking",
					new String[] { "" + r.getWinRank(playerName) }, false);
			sm("&bKD比值排名：第&d{ranking}&b名", player, "ranking",
					new String[] { "" + r.getKDRank(playerName) }, false);
			sm("&e查看其他人的？输入 &f/dt rank win或kd &e查看全服排名", player, false);
			return true;
		} else {
			if (args[1].equalsIgnoreCase("win")) {
				sm("&b[star1]单挑-胜场排行榜[star2]", player);
				if (cache.getWin().size() != 0) {
					List<String> winList = cache.getWin();
					int max;
					max = Math.min(winList.size(), 10);
					for (int i = 0; i < max; i++) {
						String color = "§b";
						if (i == 0) {
							color = "§e§l";
						}
						if (i == 1) {
							color = "§6§l";
						}
						if (i == 2) {
							color = "§b§l";
						}
						player.sendMessage(color + "No." + (i + 1) + " §f"
								+ winList.get(i).split("\\|")[0] + " §a("
								+ winList.get(i).split("\\|")[1] + ")");
					}
					int rank = r.getWinRank(player.getName());
					if (rank != 0) {
						for (String s : winList) {
							if (s.split("\\|")[0].equals(player
									.getName())) {
								sm("&b我的排名：&e{ranking} (胜利{amount}场)",
										player,
										"ranking amount",
										new String[]{"" + rank,
												s.split("\\|")[1]});
							}
						}
					} else {
						sm("&b我的排名：&e暂无", player);
					}
				} else {
					sm("&c该排行榜没有数据", player);
				}
				return true;
			}
			if (args[1].equalsIgnoreCase("kd")) {
				sm("&b[star1]单挑-KD比值排行榜[star2]", player);
				if (cache.getKD().size() != 0) {
					List<String> kdList = cache.getKD();
					int max;
					max = Math.min(kdList.size(), 10);
					for (int i = 0; i < max; i++) {
						String color = "§b";
						if (i == 0) {
							color = "§e§l";
						}
						if (i == 1) {
							color = "§6§l";
						}
						if (i == 2) {
							color = "§b§l";
						}
						BigDecimal bg = new BigDecimal(kdList.get(i).split(
								"\\|")[1]);
						double kd = bg.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						player.sendMessage(color + "No." + (i + 1) + " §f"
								+ kdList.get(i).split("\\|")[0] + " §a(" + kd
								+ ")");
					}
					int rank = r.getWinRank(player.getName());
					if (rank != 0) {
						for (String s : kdList) {
							if (s.split("\\|")[0].equals(player
									.getName())) {
								BigDecimal bg = new BigDecimal(s
										.split("\\|")[1]);
								double kd = bg.setScale(2,
										BigDecimal.ROUND_HALF_UP).doubleValue();
								sm("&b我的排名：&e{ranking} (比值{kd})", player,
										"ranking kd", new String[]{"" + rank,
												"" + kd});
							}
						}
					} else {
						sm("&b我的排名：&e暂无", player);
					}
				} else {
					sm("&c该排行榜没有数据", player);
				}
				return true;
			}
		}
		return true;
	}

}
