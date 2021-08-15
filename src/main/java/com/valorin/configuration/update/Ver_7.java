package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_7 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 7");
					continue;
				}
				if (text.startsWith("    IsOpeningEnderChestAllowed:")) {
					newTexts.add(text);
					newTexts.add("    # Whether protect players from damage of projectile");
					newTexts.add("    # 是否开启弹射物保护。开启时，玩家将不会受到任何场外玩家和怪物的远程伤害。但可能会导致一些模组武器无法造成远程伤害，纯净服务器推荐开启，模组服视实际情况而定。");
					newTexts.add("    EnableProjectileProtection: true");
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
