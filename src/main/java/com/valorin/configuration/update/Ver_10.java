package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_10 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 10");
					continue;
				}
				if (text.startsWith("  RefreshInterval:")) {
					newTexts.add(text);
					newTexts.add("# Skull Settings");
					newTexts.add("# 排行头颅设置");
					newTexts.add("Skull:");
					newTexts.add("  # The time interval in seconds for automatically refreshing skulls for ranking");
					newTexts.add("  # 排行头颅刷新的时间间隔（单位为秒）");
					newTexts.add("  RefreshInterval: 300");
					newTexts.add("# Sign Settings");
					newTexts.add("# 排行木牌设置");
					newTexts.add("Sign:");
					newTexts.add("  # The time interval in seconds for automatically refreshing signs for ranking");
					newTexts.add("  # 排行头颅刷新的时间间隔（单位为秒）");
					newTexts.add("  RefreshInterval: 300");
					continue;
				}
				if (text.startsWith("    EnableProjectileProtection:")) {
					newTexts.add(text);
					newTexts.add("    # Whether the teleportation triggered by other plugin is allowed");
					newTexts.add("    # 是否允许比赛时玩家进行由其他插件触发的传送。如果你的服务器允许玩家在单挑竞技场使用一些由其他插件制造的传送道具，请将此项设置为true");
					newTexts.add("    IsTeleportationTriggeredByOtherPluginAllowed: false");
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
