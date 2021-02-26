package com.valorin.data;

public enum MySQLCMD {
    CREATE_TABLE_ARENA(
            "CREATE TABLE IF NOT EXISTS `dantiao_arena` ("
                    + "`editname` TEXT,"
                    + "`displayname` TEXT,"
                    + "`pointa` LONGBLOB,"
                    + "`pointb` LONGBLOB,"
                    + "`commandlist` LONGBLOB,"
                    + "`watchingpoint` LONGBLOB,"
                    + "`kititem` LONGBLOB,"
                    + "`kitenable` INT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_HOLOGRAM(
            "CREATE TABLE IF NOT EXISTS `dantiao_hologram` ("
                    + "`type` INT,"
                    + "`location` LONGBLOB) DEFAULT CHARACTER SET = utf8;"), //0为win 1为kd
    CREATE_TABLE_LOBBY(
            "CREATE TABLE IF NOT EXISTS `dantiao_lobby` ("
                    + "`location` LONGBLOB) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_BLACKLIST(
            "CREATE TABLE IF NOT EXISTS `dantiao_blacklist` ("
                    + "`list` LONGBLOB) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_DAN(
            "CREATE TABLE IF NOT EXISTS `dantiao_dan` ("
                    + "`name` TEXT,"
                    + "`exp` INT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_LANGUAGE(
            "CREATE TABLE IF NOT EXISTS `dantiao_language` ("
                    + "`name` TEXT,"
                    + "`language` TEXT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_POINT(
            "CREATE TABLE IF NOT EXISTS `dantiao_point` ("
                    + "`name` TEXT,"
                    + "`points` DOUBLE) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_POINTSHOP(
            "CREATE TABLE IF NOT EXISTS `dantiao_pointshop` ("
                    + "`name` TEXT,"
                    + "`itemstack` LONGBLOB,"
                    + "`price` INT,"
                    + "`broadcast` TEXT,"
                    + "`description` TEXT,"
                    + "`salesvolume` INT,"
                    + "`dan` TEXT,"
                    + "`commands` LONGBLOB) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_POINTSHOPDATA(
            "CREATE TABLE IF NOT EXISTS `dantiao_pointshopdata` ("
                    + "`history` INT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_RANKING(
            "CREATE TABLE IF NOT EXISTS `dantiao_ranking` ("
                    + "`type` INT,"
                    + "`ranking` LONGBLOB) DEFAULT CHARACTER SET = utf8;"), //0为win 1为kd
    CREATE_TABLE_RECORDDATA(
            "CREATE TABLE IF NOT EXISTS `dantiao_recorddata` ("
                    + "`name` TEXT,"
                    + "`wins` INT,`loses` INT,`draws` INT,`winningstreaktimes` INT,`maxwinningstreaktimes` INT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_RECORD(
            "CREATE TABLE IF NOT EXISTS `dantiao_record` ("
                    + "`name` TEXT,"
                    + "`date` TEXT,"
                    + "`opponent` TEXT,"
                    + "`server` TEXT,"
                    + "`time` INT,"
                    + "`damage` DOUBLE,"
                    + "`maxdamage` DOUBLE,"
                    + "`result` INT,"
                    + "`startway` INT,"
                    + "`expchange` INT,"
                    + "`arenaeditname` TEXT) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_ENERGY("CREATE TABLE IF NOT EXISTS `dantiao_energy` ("
            + "`name` TEXT,"
            + "`energy` DOUBLE) DEFAULT CHARACTER SET = utf8;"),
    CREATE_TABLE_SEASON("CREATE TABLE IF NOT EXISTS `dantiao_season` ("
            + "`dan` TEXT,"
            + "`message` TEXT,"
            + "`itemstacks` LONGBLOB,"
            + "`points` INT,"
            + "`enable` INT) DEFAULT CHARACTER SET = utf8;"); //result: 0为胜利 1为失败 2为平局

    private String command;

    MySQLCMD(String command) {
        this.command = command;
    }

    public String commandToString() {
        return command;
    }
}