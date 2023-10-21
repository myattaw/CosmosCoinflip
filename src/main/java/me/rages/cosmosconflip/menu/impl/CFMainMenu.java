package me.rages.cosmosconflip.menu.impl;

import me.rages.cosmosconflip.menu.MenuBuilder;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class CFMainMenu extends MenuBuilder {

    public CFMainMenu(String title) {
        super(title);
    }

    @Override
    public CFMainMenu init() {
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
