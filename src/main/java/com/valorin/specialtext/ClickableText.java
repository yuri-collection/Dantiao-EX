package com.valorin.specialtext;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import static com.valorin.configuration.languagefile.MessageSender.gm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;

public class ClickableText {

	public static void sendRequest(String sn, String rn) {
		Player r = Bukkit.getPlayerExact(rn);

		TextComponent txt4 = new TextComponent();
		txt4.setText(gm("&c[x]&f&n点击拒绝&r", r));
		txt4.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/dt deny " + sn));

		TextComponent txt3 = new TextComponent();
		txt3.setText(Dec.getStr(3));
		txt3.addExtra(txt4);

		TextComponent txt2 = new TextComponent();
		txt2.setText(gm("&a[v]&f&n点击接受&r", r));
		txt2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/dt accept "
				+ sn));
		txt2.addExtra(txt3);

		TextComponent txt = new TextComponent();
		txt.setText(Dec.getStr(1));
		txt.addExtra(txt2);

		r.spigot().sendMessage(txt);
	}

	public static TextComponent sendInvitationToAll(String sn) {
		TextComponent txt2 = new TextComponent();
		txt2.setText(gm("&a[v]&f&n点击挑战他&r"));
		txt2.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/dt accept "
				+ sn));

		TextComponent txt = new TextComponent();
		txt.setText(Dec.getStr(6));
		txt.addExtra(txt2);

		return txt;
	}

	public static void sendWebsiteInfo(CommandSender sender) {
		boolean isPlayer = sender instanceof Player;

		String text7 = "§f§l]";
		String text6 = "§bCurseForge.com";
		String text5 = "§f§l] [";
		String text4 = "§bSpigotMC.org";
		String text3 = "§f§l] [";
		String text2 = "§bMCBBS.net";
		String text = "§a§lWeb: §f§l[";
		TextComponent txt7 = null, txt6 = null, txt5 = null, txt4 = null, txt3 = null, txt2 = null, txt = null;
		if (isPlayer) {
			if (sender.hasPermission("dt.admin")) {
				txt7 = new TextComponent();
				txt7.setText(text7);

				txt6 = new TextComponent();
				txt6.setText(text6);
				txt6.setHoverEvent(new HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(
								"\n§f§l● §7点击查看本插件在§fCurseForge§7的帖子\n§f§l● §7Click to view this project on §fCurseForge\n")
								.create()));
				txt6.setClickEvent(new ClickEvent(Action.OPEN_URL,
						"https://www.curseforge.com/minecraft/bukkit-plugins/dantiao"));
				txt6.addExtra(txt7);

				txt5 = new TextComponent();
				txt5.setText(text5);
				txt5.addExtra(txt6);

				txt4 = new TextComponent();
				txt4.setText(text4);
				txt4.setHoverEvent(new HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(
								"\n§f§l● §7点击查看本插件在§fSpigotMC§7的帖子\n§f§l● §7Click to view this project on §fSpigotMC\n")
								.create()));
				txt4.setClickEvent(new ClickEvent(Action.OPEN_URL,
						"https://www.spigotmc.org/resources/60432"));
				txt4.addExtra(txt5);

				txt3 = new TextComponent();
				txt3.setText(text3);
				txt3.addExtra(txt4);

				txt2 = new TextComponent();
				txt2.setText(text2);
				txt2.setHoverEvent(new HoverEvent(
						HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(
								"\n§f§l● §7点击查看本插件在§f我的世界中文论坛§7的帖子\n§f§l● §7Click to view this project on §fMCBBS\n")
								.create()));
				txt2.setClickEvent(new ClickEvent(Action.OPEN_URL,
						"https://www.mcbbs.net/thread-1141430-1-1.html"));
				txt2.addExtra(txt3);

				txt = new TextComponent();
				txt.setText(text);
				txt.addExtra(txt2);
				((Player) sender).spigot().sendMessage(txt);
			}
		} else {
			sender.sendMessage(text + text2 + text3 + text4 + text5 + text6
					+ text7);
		}
	}

	public static void sendDocumentInfo(CommandSender sender) {
		boolean isPlayer = sender instanceof Player;

		String text5 = "§e]";
		String text4 = "§e中文版-EX";
		String text3 = "§e] [";
		String text2 = "§e中文版";
		String text = "§6文档 | Document ---------------> §e[";
		TextComponent txt5 = null, txt4 = null, txt3 = null, txt2 = null, txt = null;
		if (isPlayer) {
			txt5 = new TextComponent();
			txt5.setText(text5);

			txt4 = new TextComponent();
			txt4.setText(text4);
			txt4.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("\n§f● §7点击查看本插件拓展版专属文档\n").create()));
			txt4.setClickEvent(new ClickEvent(Action.OPEN_URL,
					"https://1392847363.gitbook.io/dantiao-ex"));
			txt4.addExtra(txt5);

			txt3 = new TextComponent();
			txt3.setText(text3);
			txt3.addExtra(txt4);

			txt2 = new TextComponent();
			txt2.setText(text2);
			txt2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("\n§f● §7点击查看本插件的基础使用文档\n").create()));
			txt2.setClickEvent(new ClickEvent(Action.OPEN_URL,
					"https://1392847363.gitbook.io/dantiao"));
			txt2.addExtra(txt3);

			txt = new TextComponent();
			txt.setText(text);
			txt.addExtra(txt2);
			((Player) sender).spigot().sendMessage(txt);
		} else {
			sender.sendMessage(text + text2 + text3 + text4 + text5);
		}
	}

	public static void sendHeadInfo(CommandSender sender) {
		boolean isPlayer = sender instanceof Player;

		String text = "§b§lDan§3§l§otiao §c§l单挑拓展版 A DUEL PLUGIN §cBy-Valorin";
		if (isPlayer) {
			TextComponent txt = null;
			txt = new TextComponent();
			txt.setText(text);
			if (sender.hasPermission("dt.admin")) {
				txt.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder("\n§e基本信息\n\n§f● §7当前运行版本:"
								+ Main.getInstance().getDescription()
										.getVersion() + "\n").create()));
			}
			((Player) sender).spigot().sendMessage(txt);
		} else {
			sender.sendMessage(text);
		}
	}
}
