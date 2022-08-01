package com.valorin.data;

import com.valorin.Main;
import com.valorin.configuration.ConfigManager;
import com.valorin.data.encapsulation.Record;
import com.valorin.data.encapsulation.*;
import com.valorin.util.Debug;
import com.valorin.util.Transform;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL {
    public final String MARQUE = "Valorin";
    private boolean enable = false;
    private Connection connection;

    public boolean isEnabled() {
        return enable;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() {
        try {
            ConfigManager cm = Main.getInstance().getConfigManager();
            if (cm.isUseMySQL()) {
                String url = cm.getMySQLURL() + "?charset=utf8&useSSL=false";
                String user = cm.getMySQLUser();
                String password = cm.getMySQLPassword();
                this.connection = DriverManager.getConnection(url, user,
                        password);

                Statement statement = connection.createStatement();
                if (cm.isAreaUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_ARENA
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_HOLOGRAM
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_LOBBY
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_RANKINGSKULL
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_RANKINGSIGN
                            .commandToString());
                    Debug.send("Area相关功能确认使用MySQL储存数据",
                            "The function of Area has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Area相关功能不使用MySQL储存数据",
                            "The function of Area will not use MySQL to store data");
                }
                if (cm.isBlacklistUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_BLACKLIST
                            .commandToString());
                    Debug.send("Blacklist相关功能确认使用MySQL储存数据",
                            "The function of Blacklist has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Blacklist相关功能不使用MySQL储存数据",
                            "The function of Blacklist will not use MySQL to store data");
                }
                if (cm.isDanUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_DAN
                            .commandToString());
                    Debug.send("Dan相关功能确认使用MySQL储存数据",
                            "The function of Dan has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Dan相关功能不使用MySQL储存数据",
                            "The function of Dan will not use MySQL to store data");
                }
                if (Main.getInstance().getConfigManager().isEnergyEnabled()) {
                    if (cm.isEnergyUseMySQL()) {
                        statement.executeUpdate(MySQLCMD.CREATE_TABLE_ENERGY
                                .commandToString());
                        Debug.send("Energy相关功能确认使用MySQL储存数据",
                                "The function of Energy has confirmed to use MySQL to store data");
                    } else {
                        Debug.send("Energy相关功能不使用MySQL储存数据",
                                "The function of Energy will not use MySQL to store data");
                    }
                }
                if (cm.isLanguageFileUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_LANGUAGE
                            .commandToString());
                    Debug.send("LanguageFile相关功能确认使用MySQL储存数据",
                            "The function of LanguageFile has confirmed to use MySQL to store data");
                } else {
                    Debug.send("LanguageFile相关功能不使用MySQL储存数据",
                            "The function of LanguageFile will not use MySQL to store data");
                }
                if (cm.isPointUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_POINT
                            .commandToString());
                    Debug.send("Point相关功能确认使用MySQL储存数据",
                            "The function of Point has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Point相关功能不使用MySQL储存数据",
                            "The function of Point will not use MySQL to store data");
                }
                if (cm.isPointShopUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_POINTSHOP
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_POINTSHOPDATA
                            .commandToString());
                    Debug.send("PointShop相关功能确认使用MySQL储存数据",
                            "The function of PointShop has confirmed to use MySQL to store data");
                } else {
                    Debug.send("PointShop相关功能不使用MySQL储存数据",
                            "The function of PointShop will not use MySQL to store data");
                }
                if (cm.isRecordUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_RANKING
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_RECORDDATA
                            .commandToString());
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_RECORD
                            .commandToString());
                    Debug.send("Record相关功能确认使用MySQL储存数据",
                            "The function of Record has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Record相关功能不使用MySQL储存数据",
                            "The function of Record will not use MySQL to store data");
                }
                if (cm.isSeasonUseMySQL()) {
                    statement.executeUpdate(MySQLCMD.CREATE_TABLE_SEASON
                            .commandToString());
                    Debug.send("Season相关功能确认使用MySQL储存数据",
                            "The function of Season has confirmed to use MySQL to store data");
                } else {
                    Debug.send("Season相关功能不使用MySQL储存数据",
                            "The function of Season will not use MySQL to store data");
                }
                statement.close();

                enable = true;
                Debug.send("成功连接数据库", "Success to connect the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Debug.send("未能成功连接数据库", "Failed to connect the database");
        }
    }

    public void close() {
        try {
            if (enable) {
                connection.close();
                Debug.send("与数据库断开连接", "The connection of the database closed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet(String command) {
        try {
            return connection.createStatement().executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getArenas() { // 获取所有竞技场的编辑名
        List<String> arenas = new ArrayList<>();
        try {
            ResultSet rs = getResultSet("select * from dantiao_arena;");
            while (rs.next()) {
                arenas.add(rs.getString("editname"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arenas;
    }

    public String getArenaDisplayName(String editName) { // 获取某个竞技场的展示名
        String displayName = null;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                displayName = rs.getString("displayname");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return displayName;
    }

    public Location getArenaPointA(String editName) { // 获取某个竞技场的A点
        Location location = null;
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("pointa");
                DataMedium dataMedium = (DataMedium) (Transform
                        .serializeToObject(blob));
                location = dataMedium.getLocation();
            }
            rs.close();
            ps.close();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return location;
    }

    public Location getArenaPointB(String editName) { // 获取某个竞技场的B点
        Location location = null;
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("pointb");
                DataMedium dataMedium = (DataMedium) (Transform
                        .serializeToObject(blob));
                location = dataMedium.getLocation();
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public List<String> getArenaCommands(String editName) { // 获取某个竞技场的指令组
        List<String> list = new ArrayList<>();
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("commandlist");
                if (blob != null) {
                    List<?> rawList = (ArrayList<?>) (Transform
                            .serializeToObject(blob));
                    for (Object o : rawList) {
                        if (o instanceof String) {
                            list.add((String) o);
                        }
                    }
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Location getArenaWatchingPoint(String editName) { // 获取某个竞技场的观战点
        Location location = null;
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("watchingpoint");
                if (blob != null) {
                    DataMedium dataMedium = (DataMedium) (Transform
                            .serializeToObject(blob));
                    location = dataMedium.getLocation();
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setArenaCommands(String editName, List<String> list) { // 设置某竞技场的指令组
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_arena set commandlist = ? where editname = ?;");
                ps2.setBlob(1, Transform.serialize(list));
                ps2.setString(2, editName);
                ps2.executeUpdate();
                ps2.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setArenaWatchingPoint(String editName, Location location) { // 设置某竞技场的观战点
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `editname` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                if (location == null) {
                    ps2 = connection
                            .prepareStatement("update dantiao_arena set watchingpoint = null where editname = ?;");
                    ps2.setString(1, editName);
                } else {
                    ps2 = connection
                            .prepareStatement("update dantiao_arena set watchingpoint = ? where editname = ?;");
                    DataMedium dataMedium = new DataMedium();
                    dataMedium.setLocation(location);
                    ps2.setBlob(1, Transform.serialize(dataMedium));
                    ps2.setString(2, editName);
                }
                ps2.executeUpdate();
                ps2.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveArena(String editName, String displayName, Location pointA,
                          Location pointB) { // 保存一个已编辑完的竞技场
        try {
            PreparedStatement ps = connection
                    .prepareStatement("insert into dantiao_arena ("
                            + "editname,displayname,pointa,pointb,commandlist) value(?,?,?,?,?);");
            ps.setString(1, editName);
            ps.setString(2, displayName);
            DataMedium dataMedium = new DataMedium();
            dataMedium.setLocation(pointA);
            ps.setBlob(3, Transform.serialize(dataMedium));
            dataMedium.setLocation(pointB);
            ps.setBlob(4, Transform.serialize(dataMedium));
            ps.setBlob(5, Transform.serialize(new ArrayList<String>()));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteArena(String editName) { // 删除某个竞技场
        try {
            PreparedStatement ps = connection
                    .prepareStatement("delete from dantiao_arena where `editname` = ?");
            ps.setString(1, editName);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Location getHologramLocation(int type) { // 获取排行榜全息图的所在位置
        Location location = null;
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_hologram where `type` = ? limit 1;");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("location");
                if (blob != null) {
                    DataMedium dataMedium = (DataMedium) (Transform
                            .serializeToObject(blob));
                    location = dataMedium.getLocation();
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setHologramLocation(int type, Location location) { // 设置某排行榜全息图的所在位置
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_hologram where `type` = ? limit 1;");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                if (location == null) {
                    ps2 = connection
                            .prepareStatement("update dantiao_hologram set location = null where type = ?;");
                    ps2.setInt(1, type);
                } else {
                    ps2 = connection
                            .prepareStatement("update dantiao_hologram set location = ? where type = ?;");
                    DataMedium dataMedium = new DataMedium();
                    dataMedium.setLocation(location);
                    ps2.setBlob(1, Transform.serialize(dataMedium));
                    ps2.setInt(2, type);
                }
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_hologram (type, location) values(?,?);");
                DataMedium dataMedium = new DataMedium();
                dataMedium.setLocation(location);
                ps2.setInt(1, type);
                ps2.setBlob(2, Transform.serialize(dataMedium));
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public Location getLobbyLocation() { // 获取大厅传送点的所在位置
        Location location = null;
        try {
            Blob blob;
            ResultSet rs = getResultSet("select * from dantiao_lobby;");
            if (rs.next()) {
                blob = rs.getBlob("location");
                if (blob != null) {
                    DataMedium dataMedium = (DataMedium) (Transform
                            .serializeToObject(blob));
                    location = dataMedium.getLocation();
                }
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return location;
    }

    public void setLobbyLocation(Location location) { // 设置大厅传送点的位置
        boolean exist = false;
        try {
            ResultSet rs = getResultSet("select * from dantiao_lobby;");
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            PreparedStatement ps;
            if (exist) {
                if (location == null) {
                    ps = connection
                            .prepareStatement("delete from dantiao_lobby;");
                } else {
                    ps = connection
                            .prepareStatement("update dantiao_lobby set location = ?;");
                    DataMedium dataMedium = new DataMedium();
                    dataMedium.setLocation(location);
                    ps.setBlob(1, Transform.serialize(dataMedium));
                }
            } else {
                ps = connection
                        .prepareStatement("insert into dantiao_lobby (location) values(?);");
                DataMedium dataMedium = new DataMedium();
                dataMedium.setLocation(location);
                ps.setBlob(1, Transform.serialize(dataMedium));
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<RankingSkull> getRankingSkull() { // 获取所有排行头颅
        List<RankingSkull> rankingSkullList = new ArrayList<>();
        try {
            Blob blob;
            Location location = null;
            ResultSet rs = getResultSet("select * from dantiao_rankingskull;");
            while (rs.next()) {
                String editName = rs.getString("name");
                String rankingType = rs.getString("rankingtype");
                int ranking = rs.getInt("ranking");
                blob = rs.getBlob("location");
                if (blob != null) {
                    DataMedium dataMedium = (DataMedium) (Transform
                            .serializeToObject(blob));
                    location = dataMedium.getLocation();
                }
                RankingSkull rankingSkull = new RankingSkull(editName, rankingType, ranking, location);
                rankingSkullList.add(rankingSkull);
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return rankingSkullList;
    }

    public void addRankingSkull(String editName, String rankingType, int ranking, Location location) { // 新增一个排行头颅
        try {
            PreparedStatement ps = connection
                    .prepareStatement("insert into dantiao_rankingskull (name,rankingtype,ranking,location) value(?,?,?,?);");
            ps.setString(1, editName);
            ps.setString(2, rankingType);
            ps.setInt(3, ranking);
            DataMedium dataMedium = new DataMedium();
            dataMedium.setLocation(location);
            ps.setBlob(4, Transform.serialize(dataMedium));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeRankingSkull(String editName) { // 删除一个排行头颅
        try {
            PreparedStatement ps = connection
                    .prepareStatement("delete from dantiao_rankingskull where `name` = ?;");
            ps.setString(1, editName);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<RankingSign> getRankingSign() { // 获取所有排行木牌
        List<RankingSign> rankingSignList = new ArrayList<>();
        try {
            Blob blob1, blob2;
            Location location = null;
            List<String> text = new ArrayList<>();
            ResultSet rs = getResultSet("select * from dantiao_rankingsign;");
            while (rs.next()) {
                String editName = rs.getString("name");
                String rankingType = rs.getString("rankingtype");
                int ranking = rs.getInt("ranking");
                blob1 = rs.getBlob("location");
                if (blob1 != null) {
                    DataMedium dataMedium = (DataMedium) (Transform
                            .serializeToObject(blob1));
                    location = dataMedium.getLocation();
                }
                blob2 = rs.getBlob("origintext");
                if (blob2 != null) {
                    List<?> rawRanking = (List<?>) Transform
                            .serializeToObject(blob2);
                    for (Object o : rawRanking) {
                        if (o instanceof String) {
                            text.add((String) o);
                        }
                    }
                }
                RankingSign rankingSign = new RankingSign(editName, rankingType, ranking, location, text);
                rankingSignList.add(rankingSign);
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return rankingSignList;
    }

    public void addRankingSign(String editName, String rankingType, int ranking, Location location, List<String> text) { // 新增一个排行木牌
        try {
            PreparedStatement ps = connection
                    .prepareStatement("insert into dantiao_rankingsign (name,rankingtype,ranking,location,origintext) value(?,?,?,?,?);");
            ps.setString(1, editName);
            ps.setString(2, rankingType);
            ps.setInt(3, ranking);
            DataMedium dataMedium = new DataMedium();
            dataMedium.setLocation(location);
            ps.setBlob(4, Transform.serialize(dataMedium));
            ps.setBlob(5, Transform.serialize(text));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeRankingSign(String editName) { // 删除一个排行木牌
        try {
            PreparedStatement ps = connection
                    .prepareStatement("delete from dantiao_rankingsign where `name` = ?;");
            ps.setString(1, editName);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getBlacklist() { // 获取黑名单
        List<String> blacklist = new ArrayList<>();
        try {
            Blob blob;
            ResultSet rs = getResultSet("select * from dantiao_blacklist limit 1;");
            if (rs.next()) {
                blob = rs.getBlob("list");
                List<?> rawRanking = (ArrayList<?>) (Transform
                        .serializeToObject(blob));
                for (Object o : rawRanking) {
                    if (o instanceof String) {
                        blacklist.add((String) o);
                    }
                }
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return blacklist;
    }

    public void setBlacklist(List<String> list) { // 设置黑名单
        boolean exist = false;
        try {
            ResultSet rs = getResultSet("select * from dantiao_blacklist limit 1;");
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            PreparedStatement ps;
            if (exist) {
                ps = connection
                        .prepareStatement("update dantiao_blacklist set list = ?;");
            } else {
                ps = connection
                        .prepareStatement("insert into dantiao_blacklist (list) value(?);");
            }
            ps.setBlob(1, Transform.serialize(list));
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getDanExp(String name) { // 获取某玩家的段位经验
        int exp = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_dan where `name` = ? limit 1;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exp = rs.getInt("exp");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exp;
    }

    public void setDanExp(String name, int exp) { // 设置某玩家的段位经验
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_dan where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_dan set exp = ? where name = ?;");
                ps2.setInt(1, exp);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_dan (name,exp) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, exp);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getLanguageFile(String name) { // 获取某玩家的语言文件
        String language = null;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_language where `name` = ? limit 1;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                language = rs.getString("language");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return language;
    }

    public void setLanguageFile(String name, String language) { // 设置某玩家使用的语言文件
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_language where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_language set language = ? where name = ?;");
                ps2.setString(1, language);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_language (name,language) value(?,?);");
                ps2.setString(1, name);
                ps2.setString(2, language);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getPoint(String name) { // 获取某玩家的单挑积分余额
        double points = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_point where `name` = ? limit 1;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                points = rs.getDouble("points");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public void setPoint(String name, double point) { // 设置某玩家的单挑积分余额
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_point where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_point set points = ? where name = ?;");
                ps2.setDouble(1, point);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_point value(?,?);");
                ps2.setString(1, name);
                ps2.setDouble(2, point);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getHistoryGood() { // 获取历史商品总数
        int value = 0;
        try {
            ResultSet rs = getResultSet("select * from dantiao_pointshopdata;");
            while (rs.next()) {
                value = rs.getInt("history");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    public List<Good> getGoodList() { // 获取所有商品的代号
        List<Good> goodList = new ArrayList<>();
        try {
            ResultSet rs = getResultSet("select * from dantiao_pointshop;");
            while (rs.next()) {
                int num = Integer.parseInt(rs.getString("name")
                        .replace("n", ""));
                YamlConfiguration yaml = (YamlConfiguration) Transform
                        .streamToYaml(rs.getBinaryStream("itemstack"));
                ItemStack itemStack = yaml.getItemStack(MARQUE);
                double price = rs.getDouble("price");
                String broadcast = rs.getString("broadcast");
                String description = rs.getString("description");
                int salesVolume = rs.getInt("salesvolume");
                String dan = rs.getString("dan");

                List<String> commands = new ArrayList<>();
                Blob blob = rs.getBlob("commands");
                if (blob != null) {
                    List<?> rawCommands = (ArrayList<?>) (Transform
                            .serializeToObject(blob));
                    for (Object o : rawCommands) {
                        if (o instanceof String) {
                            commands.add((String) o);
                        }
                    }
                }

                Good good = new Good(num, itemStack, price, broadcast,
                        description, salesVolume, dan, commands);
                goodList.add(good);
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return goodList;
    }

    public void setBroadcastForGood(int num, String broadcast) { // 为某个商品设置广播信息
        try {
            PreparedStatement ps = connection
                    .prepareStatement("update dantiao_pointshop set broadcast = ? where name = ?;");
            if (broadcast == null) {
                ps.setString(1, "null");
            } else {
                ps.setString(1, broadcast);
            }
            ps.setString(2, "n" + num);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDescriptionForGood(int num, String description) { // 为某个商品设置备注信息
        try {
            PreparedStatement ps = connection
                    .prepareStatement("update dantiao_pointshop set description = ? where name = ?;");
            if (description == null) {
                ps.setString(1, "null");
            } else {
                ps.setString(1, description);
            }
            ps.setString(2, "n" + num);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSalesVolume(int num) { // 更新销量
        try {
            int now = 0;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_pointshop where name = ? limit 1;");
            ps.setString(1, "n" + num);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                now = rs.getInt("salesvolume");
            }
            PreparedStatement ps2 = connection
                    .prepareStatement("update dantiao_pointshop set salesvolume = ? where name = ?;");
            ps2.setInt(1, now + 1);
            ps2.setString(2, "n" + num);
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateHistoryGood() { // 更新历史商品总数
        try {
            boolean exist = false;
            ResultSet rs = getResultSet("select history from dantiao_pointshopdata;");
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            PreparedStatement ps2;
            if (exist) {
                int now = 0;
                ResultSet rs2 = getResultSet("select * from dantiao_pointshopdata;");
                while (rs2.next()) {
                    now = rs2.getInt("history");
                }
                rs2.close();
                ps2 = connection
                        .prepareStatement("update dantiao_pointshopdata set history = ?;");
                ps2.setInt(1, now + 1);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_pointshopdata (history) value(?);");
                ps2.setInt(1, 1);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addGood(int num, ItemStack item, double price) { // 上架一个商品
        try {
            PreparedStatement ps = connection
                    .prepareStatement("insert into dantiao_pointshop (name,itemstack,price) value(?,?,?);");
            ps.setString(1, "n" + num);
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set(MARQUE, item);
            ps.setBinaryStream(2, Transform.yamlToStream(yaml));
            ps.setDouble(3, price);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void removeGood(int num) { // 下架一个商品
        try {
            PreparedStatement ps = connection
                    .prepareStatement("delete from dantiao_pointshop where `name` = ?;");
            ps.setString(1, "n" + num);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRanking(int type) { // 获取某个排行榜
        List<String> ranking = new ArrayList<>();
        try {
            Blob blob;
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_ranking where `type` = ?;");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                blob = rs.getBlob("ranking");
                List<?> rawRanking = (ArrayList<?>) (Transform
                        .serializeToObject(blob));
                for (Object o : rawRanking) {
                    if (o instanceof String) {
                        ranking.add((String) o);
                    }
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return ranking;
    }

    public void setRanking(int type, List<String> ranking) { // 设置某个排行榜
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_ranking where `type` = ?;");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            Blob blob = Transform.serialize(ranking);
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_ranking set ranking = ? where type = ?;");
                ps2.setBlob(1, blob);
                ps2.setInt(2, type);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_ranking (type,ranking) value(?,?);");
                ps2.setInt(1, type);
                ps2.setBlob(2, blob);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getWins(String name) { // 获取某玩家的胜利场数
        int wins = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select wins from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                wins = rs.getInt("wins");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wins;
    }

    public int getLoses(String name) { // 获取某玩家的失败场数
        int loses = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                loses = rs.getInt("loses");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loses;
    }

    public int getDraws(String name) { // 获取某玩家的平局场数
        int draws = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                draws = rs.getInt("draws");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return draws;
    }

    public int getWinningStreakTimes(String name) { // 获取某玩家的连胜数
        int times = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                times = rs.getInt("winningstreaktimes");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

    public int getMaxWinningStreakTimes(String name) { // 获取某玩家的最大连胜数
        int times = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                times = rs.getInt("maxwinningstreaktimes");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return times;
    }

    public void setWins(String name, int value) { // 设置某玩家的胜场数
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_recorddata set wins = ? where name = ?;");
                ps2.setInt(1, value);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,wins) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, value);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLoses(String name, int value) { // 设置某玩家的败场数
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_recorddata set loses = ? where name = ?;");
                ps2.setInt(1, value);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,loses) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, value);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDraws(String name, int value) { // 设置某玩家的平局数
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_recorddata set draws = ? where name = ?;");
                ps2.setInt(1, value);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,draws) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, value);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setWinningStreakTimes(String name, int value) { // 设置某玩家的连胜次数
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_recorddata set winningstreaktimes = ? where name = ?;");
                ps2.setInt(1, value);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,winningstreaktimes) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, value);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setMaxWinningStreakTimes(String name, int value) { // 设置某玩家的连胜次数
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_recorddata set maxwinningstreaktimes = ? where name = ?;");
                ps2.setInt(1, value);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,maxwinningstreaktimes) value(?,?);");
                ps2.setString(1, name);
                ps2.setInt(2, value);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRecord(String name, String date, String opponent,
                          String server, int time, double damage, double maxDamage,
                          int result, int startWay, int expChange, String arenaEditName) { // 新增一条记录
        try {
            PreparedStatement ps = connection
                    .prepareStatement("insert into dantiao_record "
                            + "(name,date,opponent,server,time,damage,maxdamage,result,startway,expchange,arenaeditname) "
                            + "value(?,?,?,?,?,?,?,?,?,?,?);");
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setString(3, opponent);
            ps.setString(4, server);
            ps.setInt(5, time);
            ps.setDouble(6, damage);
            ps.setDouble(7, maxDamage);
            ps.setInt(8, result);
            ps.setInt(9, startWay);
            ps.setInt(10, expChange);
            ps.setString(11, arenaEditName);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialRecordData(String name) { // 初始化比赛记录数据
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_recorddata where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (!exist) {
                ps2 = connection
                        .prepareStatement("insert into dantiao_recorddata (name,wins,loses,draws,winningstreaktimes,maxwinningstreaktimes) value(?,0,0,0,0,0);");
                ps2.setString(1, name);
                ps2.executeUpdate();
                ps2.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getRecordNumber(String name) { // 获取某玩家比赛记录的总条数
        int number = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select name from dantiao_record where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                number++;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return number;
    }

    public List<Record> getRecordList(String name) { // 获取某玩家的所有比赛记录
        List<Record> recordList = new ArrayList<>();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_record where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                String opponent = rs.getString("opponent");
                String server = rs.getString("server");
                int time = rs.getInt("time");
                double damage = rs.getDouble("damage");
                double maxDamage = rs.getDouble("maxdamage");
                int result = rs.getInt("result");
                int startWay = rs.getInt("startway");
                int expChange = rs.getInt("expchange");
                String arenaEditName = rs.getString("arenaeditname");
                Record record = new Record(name, date, opponent, server, time,
                        damage, maxDamage, result, startWay, expChange,
                        arenaEditName);
                recordList.add(record);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recordList;
    }

    public double getEnergy(String name) { // 获取某玩家的精力值
        double points = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_energy where `name` = ? limit 1;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                points = rs.getDouble("energy");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public void setEnergy(String name, double energy) { // 设置某玩家的精力值
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_energy where `name` = ?;");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_energy set energy = ? where name = ?;");
                ps2.setDouble(1, energy);
                ps2.setString(2, name);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_energy (name,energy) value(?,?);");
                ps2.setString(1, name);
                ps2.setDouble(2, energy);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getSeasonDanMessage(String danEditName) { // 获取某段位的赛季结束致语
        String message = null;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ? limit 1;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                message = rs.getString("message");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void setSeasonDanMessage(String danEditName, String message) { // 设置某段位的赛季结束致语
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ?;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_season set message = ? where dan = ?;");
                ps2.setString(1, message);
                ps2.setString(2, danEditName);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_season (dan,message) value(?,?);");
                ps2.setString(1, danEditName);
                ps2.setString(2, message);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemStack> getSeasonDanItemStacks(String danEditName) { // 获取某段位的赛季结束物品奖励
        List<ItemStack> itemStacks = new ArrayList<>();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ? limit 1;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                YamlConfiguration yaml = (YamlConfiguration) Transform
                        .streamToYaml(rs.getBinaryStream("itemstacks"));
                ConfigurationSection section = yaml
                        .getConfigurationSection(MARQUE);
                if (section != null) {
                    section.getKeys(false).forEach(
                            subKey -> {
                                ItemStack itemStack = yaml.getItemStack(MARQUE
                                        + "." + subKey);
                                itemStacks.add(itemStack);
                            });
                }
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return itemStacks;
    }

    public void setSeasonDanItemStacks(String danEditName,
                                       List<ItemStack> itemStacks) { // 设置某段位的赛季结束物品奖励
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ?;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            YamlConfiguration yaml = new YamlConfiguration();
            for (int i = 0; i < itemStacks.size(); i++) {
                ItemStack itemStack = itemStacks.get(i);
                yaml.set(MARQUE + "." + i, itemStack);
            }
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_season set itemstacks = ? where dan = ?;");
                ps.setBinaryStream(1, Transform.yamlToStream(yaml));
                ps.setString(2, danEditName);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_season (dan,itemstacks) value(?,?);");
                ps2.setString(1, danEditName);
                ps2.setBinaryStream(2, Transform.yamlToStream(yaml));
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public int getSeasonDanPoints(String danEditName) { // 获取某段位的赛季结束积分奖励
        int points = 0;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ? limit 1;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                points = rs.getInt("points");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }

    public void setSeasonDanPoints(String danEditName, int points) { // 设置某段位的赛季结束积分奖励
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ?;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_season set points = ? where dan = ?;");
                ps2.setInt(1, points);
                ps2.setString(2, danEditName);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_season (dan,points) value(?,?);");
                ps2.setString(1, danEditName);
                ps2.setInt(2, points);
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isSeasonDanEnable(String danEditName) { // 获取某段位的告示功能是否开启
        boolean enable = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ? limit 1;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("enable") == 0) {
                    enable = true;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enable;
    }

    public void setSeasonDanEnable(String danEditName, boolean enable) { // 设置某段位的告示功能是否开启
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_season where `dan` = ?;");
            ps.setString(1, danEditName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_season set enable = ? where dan = ?;");
                if (enable) {
                    ps2.setInt(1, 0);
                } else {
                    ps2.setInt(1, 1);
                }
                ps2.setString(2, danEditName);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_season (dan,enable) value(?,?);");
                ps2.setString(1, danEditName);
                if (enable) {
                    ps2.setInt(2, 0);
                } else {
                    ps2.setInt(2, 1);
                }
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDanForGood(int num, String dan) { // 为某个商品设置限制段位
        try {
            PreparedStatement ps = connection
                    .prepareStatement("update dantiao_pointshop set dan = ? where name = ?;");
            if (dan == null) {
                ps.setString(1, "null");
            } else {
                ps.setString(1, dan);
            }
            ps.setString(2, "n" + num);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCommandsForGood(int num, List<String> commands) { // 设置某个商品的执行指令
        try {
            PreparedStatement ps = connection
                    .prepareStatement("update dantiao_pointshop set commands = ? where name = ?;");
            ps.setBlob(1, Transform.serialize(commands));
            ps.setString(2, "n" + num);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setArenaKit(String editName, List<ItemStack> itemStacks) { // 设置某竞技场的Kit物品
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `kititem` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            YamlConfiguration yaml = new YamlConfiguration();
            for (int i = 0; i < itemStacks.size(); i++) {
                ItemStack itemStack = itemStacks.get(i);
                yaml.set(MARQUE + "." + i, itemStack);
            }
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_arena set kititem = ? where editname = ?;");
                ps2.setBinaryStream(1, Transform.yamlToStream(yaml));
                ps2.setString(2, editName);
                ps2.executeUpdate();
                ps2.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public List<ItemStack> getArenaKit(String editName) { // 获取某竞技场的Kit物品
        List<ItemStack> itemStacks = new ArrayList<>();
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `kititem` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                YamlConfiguration yaml = (YamlConfiguration) Transform
                        .streamToYaml(rs.getBinaryStream("itemstacks"));
                ConfigurationSection section = yaml
                        .getConfigurationSection(MARQUE);
                if (section != null) {
                    section.getKeys(false).forEach(
                            subKey -> {
                                ItemStack itemStack = yaml.getItemStack(MARQUE
                                        + "." + subKey);
                                itemStacks.add(itemStack);
                            });
                }
            }
            rs.close();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return itemStacks;
    }

    public boolean isArenaKitEnable(String editName) { // 获取某竞技场的KitPVP模式是否开启
        boolean enable = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `kitenable` = ? limit 1;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("kitenable") == 0) {
                    enable = true;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enable;
    }

    public void setArenaKitEnable(String editName, boolean enable) { // 设置某竞技场的KitPVP模式是否开启
        boolean exist = false;
        try {
            PreparedStatement ps = connection
                    .prepareStatement("select * from dantiao_arena where `kitenable` = ?;");
            ps.setString(1, editName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                exist = true;
            }
            rs.close();
            ps.close();
            PreparedStatement ps2;
            if (exist) {
                ps2 = connection
                        .prepareStatement("update dantiao_arena set kitenable = ? where editname = ?;");
                ps2.setString(2, editName);
                if (enable) {
                    ps2.setInt(1, 0);
                } else {
                    ps2.setInt(1, 1);
                }
                ps2.setString(2, editName);
            } else {
                ps2 = connection
                        .prepareStatement("insert into dantiao_arena (editname,kitenable) value(?,?);");
                ps2.setString(1, editName);
                if (enable) {
                    ps2.setInt(2, 0);
                } else {
                    ps2.setInt(2, 1);
                }
            }
            ps2.executeUpdate();
            ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
