package com.valorin.configuration.update;

import static com.valorin.Main.getInstance;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.base.Charsets;
import com.valorin.Main;

public class ConfigUpdate {
	public static void execute() {
		FileConfiguration defaultConfig = new YamlConfiguration();
		FileConfiguration nowConfig = new YamlConfiguration();
		File nowConfigFile = new File(getInstance().getDataFolder(),
				"config.yml");
		int nowVersion = 0;
		int lastVersion = -1;
		try {
			defaultConfig.load(new BufferedReader(new InputStreamReader(Main
					.getInstance().getResource("config.yml"), Charsets.UTF_8)));
			lastVersion = defaultConfig.getInt("ConfigVersion");

			nowConfig.load(new BufferedReader(new InputStreamReader(
					new FileInputStream(nowConfigFile), Charsets.UTF_8)));
			nowVersion = nowConfig.getInt("ConfigVersion");
		} catch (IOException | InvalidConfigurationException e) {
			/* e.printStackTrace(); */
		}

		if (lastVersion == -1) {
			return;
		}

		if (nowVersion >= lastVersion) {
			return;
		}

		ConsoleCommandSender console = Bukkit.getConsoleSender();
		console.sendMessage("§8§l[§bDantiao§8§l]");
		console.sendMessage("§f- §7正在进行config.yml升级");
		console.sendMessage("§f- §7Now updating the config.yml");

		File backUpConfigFile = new File(getInstance().getDataFolder()
				+ "/OldConfigs", getDate() + ".yml");
		File fileParent = backUpConfigFile.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		try {
			backUpConfigFile.createNewFile();
		} catch (IOException e) {
			/* e.printStackTrace(); */
		}
		try {
			copyFile(nowConfigFile, backUpConfigFile);
		} catch (IOException e) {
			/* e.printStackTrace(); */
		}

		if (nowVersion < 1) {
			Ver_1.execute();
		} else {
			switch (nowVersion) {
			case 1:
				Ver_2.execute();
			case 2:
				Ver_3.execute();
			case 3:
				Ver_4.execute();
			case 4:
				Ver_5.execute();
			}
		}

		console.sendMessage("§8§l[§bDantiao§8§l]");
		console.sendMessage("§f- §aconfig.yml升级已完成");
		console.sendMessage("§f- §aThe config.yml has updated successfully!");
	}

	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		FileInputStream inputStream = new FileInputStream(sourceFile);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				inputStream);
		FileOutputStream outputStream = new FileOutputStream(targetFile);
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				outputStream);
		byte[] bytes = new byte[1024 * 2];
		int len = 0;
		while ((len = inputStream.read(bytes)) != -1) {
			bufferedOutputStream.write(bytes, 0, len);
		}
		bufferedOutputStream.flush();
		bufferedInputStream.close();
		bufferedOutputStream.close();
		inputStream.close();
		outputStream.close();
	}

	public static String getDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HHmmss");
		String date = formatter.format(cal.getTime());
		return date;
	}

	public static List<String> readTexts(File file) {
		List<String> texts = new ArrayList<String>();
		try {
			texts = new ArrayList<String>();
	        BufferedReader bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
	        String s = null;
	        while ((s = bufr.readLine()) != null) {
				texts.add(s);
			}
			bufr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texts;
	}
	
	public static void writeTexts(File file, List<String> texts) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			for (int i = 0; i < texts.size(); i++) {
				if (i == texts.size() - 1) {
					writer.write(texts.get(i));
				} else {
					writer.write(texts.get(i) + "\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
