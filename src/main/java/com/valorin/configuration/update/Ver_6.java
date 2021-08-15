package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_6 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 6");
					continue;
				}
				if (text.startsWith("  Finish:")) {
					newTexts.add("  Process:");
					newTexts.add("    # Whether the player who isn't OP can open a chest in the game");
					newTexts.add("    # 非OP玩家是否能在比赛中打开普通箱子");
					newTexts.add("    IsOpeningChestAllowed: true");
					newTexts.add("    # Whether the player who isn't OP can open an ender chest in the game");
					newTexts.add("    # 非OP玩家是否能在比赛中打开末影箱");
					newTexts.add("    IsOpeningEnderChestAllowed: true");
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
