package com.valorin.configuration.update;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.valorin.Main;

public class Ver_5 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (int i = 0;i < texts.size();i++) {
				String text = texts.get(i);
				
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 5");
					continue;
				}
				if (text.startsWith("      Points:")) {
					newTexts.add(text);
					if (texts.get(i - 1).startsWith("      # 奖励给获胜方的单挑积分数")) {
						newTexts.add("      # The deduction of loser's points");
						newTexts.add("      # 输家扣除的单挑积分");
						newTexts.add("      PointsDeducted: 0");
					} else {
						if (texts.get(i - 4).startsWith("    Inviting:") || texts.get(i - 4).startsWith("    Compulsion:")) {
							newTexts.add("      PointsDeducted: 0");
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
