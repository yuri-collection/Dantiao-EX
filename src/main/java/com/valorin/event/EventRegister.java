package com.valorin.event;

import com.valorin.Main;
import com.valorin.event.game.*;
import com.valorin.event.gui.RecordsGUI;
import com.valorin.event.gui.ShopGUI;
import com.valorin.event.gui.StartGUI;
import com.valorin.event.requests.LeaveServer;
import com.valorin.event.sign.ClickSign;
import com.valorin.event.sign.CreateSign;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class EventRegister {
    public static void registerEvents() {
        Listener[] listeners = {new EndGame(), new Protection(), new PVP(),
                new Teleport(), new ArenaCreate(), new Chat(),
                new RecordsGUI(), new ShopGUI(), new StartGUI(),
                new TypeCommands(), new Move(), new JoinServer(),
                new LeaveServer(), new ClickSign(), new CreateSign(),
                new CommandTypeAmountStatistics(), new CompulsoryTeleport(),
                new ClickPlayer(), new SaveSeasonRewardItems(),
                new InterruptSearching(), new SaveArenaKitItems(),
                new InterruptAutoTeleport(), new DropItemInKitPVPMode()};
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener,
                    Main.getInstance());
        }
    }
}
