package com.valorin.event;

import com.valorin.Main;
import com.valorin.event.game.*;
import com.valorin.event.gui.EventRecordsGUI;
import com.valorin.event.gui.EventShopGUI;
import com.valorin.event.gui.EventStartGUI;
import com.valorin.event.ranking.EventClickRankingSign;
import com.valorin.event.ranking.EventClickRankingSkull;
import com.valorin.event.requests.EventLeaveServer;
import com.valorin.event.sign.EventClickSign;
import com.valorin.event.sign.EventCreateSign;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class TheEventRegister {
    public static void registerEvents() {
        Listener[] listeners = {new EventEndGame(), new EventProtection(), new EventPVP(),
                new EventTeleport(), new EventArenaCreate(), new EventChat(),
                new EventRecordsGUI(), new EventShopGUI(), new EventStartGUI(),
                new EventTypeCommands(), new EventMove(), new EventJoinServer(),
                new EventLeaveServer(), new EventClickSign(), new EventCreateSign(),
                new EventCommandTypeAmountStatistics(), new EventCompulsoryTeleport(),
                new EventClickPlayer(), new EventSaveSeasonRewardItems(),
                new EventInterruptSearching(), new EventSaveArenaKitItems(),
                new EventInterruptAutoTeleport(), new EventDropItemInKitPVPMode(),
                new EventCheckVersion(), new EventOpenChest(), new EventInteract(),
                new EventClickRankingSkull(), new EventClickRankingSign()};
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener,
                    Main.getInstance());
        }
    }
}
