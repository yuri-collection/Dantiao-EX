package com.valorin.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class ShopEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final Player buyer;
    private final int page;
    private final int row;
    private final int column;
    private final ItemStack item;

    /**
     * 玩家在单挑积分商城购物时调用（前提是有足够的积分）
     *
     * @param buyer  玩家
     * @param page   商品所在的页数
     * @param row    商品所在的行数
     * @param column 商品所在的列数
     */
    public ShopEvent(Player buyer, int page, int row, int column, ItemStack item) {
        this.buyer = buyer;
        this.page = page;
        this.row = row;
        this.column = column;
        this.item = item;
    }

    public Player getPlayer() {
        return buyer;
    }

    public int getPage() {
        return page;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getNum() {
        return (page - 1) * 36 + (row - 1) * 9 + (column - 1);
    }

    public ItemStack getItemStack() {
        return item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
