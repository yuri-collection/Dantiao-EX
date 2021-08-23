package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_8 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 8");
					continue;
				}
				if (text.startsWith("    TeleportCountDown:")) {
					newTexts.add(text);
					newTexts.add("    # The settings of auto-respawn");
					newTexts.add("    # 单挑比赛结束后自动复活的相关设置");
					newTexts.add("    AutoRespawn:");
					newTexts.add("      # Whether enable the function which makes player who dies in arena respawn automatically");
					newTexts.add("      # 是否开启此自动复活功能");
					newTexts.add("      Enable: true");
					newTexts.add("      # The way of respawning");
					newTexts.add("      # If some problem occurred when respawn,please try to change the way of respawning");
					newTexts.add("      # Ways provided are as follows");
					newTexts.add("      # SPIGOT - Use method player.spigot().respawn()");
					newTexts.add("      # SETHEALTH - Use method player.setHealth(player.getMaxHealth())");
					newTexts.add("      # If you cannot solve problem by change the way,just disable auto-respawn function");
					newTexts.add("      # 自动重生的方式");
					newTexts.add("      # 如果你服务器的自动重生遇到了问题，请尝试修改此项配置已改变自动重生的方式");
					newTexts.add("      # 目前的方式有：");
					newTexts.add("      # SPIGOT - 使用player.spigot().respawn()方法重生");
					newTexts.add("      # SETHEALTH - 使用player.setHealth(player.getMaxHealth())方法重生");
					newTexts.add("      # 如果你通过修改重生方式都不能解决问题，那请直接关闭此功能");
					newTexts.add("      Way: \"SPIGOT\"");
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
