package com.valorin.timetable;

import java.util.ArrayList;
import java.util.List;

import com.valorin.Main;
import com.valorin.configuration.ConfigManager;

public class Timetable {

	List<String> searching = new ArrayList<String>();
	List<String> invite = new ArrayList<String>();

	public List<String> getSearching() {
		return searching;
	}

	public List<String> getInvite() {
		return invite;
	}

	public void close() {
		searching = new ArrayList<String>();
		invite = new ArrayList<String>();
	}

	public Timetable() {
		List<List<String>> rawTimetables = new ArrayList<>();
		ConfigManager configManager = Main.getInstance().getConfigManager();
		rawTimetables.add(configManager.getSearchingTimeTable());
		rawTimetables.add(configManager.getInviteTimeTable());
		int index = 0;
		for (List<String> rawTimetable : rawTimetables) {
			List<String> timetable = new ArrayList<>();
			boolean hasTimetable = false;
			if (rawTimetable.size() == 0) {
				continue;
			}
			for (String s : rawTimetable) { // 格式 13:00-15:00
				try {
					String[] s1 = s.split("-");
					String start = s1[0];
					String end = s1[1];
					int startMinutes = Integer.parseInt(start.split(":")[0])
							* 60 + Integer.parseInt(start.split(":")[1]);
					int endMinutes = Integer.parseInt(end.split(":")[0]) * 60
							+ Integer.parseInt(end.split(":")[1]);
					if (startMinutes < endMinutes) {
						timetable.add(s);
						hasTimetable = true;
					}
				} catch (Exception ignored) {
				}
			}
			if (!hasTimetable) {
				continue;
			}
			if (index == 0) {
				this.searching = timetable;
			}
			if (index == 1) {
				this.invite = timetable;
			}
			index++;
		}
	}
}
