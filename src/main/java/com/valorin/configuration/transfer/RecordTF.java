package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.ranking;
import static com.valorin.configuration.DataFile.records;
import static com.valorin.configuration.DataFile.recordsFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.data.MySQL;

public class RecordTF {
	public static void execute(Player p) {
		if (!recordsFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
		Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
			MySQL mysql = Main.getInstance().getMySQL();
			mysql.setRanking(0, ranking.getStringList("Win"));
			mysql.setRanking(1, ranking.getStringList("KD"));
		for (String name : records.getKeys(false)) {
			ConfigurationSection section = records.getConfigurationSection(name + ".Record");
			if (section != null) {
				section.getKeys(false).forEach(subKey -> {
					String prefix = name + ".Record." + subKey;
					
					String date = records.getString(prefix + ".date");
					String opponent = records.getString(prefix + ".player");
					String server = records.getString(prefix + ".server");
					int time = records.getInt(prefix + ".time");
					int damage = records.getInt(prefix + ".damage");
					int maxDamage = records.getInt(prefix + ".maxdamage");
					int result;
					if (records.getBoolean(prefix + ".isWin")) {
						result = 0; // 胜利
					} else {
						if (records.getBoolean(prefix + ".isDraw")) {
						    result = 2; // 失败
						} else {
							result = 1; // 平局
						}
					}
					int startWay = records.getInt(prefix + ".startWay");
					int expChange = records.getInt(prefix + ".expChange");
					String arenaEditName = records.getString(prefix + ".arenaEditName");
					mysql.addRecord(name, date, opponent, server, time, damage, maxDamage, result, startWay, expChange, arenaEditName);
				});
			}
			int wins = records.getInt(name + ".Win");
			int loses = records.getInt(name + ".Lose");
			int draws = records.getInt(name + ".Draw");
			mysql.setWins(name, wins);
			mysql.setLoses(name, loses);
			mysql.setDraws(name, draws);
		}
		});
	}
}
