package com.valorin.stats;

import com.valorin.Main;
import com.valorin.stats.data.SingleLineChartData;
import com.valorin.stats.metrics.BStats;
import com.valorin.stats.metrics.CStats;
import org.bukkit.plugin.Plugin;

public class Stats {

    public Stats() {
        try {
            SingleLineChartData singleLineChartData = Main.getInstance().getSingleLineChartData();
            Plugin plugin = Main.getInstance();

            BStats bStats = new BStats(plugin, 6343);
            bStats.addCustomChart(new BStats.SingleLineChart("duel_amount",
                    () -> singleLineChartData.uploadGameTimes()));
            bStats.addCustomChart(new BStats.SingleLineChart("command_type_amount",
                    () -> singleLineChartData.uploadTypeCommandTimes()));
            bStats.addCustomChart(new BStats.SingleLineChart("commodity_number",
                    () -> singleLineChartData.uploadGoodNumber()));
            bStats.addCustomChart(new BStats.SingleLineChart("arena_number",
                    () -> singleLineChartData.uploadArenaNumber()));

            new CStats(plugin);
        } catch (Exception e) {
        }
    }
}
