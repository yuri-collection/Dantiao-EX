package com.valorin.configuration.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.valorin.Main;

public class Ver_2 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (int i = 0;i < texts.size();i++) {
				String text = texts.get(i);
				
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 2");
					continue;
				}
				if (text.startsWith("  # Dan: The data of players' DUEL LEVEL")) {
					newTexts.add(text);
					newTexts.add("  # Energy: The data of players' energy");
					continue;
				}
				if (text.startsWith("  # Dan: 是否使用数据库储存玩家的段位经验数据")) {
					newTexts.add(text);
					newTexts.add("  # Energy: 是否使用数据库储存玩家的精力值数据");
					continue;
				}
				if (text.startsWith("    Dan:")) {
					newTexts.add(text);
					if (texts.get(i - 3).startsWith("  Function:")) {
						newTexts.add("    Energy: true");
					}
					continue;
				}
				if (text.startsWith("      Firework:")) {
					newTexts.add(text);
					if (texts.get(i - 1).startsWith("      # 是否为获胜方燃放一个烟花")) {
						newTexts.add("      # Whether broadcast the result of the game");
						newTexts.add("      # 比赛结束后是否全服广播比赛情况");
						newTexts.add("      Broadcast: true");
					} else {
						if (texts.get(i - 7).startsWith("    Inviting:")) {
							newTexts.add("      Broadcast: false");
						} else {
							newTexts.add("      Broadcast: true");
						}
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
