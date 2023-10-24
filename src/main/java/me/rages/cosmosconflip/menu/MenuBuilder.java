package me.rages.cosmosconflip.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class MenuBuilder<T> implements InventoryHolder {

    public Inventory inventory;
    private String title;

    public MenuBuilder(String title, InventoryType inventoryType) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this, inventoryType, getTitle());
    }

    public MenuBuilder(String title) {
        this.title = title;
        this.inventory = Bukkit.createInventory(this, 27, getTitle());
    }

    public MenuBuilder open(Player player) {
        player.closeInventory();
        player.openInventory(inventory);
        return this;
    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', title);
    }

    public abstract T init();

    public Inventory getInventory() {
        return this.inventory;
    }

    public abstract void onInventoryClick(InventoryClickEvent event);

    public abstract void onInventoryClose(InventoryCloseEvent event);

    public abstract void onInventoryOpen(InventoryOpenEvent event);


}