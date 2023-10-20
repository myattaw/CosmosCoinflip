package me.rages.cosmosconflip.ui;

import me.rages.cosmosconflip.CoinflipPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ColorMenu extends MenuBuilder {

    public ColorMenu(String title, InventoryType inventoryType) {
        super(title, inventoryType);
    }

    @Override
    public ColorMenu init() {
        int i = 0;
        for (Material material : CoinflipPlugin.COLOR_TYPES) {
            inventory.setItem(i++, new ItemStack(material));
        }
        return this;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {

    }
}
