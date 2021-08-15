package com.valorin.data.encapsulation;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Good {
    private final int num;
    private final ItemStack item;
    private final double price;
    private String broadcast;
    private String description;
    private int salesVolume;

    private String dan;
    private List<String> commands;

    public Good(int num, ItemStack item, double price, String broadcast, String description, int salesVolume, String dan, List<String> commands) {
        this.num = num;
        this.item = item;
        this.price = price;
        this.broadcast = broadcast;
        this.description = description;
        this.salesVolume = salesVolume;
        this.dan = dan;
        this.commands = commands;
    }

    public int getNum() {
        return num;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public double getPrice() {
        return price;
    }

    public String getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(String broadcast) {
        this.broadcast = broadcast;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDan() {
        return dan;
    }

    public void setDan(String dan) {
        this.dan = dan;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void updateSalesVolume() {
        salesVolume++;
    }

}
