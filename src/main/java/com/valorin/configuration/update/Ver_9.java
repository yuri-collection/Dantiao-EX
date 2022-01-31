package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_9 {
	public static void execute() {
		try {
			File configFile = new File(Main.getInstance().getDataFolder(),
					"config.yml");

			List<String> texts = ConfigUpdate.readTexts(configFile);
			
			List<String> newTexts = new ArrayList<>();

			for (String text : texts) {
				if (text.startsWith("ConfigVersion:")) {
					newTexts.add("ConfigVersion: 9");
					continue;
				}
				if (text.startsWith("    EnableProjectileProtection:")) {
					newTexts.add(text);
					newTexts.add("    # Game time limit in seconds.If the timeout occurs,the winner will be decided according to the preset handling scheme in this configuration file.If there is no scheme,players on both sides will be judged to be in a draw after the game is over time");
					newTexts.add("    # 比赛时间限制，单位为秒。若超时则会按本配置文件中的预设方案判定赢家，若无方案则判定平局");
					newTexts.add("    TimeOut: 300");
					newTexts.add("    # The whitelist of commands.If the command player types in the game contains the following sentence,it will be allowed");
					newTexts.add("    # 指令白名单。如果玩家在比赛时输入的指令包含以下的字符，指令则会被允许执行");
					newTexts.add("    CommandWhitelist:");
					newTexts.add("      - '/dantiao test_whitelist_command1'");
					newTexts.add("      - '/dantiao test_whitelist_command2'");
					continue;
				}
				if (text.startsWith("    TeleportCountDown: 6")) {
					newTexts.add(text);
					newTexts.add("    # The handling scheme to decide the winner after timeout");
					newTexts.add("    # 超时后判定输赢的方案");
					newTexts.add("    TimeOutHandlingScheme:");
					newTexts.add("      # Whether enable the function which will provide a handling scheme after the game is over time to decide the winner.If it is disabled,players on both sides will be judged to be in a draw after the game is over time");
					newTexts.add("      # 是否开启比赛超时的输赢判定方案，若不开启此方案，比赛超时后则直接判定为平局");
					newTexts.add("      Enable: true");
					newTexts.add("      # The handling scheme of game timeout");
					newTexts.add("      # Schemes provided are as follows");
					newTexts.add("      # COMPARE_HEALTH - Compare the health value of players on both sides");
					newTexts.add("      # COMPARE_HEALTH_PERCENTAGE - Compare the health percentage of players on both sides");
					newTexts.add("      # 超时后判定输赢的方案");
					newTexts.add("      # 目前的方案有：");
					newTexts.add("      # COMPARE_HEALTH - 比较双方玩家的血量值");
					newTexts.add("      # COMPARE_HEALTH_PERCENTAGE - 比较双方玩家的血量百分比");
					newTexts.add("      Scheme: \"COMPARE_HEALTH\"");
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
