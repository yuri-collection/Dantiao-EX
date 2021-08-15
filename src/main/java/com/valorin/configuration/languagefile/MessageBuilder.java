package com.valorin.configuration.languagefile;

import static com.valorin.Main.getInstance;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.configuration.ConfigManager;

public class MessageBuilder {

	/*
	 * Chinese:中文消息，必须 player:玩家，必须 v:变量，非必须，默认无 vl：变量的表示，非必须，默认无 prefix:是否前缀，必须，默认无
	 */
	public static String gmLog(String Chinese, Player player, String v, String[] vl,
			boolean prefix, boolean hdTitle) // 获取一个语言项
	{
		if (Chinese.length() == 0) {
			return "";
		}
		LanguageFileLoader lfl = getInstance().getLanguageFileLoader();
		Map<File, List<String>> lang = lfl.getLang();
		List<File> languagesList = lfl.getLanguagesList();
		List<String> defaultLang = lfl.getDefaultLang();

		if (defaultLang.contains(Chinese)) {// 这条中文属于默认消息，通过验证
			String finalMessage = ""; // 最终要输出的消息
			int number = 0;
			for (int i = 0; i < defaultLang.size(); i++) {
				if (defaultLang.get(i).equals(Chinese)) {
					number = i;
				}
			}
			if (player == null) {
				ConfigManager configManager = getInstance().getConfigManager();
				boolean exist = false;
				if (configManager.getDefaultLanguage() != null) {
					for (File f : languagesList) {
						if (f.getName().replace(".txt", "")
								.equals(configManager.getDefaultLanguage())) {
							exist = true;
							finalMessage = lang.get(f).get(number);
						}
					}
				}
				if (!exist) {
					finalMessage = defaultLang.get(number);
				}
			} else {
				String plang = getInstance().getCacheHandler()
						.getLanguageFile().get(player.getName());
				if (plang == null) {
					String configDefaultLang = getInstance().getConfigManager()
							.getDefaultLanguage();
					boolean exist = false;
					if (configDefaultLang != null) {
						for (File f : languagesList) {
							if (f.getName().replace(".txt", "")
									.equals(configDefaultLang)) {
								exist = true;
								finalMessage = lang.get(f).get(number);
							}
						}
					}
					if (!exist) {
						finalMessage = defaultLang.get(number);
					}
				} else {
					boolean isDefaultLang = true;
					if (languagesList != null) {
						for (File f : languagesList) {
							if (f.getName().split("\\.")[0].equals(plang)) {
								if (number > lang.get(f).size() - 1) {
									return "";
								}
								finalMessage = lang.get(f).get(number);
								isDefaultLang = false;
								break;
							}
						}
					}
					if (isDefaultLang) {
						finalMessage = defaultLang.get(number);
					}
				}
			}
			// 终端处理
			if (vl != null) {
				String[] vll = v.split("\\ ");
				for (int i = 0; i < vll.length; i++) {
					finalMessage = finalMessage.replace("{" + vll[i] + "}",
							vl[i]);
				}
			}
			finalMessage = finalMessage.replace("&", "§");
			if (player != null) {
				finalMessage = SymbolsExecutor.execute(finalMessage);// 替换符号
			} else {
				if (hdTitle) {
					finalMessage = SymbolsExecutor.execute(finalMessage);// 替换符号
				} else {
					List<String> symbolsMark = Main.getInstance()
							.getSymbolLoader().getSymbolsMark();
					for (String s : symbolsMark) {
						finalMessage = finalMessage.replace(s, "");
					}
				}
			}
			if (prefix) {
				finalMessage = Main.getInstance().getConfigManager()
						.getPrefix()
						+ finalMessage;
			}
			return finalMessage;
		} else {
			return Chinese+"§8Error:This message hasn't registered!(消息未注册，可能是编码问题，请联系腐竹建议TA为语言文件转码)";
		}
	}
}
