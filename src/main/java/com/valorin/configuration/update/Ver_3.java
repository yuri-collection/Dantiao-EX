package com.valorin.configuration.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.valorin.Main;

public class Ver_3 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 3");
					continue;
				}
				if (text.startsWith("  Start:")) {
					newTexts.add(text);
					newTexts.add("    # Whether change the player's game mode to SURVIVAL compulsorily when the game starts");
					newTexts.add("    # 是否在比赛开始时强行为玩家切换为生存模式");
					newTexts.add("    ChangeGameMode: true");
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
