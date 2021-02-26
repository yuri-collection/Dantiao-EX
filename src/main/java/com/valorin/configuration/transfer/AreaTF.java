package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.areas;
import static com.valorin.configuration.DataFile.areasFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;



import java.util.List;



import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;



import com.valorin.Main;
import com.valorin.data.MySQL;


public class AreaTF {
	public static void execute(Player p) {
		if (!areasFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
		Bukkit.getScheduler()
				.runTaskAsynchronously(
						Main.getInstance(),
						() -> {
							MySQL mysql = Main.getInstance().getMySQL();
							ConfigurationSection section = areas
									.getConfigurationSection("Arenas");
							if (section != null) {
								section.getKeys(false)
										.forEach(
												editName -> {
													String worldA = areas
															.getString("Arenas."
																	+ editName
																	+ ".A.World");
													double xA = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.X"), yA = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.Y"), zA = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.Z");
													float yawA = (float) areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.YAW"), pitchA = (float) areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.PITCH");
													double xB = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.X"), yB = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.Y"), zB = areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.Z");
													float yawB = (float) areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.YAW"), pitchB = (float) areas
															.getDouble("Arenas."
																	+ editName
																	+ ".A.PITCH");
													Location pointA = new Location(
															Bukkit.getWorld(worldA),
															xA, yA, zA, yawA,
															pitchA);
													Location pointB = new Location(
															Bukkit.getWorld(worldA),
															xB, yB, zB, yawB,
															pitchB);
													String displayName = areas
															.getString("Arenas."
																	+ editName
																	+ ".Name");
													mysql.saveArena(editName,
															displayName,
															pointA, pointB);

													String worldW = areas
															.getString("Arenas."
																	+ editName
																	+ ".WatchingPoint.World");
													if (worldW != null) {
														double xW = areas
																.getDouble("Arenas."
																		+ editName
																		+ ".WatchingPoint.X"), yW = areas
																.getDouble("Arenas."
																		+ editName
																		+ ".WatchingPoint.Y"), zW = areas
																.getDouble("Arenas."
																		+ editName
																		+ ".WatchingPoint.Z");
														float yawW = (float) areas
																.getDouble("Arenas."
																		+ editName
																		+ ".WatchingPoint.YAW"), pitchW = (float) areas
																.getDouble("Arenas."
																		+ editName
																		+ ".WatchingPoint.PITCH");
														Location pointW = new Location(
																Bukkit.getWorld(worldW),
																xW, yW, zW,
																yawW, pitchW);
														mysql.setArenaWatchingPoint(
																editName,
																pointW);

														List<String> commandList = areas
																.getStringList("Arenas."
																		+ editName
																		+ ".Commands");
														mysql.setArenaCommands(
																editName,
																commandList);
													}

												});
							}

							String worldW = areas
									.getString("Dantiao-HD-Win.World");
							if (worldW != null) {
								double xW = areas.getInt("Dantiao-HD-Win.X");
								double yW = areas.getInt("Dantiao-HD-Win.Y");
								double zW = areas.getInt("Dantiao-HD-Win.Z");
								Location locationW = new Location(Bukkit
										.getWorld(worldW), xW, yW, zW);
								mysql.setHologramLocation(0, locationW);
							}

							String worldK = areas
									.getString("Dantiao-HD-KD.World");
							if (worldK != null) {
								double xK = areas.getInt("Dantiao-HD-KD.X");
								double yK = areas.getInt("Dantiao-HD-KD.Y");
								double zK = areas.getInt("Dantiao-HD-KD.Z");
								Location locationK = new Location(Bukkit
										.getWorld(worldK), xK, yK, zK);
								mysql.setHologramLocation(1, locationK);
							}

							String worldL = areas
									.getString("Dantiao-LobbyPoint.World");
							if (worldL != null) {
								double xL = areas
										.getInt("Dantiao-LobbyPoint.X");
								double yL = areas
										.getInt("Dantiao-LobbyPoint.Y");
								double zL = areas
										.getInt("Dantiao-LobbyPoint.Z");
								Location locationL = new Location(Bukkit
										.getWorld(worldL), xL, yL, zL);
								mysql.setLobbyLocation(locationL);
							}
						});

	}
}
