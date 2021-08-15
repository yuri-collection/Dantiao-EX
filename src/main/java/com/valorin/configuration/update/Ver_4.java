package com.valorin.configuration.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.valorin.Main;

public class Ver_4 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (int i = 0;i < texts.size();i++) {
				String text = texts.get(i);
				
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 4");
					continue;
				}
				if (text.startsWith("    ProtectionExp:")) {
					newTexts.add(text);
					newTexts.add("    # The teleport countdown of the winner");
					newTexts.add("    # 比赛结束后胜利一方的玩家传送离场的倒计时时间");
					newTexts.add("    TeleportCountDown: 6");
					continue;
				}
				if (text.startsWith("  # Record: The data of ranking and players' game record")) {
					newTexts.add(text);
					newTexts.add("  # Season: The data of season");
					continue;
				}
				if (text.startsWith("  # Record: 是否使用数据库储存玩家的战斗记录以及全服的胜场数排行榜、KD值排行榜数据")) {
					newTexts.add(text);
					newTexts.add("  # Season: 是否使用数据库储存赛季的奖励方案数据");
					continue;
				}
				if (text.startsWith("    Record:")) {
					newTexts.add(text);
					if (texts.get(i - 8).startsWith("  Function:")) {
						newTexts.add("    Season: false");
					}
					continue;
				}
				
				newTexts.add(text);
			}
			
			ConfigUpdate.writeTexts(configFile, newTexts);

			Main.getInstance().saveResource("config.yml", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
