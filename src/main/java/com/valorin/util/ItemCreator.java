package com.valorin.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {
    private ItemStack itemStack;

    /*
     * material必填 displayname,lore,mark选填 但要同时存在 用于背包物品
     */
    public ItemCreator(Material material, String displayName,
                       List<String> lore, String mark, boolean light) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta im;
        if (displayName != null && mark != null) {
            im = itemStack.getItemMeta();
            im.setDisplayName(displayName);
            itemStack.setItemMeta(im);
        }
        if (lore != null && mark != null) {
            im = itemStack.getItemMeta();
            List<String> list = new ArrayList<String>();
            list.add(mark);
            list.addAll(lore);
            im.setLore(list);
            itemStack.setItemMeta(im);
        }
        if (light) {
            im = itemStack.getItemMeta();
            im.addEnchant(Enchantment.LUCK, 1, true);
            if (ViaVersion.isHasItemFlagMethod()) {
                im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
            }
            itemStack.setItemMeta(im);
        }
        this.itemStack = itemStack;
    }

    /*
     * material必填 displayname,lore选填，无其他特殊要求 用于GUI物品
     */
    public ItemCreator(Material material, String displayName,
                       List<String> lore, int s, boolean light) {
        ItemStack itemStack = new ItemStack(material, 1, (short) s);
        ItemMeta im = itemStack.getItemMeta();
        ;
        if (im != null) {
            if (displayName != null) {
                im.setDisplayName(displayName);
                itemStack.setItemMeta(im);
            }
            if (lore != null) {
                im.setLore(lore);
                itemStack.setItemMeta(im);
            }
            if (light) {
                im.addEnchant(Enchantment.LUCK, 1, true);
                if (ViaVersion.isHasItemFlagMethod()) {
                    im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                    im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
                }
                itemStack.setItemMeta(im);
            }
        }
        this.itemStack = itemStack;
    }

    public ItemStack get() {
        return itemStack;
    }
}
