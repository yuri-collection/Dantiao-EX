package com.valorin.configuration;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DataFile {
	public static File areasFile = new File("plugins/Dantiao/areas.yml");
	// 记录区域：单挑大厅，KD全息图位置，WIN全息图位置，各个竞技场
	public static File shopFile = new File("plugins/Dantiao/shop.yml");
	// 记录积分商城中的商品
	public static File recordsFile = new File("plugins/Dantiao/records.yml");
	// 玩家的比赛记录
	public static File pdFile = new File("plugins/Dantiao/playersdata.yml");
	// 玩家数据，记录玩家积分，所用的语言文件
	public static File blacklistFile = new File("plugins/Dantiao/blacklist.yml");
	// 记录黑名单
	public static File symbolsFile = new File("plugins/Dantiao/symbols.yml");
	// 特殊符号
	public static File rankingFile = new File("plugins/Dantiao/ranking.yml");
	// 排行榜
	public static File seasonFile = new File("plugins/Dantiao/season.yml");
	// 排行榜
	public static FileConfiguration areas;
	public static FileConfiguration shop;
	public static FileConfiguration records;
	public static FileConfiguration pd;
	public static FileConfiguration blacklist;
	public static FileConfiguration symbols;
	public static FileConfiguration ranking;
	public static FileConfiguration season;

	public static void loadData() {
		areas = YamlConfiguration.loadConfiguration(areasFile);
		shop = YamlConfiguration.loadConfiguration(shopFile);
		records = YamlConfiguration.loadConfiguration(recordsFile);
		symbols = YamlConfiguration.loadConfiguration(symbolsFile);
		pd = YamlConfiguration.loadConfiguration(pdFile);
		blacklist = YamlConfiguration.loadConfiguration(blacklistFile);
		ranking = YamlConfiguration.loadConfiguration(rankingFile);
		season = YamlConfiguration.loadConfiguration(seasonFile);
		areas.options().copyDefaults(true);
		shop.options().copyDefaults(true);
		records.options().copyDefaults(true);
		symbols.options().copyDefaults(true);
		pd.options().copyDefaults(true);
		blacklist.options().copyDefaults(true);
		ranking.options().copyDefaults(true);
		season.options().copyDefaults(true);
	}

	public static void saveAreas() {
		try {
			areas.save(areasFile);
		} catch (Exception ignored) { }
	}

	public static void saveShop() {
		try {
			shop.save(shopFile);
		} catch (Exception ignored) { }
	}

	public static void saveRecords() {
		try {
			records.save(recordsFile);
		} catch (Exception ignored) { }
	}

	public static void savepd() {
		try {
			pd.save(pdFile);
		} catch (Exception ignored) { }
	}

	public static void saveSymbols() {
		try {
			symbols.save(symbolsFile);
		} catch (Exception ignored) { }
	}

	public static void saveBlackList() {
		try {
			blacklist.save(blacklistFile);
		} catch (Exception ignored) { }
	}

	public static void saveRanking() {
		try {
			ranking.save(rankingFile);
		} catch (Exception ignored) { }
	}
	
	public static void saveSeason() {
		try {
			season.save(seasonFile);
		} catch (Exception ignored) { }
	}
}
