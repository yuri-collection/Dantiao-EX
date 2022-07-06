package com.valorin.data;

import com.valorin.configuration.ConfigManager;
import com.valorin.data.encapsulation.Good;
import com.valorin.data.encapsulation.RankingSign;
import com.valorin.data.encapsulation.Record;
import com.valorin.data.encapsulation.RankingSkull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.DataFile.*;

public class Data {
    private static boolean useDatabase;

    private static boolean areaB = false, blacklistB = false, danB = false,
            energyB = false, languageFileB = false, pointB = false,
            pointShopB = false, recordB = false, seasonB = false;

    public static void initialB(ConfigManager configManager) {
        if (configManager.isUseMySQL()) {
            areaB = configManager.isAreaUseMySQL();
            blacklistB = configManager.isBlacklistUseMySQL();
            danB = configManager.isDanUseMySQL();
            energyB = configManager.isEnergyUseMySQL();
            languageFileB = configManager.isLanguageFileUseMySQL();
            pointB = configManager.isPointUseMySQL();
            pointShopB = configManager.isPointShopUseMySQL();
            recordB = configManager.isRecordUseMySQL();
            seasonB = configManager.isSeasonUseMySQL();
        }
    }

    public static List<String> getArenas() { // 获取所有竞技场的编辑名
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenas();
        } else {
            List<String> arenaList = new ArrayList<>();
            if (!areasFile.exists()) {
                return arenaList;
            }
            ConfigurationSection section = areas
                    .getConfigurationSection("Arenas");
            if (section == null) {
                return arenaList;
            }
            arenaList.addAll(section.getKeys(false));
            return arenaList;
        }
    }

    public static String getArenaDisplayName(String editName) { // 获取某个竞技场的展示名
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaDisplayName(editName);
        } else {
            return areas.getString("Arenas." + editName + ".Name");
        }
    }

    public static Location getArenaPointA(String editName) { // 获取某个竞技场的A点
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaPointA(editName);
        } else {
            World world = Bukkit.getWorld(areas.getString("Arenas." + editName
                    + ".A.World"));
            double x = areas.getDouble("Arenas." + editName + ".A.X"), y = areas
                    .getDouble("Arenas." + editName + ".A.Y"), z = areas
                    .getDouble("Arenas." + editName + ".A.Z");
            float yaw = (float) areas
                    .getDouble("Arenas." + editName + ".A.YAW"), pitch = (float) areas
                    .getDouble("Arenas." + editName + ".A.PITCH");
            return new Location(world, x, y, z, yaw, pitch);
        }
    }

    public static Location getArenaPointB(String editName) { // 获取某个竞技场的B点
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaPointB(editName);
        } else {
            World world = Bukkit.getWorld(areas.getString("Arenas." + editName
                    + ".B.World"));
            double x = areas.getDouble("Arenas." + editName + ".B.X"), y = areas
                    .getDouble("Arenas." + editName + ".B.Y"), z = areas
                    .getDouble("Arenas." + editName + ".B.Z");
            float yaw = (float) areas
                    .getDouble("Arenas." + editName + ".B.YAW"), pitch = (float) areas
                    .getDouble("Arenas." + editName + ".B.PITCH");
            return new Location(world, x, y, z, yaw, pitch);
        }
    }

    public static List<String> getArenaCommands(String editName) { // 获取某个竞技场的指令组
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaCommands(editName);
        } else {
            return areas.getStringList("Arenas." + editName + ".Commands");
        }
    }

    public static Location getArenaWatchingPoint(String editName) { // 获取某个竞技场的观战点
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaWatchingPoint(editName);
        } else {
            String world = areas.getString("Arenas." + editName
                    + ".WatchingPoint.World");
            if (world == null) {
                return null;
            }
            double x = areas.getDouble("Arenas." + editName
                    + ".WatchingPoint.X"), y = areas.getDouble("Arenas."
                    + editName + ".WatchingPoint.Y"), z = areas
                    .getDouble("Arenas." + editName + ".WatchingPoint.Z");
            float yaw = (float) areas.getDouble("Arenas." + editName
                    + ".WatchingPoint.YAW"), pitch = (float) areas
                    .getDouble("Arenas." + editName + ".WatchingPoint.PITCH");
            return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        }
    }

    public static void setArenaCommands(String editName, List<String> list) { // 设置某竞技场的指令组
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setArenaCommands(editName, list);
                } else {
                    areas.set("Arenas." + editName + ".Commands", list);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setArenaWatchingPoint(String editName, Location location) { // 设置某竞技场的观战点
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setArenaWatchingPoint(editName,
                            location);
                } else {
                    if (location == null) {
                        areas.set("Arenas." + editName + ".WatchingPoint", null);
                    } else {
                        areas.set(
                                "Arenas." + editName + ".WatchingPoint.World",
                                location.getWorld().getName());
                        areas.set("Arenas." + editName + ".WatchingPoint.X",
                                location.getX());
                        areas.set("Arenas." + editName + ".WatchingPoint.Y",
                                location.getY());
                        areas.set("Arenas." + editName + ".WatchingPoint.Z",
                                location.getZ());
                        areas.set("Arenas." + editName + ".WatchingPoint.YAW",
                                location.getYaw());
                        areas.set(
                                "Arenas." + editName + ".WatchingPoint.PITCH",
                                location.getPitch());
                    }
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void saveArena(String editName, String displayName,
                                 Location pointA, Location pointB) { // 保存一个已编辑完的竞技场
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().saveArena(editName, displayName,
                            pointA, pointB);
                } else {
                    areas.set("Arenas." + editName + ".A.World", pointA
                            .getWorld().getName());
                    areas.set("Arenas." + editName + ".A.X", pointA.getX());
                    areas.set("Arenas." + editName + ".A.Y", pointA.getY());
                    areas.set("Arenas." + editName + ".A.Z", pointA.getZ());
                    areas.set("Arenas." + editName + ".A.YAW",
                            pointA.getYaw());
                    areas.set("Arenas." + editName + ".A.PITCH",
                            pointA.getPitch());
                    areas.set("Arenas." + editName + ".B.World", pointB
                            .getWorld().getName());
                    areas.set("Arenas." + editName + ".B.X", pointB.getX());
                    areas.set("Arenas." + editName + ".B.Y", pointB.getY());
                    areas.set("Arenas." + editName + ".B.Z", pointB.getZ());
                    areas.set("Arenas." + editName + ".B.YAW",
                            pointB.getYaw());
                    areas.set("Arenas." + editName + ".B.PITCH",
                            pointB.getPitch());
                    areas.set("Arenas." + editName + ".Name", displayName);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void deleteArena(String editName) { // 删除某个竞技场
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().deleteArena(editName);
                } else {
                    areas.set("Arenas." + editName, null);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static Location getHologramLocation(int type) { // 获取排行榜全息图的所在位置
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getHologramLocation(type);
        } else {
            String prefix;
            if (type == 0) {
                prefix = "Dantiao-HD-Win.";
            } else {
                prefix = "Dantiao-HD-KD.";
            }
            String world = areas.getString(prefix + "World");
            if (world == null) {
                return null;
            }
            double x = areas.getInt(prefix + "X");
            double y = areas.getInt(prefix + "Y");
            double z = areas.getInt(prefix + "Z");
            return new Location(Bukkit.getWorld(world), x, y, z);
        }
    }

    public static void setHologramLocation(int type, Location location) { // 设置某排行榜全息图的所在位置
        useDatabase = areaB;
        if (useDatabase) {
            getInstance().getMySQL().setHologramLocation(type, location);
        } else {
            String prefix;
            if (type == 0) {
                prefix = "Dantiao-HD-Win.";
            } else {
                prefix = "Dantiao-HD-KD.";
            }
            if (location == null) {
                areas.set(prefix.replace(".", ""), null);
            } else {
                areas.set(prefix + "World", location.getWorld().getName());
                areas.set(prefix + "X", location.getX());
                areas.set(prefix + "Y", location.getY());
                areas.set(prefix + "Z", location.getZ());
            }
            saveAreas();
        }
    }

    public static Location getLobbyLocation() { // 获取大厅传送点的所在位置
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getLobbyLocation();
        } else {
            String prefix = "Dantiao-LobbyPoint.";
            String world = areas.getString(prefix + "World");
            if (world == null) {
                return null;
            }
            double x = areas.getInt(prefix + "X");
            double y = areas.getInt(prefix + "Y");
            double z = areas.getInt(prefix + "Z");
            return new Location(Bukkit.getWorld(world), x, y, z);
        }
    }

    public static void setLobbyLocation(Location location) { // 设置大厅传送点的所在位置
        useDatabase = areaB;
        if (useDatabase) {
            getInstance().getMySQL().setLobbyLocation(location);
        } else {
            if (location == null) {
                areas.set("Dantiao-LobbyPoint", null);
            } else {
                String prefix = "Dantiao-LobbyPoint.";
                areas.set(prefix + "World", location.getWorld().getName());
                areas.set(prefix + "X", location.getX());
                areas.set(prefix + "Y", location.getY());
                areas.set(prefix + "Z", location.getZ());
            }
            saveAreas();
        }
    }

    public static List<RankingSkull> getRankingSkullList() { // 获取所有排行头颅
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getRankingSkull();
        } else {
            List<RankingSkull> rankingSkullList = new ArrayList<>();
            if (!areasFile.exists()) {
                return rankingSkullList;
            }
            ConfigurationSection section = areas
                    .getConfigurationSection("RankingSkulls");
            if (section == null) {
                return rankingSkullList;
            }
            for (String key : section.getKeys(false)) {
                String prefix = "RankingSkulls." + key + ".";
                String worldName = areas.getString(prefix + "World");
                World world = Bukkit.getWorld(worldName);
                double x = areas.getInt(prefix + "X");
                double y = areas.getInt(prefix + "Y");
                double z = areas.getInt(prefix + "Z");
                Location location = null;
                if (world != null) {
                    location = new Location(world, x, y, z);
                }

                String editName = areas.getString(prefix + "EditName");
                String rankingType = areas.getString(prefix + "RankingType");
                int ranking = areas.getInt(prefix + "Ranking");

                RankingSkull rankingSkull = new RankingSkull(editName, rankingType, ranking, location);
                rankingSkullList.add(rankingSkull);
            }
            return rankingSkullList;
        }
    }

    public static void addRankingSkull(String editName, String rankingType, int ranking, Location location) { // 增加一个排行头颅
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().addRankingSkull(editName, rankingType, ranking, location);
                } else {
                    String prefix = "RankingSkulls." + editName + ".";
                    areas.set(prefix + "EditName", editName);
                    areas.set(prefix + "Ranking", ranking);
                    areas.set(prefix + "RankingType", rankingType);
                    areas.set(prefix + "World", location.getWorld().getName());
                    areas.set(prefix + "X", location.getX());
                    areas.set(prefix + "Y", location.getY());
                    areas.set(prefix + "Z", location.getZ());
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void removeRankingSkull(String editName) { // 删除一个排行头颅
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().removeRankingSkull(editName);
                } else {
                    areas.set("RankingSkulls." + editName, null);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static List<RankingSign> getRankingSignList() { // 获取所有排行木牌
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getRankingSign();
        } else {
            List<RankingSign> rankingSignList = new ArrayList<>();
            if (!areasFile.exists()) {
                return rankingSignList;
            }
            ConfigurationSection section = areas
                    .getConfigurationSection("RankingSigns");
            if (section == null) {
                return rankingSignList;
            }
            for (String key : section.getKeys(false)) {
                String prefix = "RankingSigns." + key + ".";
                String worldName = areas.getString(prefix + "World");
                World world = Bukkit.getWorld(worldName);
                double x = areas.getInt(prefix + "X");
                double y = areas.getInt(prefix + "Y");
                double z = areas.getInt(prefix + "Z");
                Location location = null;
                if (world != null) {
                    location = new Location(world, x, y, z);
                }

                String editName = areas.getString(prefix + "EditName");
                String rankingType = areas.getString(prefix + "RankingType");
                int ranking = areas.getInt(prefix + "Ranking");

                RankingSign rankingSign = new RankingSign(editName, rankingType, ranking, location);
                rankingSignList.add(rankingSign);
            }
            return rankingSignList;
        }
    }

    public static void addRankingSign(String editName, String rankingType, int ranking, Location location) { // 增加一个排行木牌
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().addRankingSign(editName, rankingType, ranking, location);
                } else {
                    String prefix = "RankingSigns." + editName + ".";
                    areas.set(prefix + "EditName", editName);
                    areas.set(prefix + "Ranking", ranking);
                    areas.set(prefix + "RankingType", rankingType);
                    areas.set(prefix + "World", location.getWorld().getName());
                    areas.set(prefix + "X", location.getX());
                    areas.set(prefix + "Y", location.getY());
                    areas.set(prefix + "Z", location.getZ());
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void removeRankingSign(String editName) { // 删除一个排行木牌
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().removeRankingSign(editName);
                } else {
                    areas.set("RankingSigns." + editName, null);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static List<String> getBlacklist() { // 获取黑名单
        useDatabase = blacklistB;
        if (useDatabase) {
            return getInstance().getMySQL().getBlacklist();
        } else {
            return blacklist.getStringList("BlackList");
        }
    }

    public static void setBlacklist(List<String> list) { // 设置黑名单
        useDatabase = blacklistB;
        if (useDatabase) {
            getInstance().getMySQL().setBlacklist(list);
        } else {
            blacklist.set("BlackList", list);
            saveBlackList();
        }
    }

    public static int getDanExp(String name) { // 获取某玩家的段位经验
        useDatabase = danB;
        if (useDatabase) {
            return getInstance().getMySQL().getDanExp(name);
        } else {
            return playerData.getInt(name + ".Exp");
        }
    }

    public static void setDanExp(String name, int exp, boolean isAsyn) { // 设置某玩家的段位经验
        useDatabase = danB;
        Action action = () -> {
            if (useDatabase) {
                getInstance().getMySQL().setDanExp(name, exp);
            } else {
                playerData.set(name + ".Exp", exp);
                savePlayerData();
            }
        };
        if (isAsyn) {
            new BukkitRunnable() {
                public void run() {
                    action.run();
                }
            }.runTaskAsynchronously(getInstance());
        } else {
            action.run();
        }
    }

    public static String getLanguageFile(String name) { // 获取某玩家的语言文件
        useDatabase = languageFileB;
        if (useDatabase) {
            return getInstance().getMySQL().getLanguageFile(name);
        } else {
            return playerData.getString(name + ".Language");
        }
    }

    public static void setLanguageFile(String name, String language,
                                       boolean isAsyn) { // 设置某玩家使用的语言文件
        useDatabase = languageFileB;
        Action action = () -> {
            if (useDatabase) {
                getInstance().getMySQL().setLanguageFile(name, language);
            } else {
                playerData.set(name + ".Language", language);
                savePlayerData();
            }
        };
        if (isAsyn) {
            new BukkitRunnable() {
                public void run() {
                    action.run();
                }
            }.runTaskAsynchronously(getInstance());
        } else {
            action.run();
        }
    }

    public static double getPoint(String name) { // 获取某玩家的单挑积分余额
        useDatabase = pointB;
        if (useDatabase) {
            return getInstance().getMySQL().getPoint(name);
        } else {
            return playerData.getDouble(name + ".Points");
        }
    }

    public static void setPoint(String name, double point, boolean isAsyn) { // 设置某玩家的单挑积分余额
        useDatabase = pointB;

        Action action = () -> {
            if (useDatabase) {
                getInstance().getMySQL().setPoint(name, point);
            } else {
                playerData.set(name + ".Points", point);
                savePlayerData();
            }
        };
        if (isAsyn) {
            new BukkitRunnable() {
                public void run() {
                    action.run();
                }
            }.runTaskAsynchronously(getInstance());
        } else {
            action.run();
        }
    }

    public static int getHistoryGood() { // 获取历史商品总数
        useDatabase = pointShopB;
        if (useDatabase) {
            return getInstance().getMySQL().getHistoryGood();
        } else {
            return shop.getInt("Num");
        }
    }

    public static List<Good> getGoodList() { // 获取所有商品
        useDatabase = pointShopB;
        if (useDatabase) {
            return getInstance().getMySQL().getGoodList();
        } else {
            List<Good> list = new ArrayList<>();
            Set<String> set = shop.getKeys(false);
            set.remove("Num");
            for (String key : set) {
                ItemStack item = shop.getItemStack(key + ".Item");
                double price = shop.getDouble(key + ".Price");
                String broadcast = shop.getString(key + ".Broadcast");
                String description = shop.getString(key + ".Description");
                int salesVolumn = shop.getInt(key + ".SalesVolume");
                String dan = shop.getString(key + ".Dan");
                List<String> commands = shop.getStringList(key + ".Commands");
                int num = Integer.parseInt(key.replace("n", ""));
                Good good = new Good(num, item, price, broadcast, description,
                        salesVolumn, dan, commands);
                list.add(good);
            }
            return list;
        }
    }

    public static void setBroadcastForGood(int num, String broadcast) { // 为某个商品设置广播信息
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL()
                            .setBroadcastForGood(num, broadcast);
                } else {
                    shop.set("n" + num + ".Broadcast", broadcast);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setDescriptionForGood(int num, String description) { // 为某个商品设置备注信息
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setDescriptionForGood(num,
                            description);
                } else {
                    shop.set("n" + num + ".Description", description);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void updateSalesVolumn(int num) { // 更新销量
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().updateSalesVolume(num);
                } else {
                    int now = shop.getInt("n" + num + ".SalesVolume");
                    shop.set("n" + num + ".SalesVolume", now + 1);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void updateHistoryGood() { // 更新历史商品总数
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().updateHistoryGood();
                } else {
                    shop.set("Num", shop.getInt("Num") + 1);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void addGood(int num, ItemStack item, double price) { // 上架一个商品
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().addGood(num, item, price);
                } else {
                    shop.set("n" + num + ".Item", item);
                    shop.set("n" + num + ".Price", price);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void removeGood(int num) { // 下架一个商品
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().removeGood(num);
                } else {
                    shop.set("n" + num, null);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static List<String> getWinRanking() { // 获取胜场排行榜
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getRanking(0);
        } else {
            return ranking.getStringList("Win");
        }
    }

    public static List<String> getKDRanking() { // 获取KD值排行榜
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getRanking(1);
        } else {
            return ranking.getStringList("KD");
        }
    }

    public static void setRanking(int type, List<String> list) { // 设置排行榜
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setRanking(type, list);
                } else {
                    if (type == 0) {
                        ranking.set("Win", list);
                    } else {
                        ranking.set("KD", list);
                    }
                    saveRanking();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static int getWins(String name) { // 获取某玩家的胜利场数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getWins(name);
        } else {
            return records.getInt(name + ".Win");
        }
    }

    public static int getLoses(String name) { // 获取某玩家的失败场数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getLoses(name);
        } else {
            return records.getInt(name + ".Lose");
        }
    }

    public static int getDraws(String name) { // 获取某玩家的平局场数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getDraws(name);
        } else {
            return records.getInt(name + ".Draw");
        }
    }

    public static int getWinningStreakTimes(String name) { // 获取某玩家的连胜数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getWinningStreakTimes(name);
        } else {
            return playerData.getInt(name + ".Winning-Streak-Times");
        }
    }

    public static int getMaxWinningStreakTimes(String name) { // 获取某玩家的最大连胜数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getMaxWinningStreakTimes(name);
        } else {
            return playerData.getInt(name + ".Max-Winning-Streak-Times");
        }
    }

    public static void setWins(String name, int value) { // 设置某玩家的胜利场数
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setWins(name, value);
                } else {
                    records.set(name + ".Win", value);
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setLoses(String name, int value) { // 设置某玩家的平局场数
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setLoses(name, value);
                } else {
                    records.set(name + ".Lose", value);
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setDraws(String name, int value) { // 设置某玩家的平局场数
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setDraws(name, value);
                } else {
                    records.set(name + ".Draw", value);
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setWinningStreakTimes(String name, int value) { // 设置某玩家的连胜次数
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setWinningStreakTimes(name, value);
                } else {
                    records.set(name + ".Winning-Streak-Times", value);
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setMaxWinningStreakTimes(String name, int value) { // 设置某玩家的最大连胜次数
        useDatabase = recordB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setMaxWinningStreakTimes(name,
                            value);
                } else {
                    records.set(name + ".Max-Winning-Streak-Times", value);
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void addRecord(String name, String date, String opponent,
                                 String server, int time, double damage, double maxDamage,
                                 int result, int startWay, int expChange, String arenaEditName) { // 新增一条记录
        useDatabase = recordB;
        if (useDatabase) {
            getInstance().getMySQL().addRecord(name, date, opponent, server,
                    time, damage, maxDamage, result, startWay, expChange,
                    arenaEditName);
        } else {
            int logWins = records.getInt(name + ".Win");
            int logLoses = records.getInt(name + ".Lose");
            int logDraws = records.getInt(name + ".Draw");
            int logGameTimes = logWins + logLoses + logDraws;
            records.set(name + ".Record." + logGameTimes + ".player", opponent);
            records.set(name + ".Record." + logGameTimes + ".time", time);
            records.set(name + ".Record." + logGameTimes + ".date", date);
            records.set(name + ".Record." + logGameTimes + ".damage", damage);
            records.set(name + ".Record." + logGameTimes + ".maxdamage",
                    maxDamage);
            if (result == 0) { // 胜利
                records.set(name + ".Record." + logGameTimes + ".isWin", true);
                records.set(name + ".Record." + logGameTimes + ".isDraw", false);
            }
            if (result == 1) { // 失败
                records.set(name + ".Record." + logGameTimes + ".isWin", false);
                records.set(name + ".Record." + logGameTimes + ".isDraw", false);
            }
            if (result == 2) {
                records.set(name + ".Record." + logGameTimes + ".isWin", false);
                records.set(name + ".Record." + logGameTimes + ".isDraw", true);
            }
            records.set(name + ".Record." + logGameTimes + ".startWay",
                    startWay);
            records.set(name + ".Record." + logGameTimes + ".expChange",
                    expChange);
            records.set(name + ".Record." + logGameTimes + ".arenaEditName",
                    arenaEditName);
            records.set(name + ".Record." + logGameTimes + ".server",
                    server);
            saveRecords();
        }
    }

    public static void initialRecordData(String name) { // 初始化比赛记录数据，仅数据库
        useDatabase = recordB;
        if (useDatabase) {
            Bukkit.getScheduler().runTaskAsynchronously(getInstance(),
                    () -> getInstance().getMySQL().initialRecordData(name));
        }
    }

    public static int getRecordNumber(String name) { // 获取某玩家比赛记录的总条数
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getRecordNumber(name);
        } else {
            int wins = records.getInt(name + ".Win");
            int loses = records.getInt(name + ".Lose");
            int draws = records.getInt(name + ".Draw");
            return wins + loses + draws;
        }
    }

    public static List<Record> getRecordList(String name) { // 获取某条比赛记录
        useDatabase = recordB;
        if (useDatabase) {
            return getInstance().getMySQL().getRecordList(name);
        } else {
            List<Record> recordList = new ArrayList<>();
            ConfigurationSection section = records.getConfigurationSection(name
                    + ".Record");
            if (section != null) {
                section.getKeys(false).forEach(subKey -> {
                    String prefix = name + ".Record." + subKey;

                    String date = records.getString(prefix + ".date");
                    String opponent = records.getString(prefix + ".player");
                    String server = records.getString(prefix + ".server");
                    int time = records.getInt(prefix + ".time");
                    int damage = records.getInt(prefix + ".damage");
                    int maxDamage = records.getInt(prefix + ".maxdamage");
                    int result;
                    if (records.getBoolean(prefix + ".isWin")) {
                        result = 0; // 胜利
                    } else {
                        if (records.getBoolean(prefix + ".isDraw")) {
                            result = 2; // 失败
                        } else {
                            result = 1; // 平局
                        }
                    }
                    int startWay = records.getInt(prefix + ".startWay");
                    int expChange = records.getInt(prefix + ".expChange");
                    String arenaEditName = records.getString(prefix
                            + ".arenaEditName");

                    Record record = new Record(name, date, opponent, server,
                            time, damage, maxDamage, result, startWay,
                            expChange, arenaEditName);
                    recordList.add(record);
                });
            }
            return recordList;
        }
    }

    public static double getEnergy(String name) { // 获取某玩家的精力值
        useDatabase = energyB;
        if (useDatabase) {
            return getInstance().getMySQL().getEnergy(name);
        } else {
            return playerData.getDouble(name + ".Energy");
        }
    }

    public static void setEnergy(String name, double energy, boolean isAsyn) { // 设置某玩家的精力值
        useDatabase = energyB;

        Action action = () -> {
            if (useDatabase) {
                getInstance().getMySQL().setEnergy(name, energy);
            } else {
                playerData.set(name + ".Energy", energy);
                savePlayerData();
            }
        };
        if (isAsyn) {
            new BukkitRunnable() {
                public void run() {
                    action.run();
                }
            }.runTaskAsynchronously(getInstance());
        } else {
            action.run();
        }
    }

    public static String getSeasonDanMessage(String danEditName) { // 获取某段位的赛季结束致语
        useDatabase = seasonB;
        if (useDatabase) {
            return getInstance().getMySQL().getSeasonDanMessage(danEditName);
        } else {
            return season.getString(danEditName + ".message");
        }
    }

    public static void setSeasonDanMessage(String danEditName, String message) { // 设置某段位的赛季结束致语
        useDatabase = seasonB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setSeasonDanMessage(danEditName,
                            message);
                } else {
                    season.set(danEditName + ".message", message);
                    saveSeason();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static List<ItemStack> getSeasonDanItemStacks(String danEditName) { // 获取某段位的赛季结束物品奖励
        useDatabase = seasonB;
        if (useDatabase) {
            return getInstance().getMySQL().getSeasonDanItemStacks(danEditName);
        } else {
            List<ItemStack> itemStacks = new ArrayList<>();
            ConfigurationSection section = season
                    .getConfigurationSection(danEditName + ".ItemStacks");
            if (section != null) {
                section.getKeys(false).forEach(
                        subKey -> {
                            ItemStack itemStack = season
                                    .getItemStack(danEditName + ".ItemStacks."
                                            + subKey);
                            itemStacks.add(itemStack);
                        });
            }
            return itemStacks;
        }
    }

    public static void setSeasonDanItemStacks(String danEditName,
                                              List<ItemStack> itemStacks) { // 设置某段位的赛季结束物品奖励
        useDatabase = seasonB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setSeasonDanItemStacks(
                            danEditName, itemStacks);
                } else {
                    season.set(danEditName + ".ItemStacks", null);
                    for (int i = 0; i < itemStacks.size(); i++) {
                        ItemStack itemStack = itemStacks.get(i);
                        season.set(danEditName + ".ItemStacks." + i, itemStack);
                    }
                    saveSeason();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static int getSeasonDanPoints(String danEditName) { // 获取某段位的赛季结束积分奖励
        useDatabase = seasonB;
        if (useDatabase) {
            return getInstance().getMySQL().getSeasonDanPoints(danEditName);
        } else {
            return season.getInt(danEditName + ".Points");
        }
    }

    public static void setSeasonDanPoints(String danEditName, int points) { // 设置某段位的赛季结束积分奖励
        useDatabase = seasonB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setSeasonDanPoints(danEditName,
                            points);
                } else {
                    season.set(danEditName + ".Points", points);
                    saveSeason();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static boolean isSeasonDanEnable(String danEditName) { // 获取某段位的告示功能是否开启
        useDatabase = seasonB;
        if (useDatabase) {
            return getInstance().getMySQL().isSeasonDanEnable(danEditName);
        } else {
            return season.getBoolean(danEditName + ".Enable");
        }
    }

    public static void setSeasonDanEnable(String danEditName, boolean enable) { // 设置某段位的告示功能是否开启
        useDatabase = seasonB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setSeasonDanEnable(danEditName,
                            enable);
                } else {
                    season.set(danEditName + ".Enable", enable);
                    saveSeason();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setDanForGood(int num, String dan) { // 为某个商品设置限制段位
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setDanForGood(num, dan);
                } else {
                    shop.set("n" + num + ".Dan", dan);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setCommandsForGood(int num, List<String> commands) { // 为某个商品设置执行指令
        useDatabase = pointShopB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setCommandsForGood(num, commands);
                } else {
                    shop.set("n" + num + ".Commands", commands);
                    saveShop();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static void setArenaKit(String editName, List<ItemStack> itemStacks) { // 设置某竞技场的Kit物品
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL().setArenaKit(editName, itemStacks);
                } else {
                    areas.set("Arenas." + editName + ".KitItem", null);
                    for (int i = 0; i < itemStacks.size(); i++) {
                        areas.set("Arenas." + editName + ".KitItem." + i,
                                itemStacks.get(i));
                    }
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public static List<ItemStack> getArenaKit(String editName) { // 获取某竞技场的Kit物品
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().getArenaKit(editName);
        } else {
            List<ItemStack> itemStacks = new ArrayList<>();
            ConfigurationSection section = areas
                    .getConfigurationSection("Arenas." + editName + ".KitItem");
            if (section == null) {
                return itemStacks;
            }
            section.getKeys(false).forEach(
                    key -> itemStacks.add(areas.getItemStack("Arenas." + editName
                            + ".KitItem." + key)));
            return itemStacks;
        }
    }

    public static boolean isArenaKitEnable(String editName) { // 获取某竞技场的KitPVP模式是否开启
        useDatabase = areaB;
        if (useDatabase) {
            return getInstance().getMySQL().isArenaKitEnable(editName);
        } else {
            return areas.getBoolean("Arenas." + editName + ".KitEnable");
        }
    }

    public static void setArenaKitEnable(String editName, boolean enable) { // 设置某竞技场的KitPVP模式是否开启
        useDatabase = areaB;
        new BukkitRunnable() {
            public void run() {
                if (useDatabase) {
                    getInstance().getMySQL()
                            .setArenaKitEnable(editName, enable);
                } else {
                    areas.set("Arenas." + editName + ".KitEnable", enable);
                    saveAreas();
                }
            }
        }.runTaskAsynchronously(getInstance());
    }

    public interface Action {
        void run();
    }
}
