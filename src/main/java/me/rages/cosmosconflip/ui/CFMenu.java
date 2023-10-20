package me.rages.cosmosconflip.ui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class CFMenu extends MenuBuilder {

    private Player creator;
    private Material userMaterial;
    private Material opponentMaterial;
    private int timer = 3;

    public CFMenu(String title, Material creatorMaterial) {
        super(title, InventoryType.HOPPER);
        this.userMaterial = creatorMaterial;
    }

    public CFMenu(String title, Material creatorMaterial, Material opponentMaterial) {
        super(title, InventoryType.HOPPER);
        this.userMaterial = creatorMaterial;
        this.opponentMaterial = opponentMaterial;
    }

    @Override
    public CFMenu init() {
        return this;
    }

    public CFMenu open(Player player) {
        player.openInventory(inventory);
        return this;
    }

    public void updateTimer() {
        inventory.setItem(2, new ItemStack(Material.LIME_STAINED_GLASS_PANE, this.timer--));
    }

    public void updateCoin(boolean state) {
        if (state) {
            inventory.setItem(2, new ItemStack(userMaterial));
        } else {
            inventory.setItem(2, new ItemStack(opponentMaterial));
        }
    }

    public void redraw() {
        inventory.setItem(0, new ItemStack(userMaterial));
        inventory.setItem(4, new ItemStack(opponentMaterial));
    }

    public Material getUserMaterial() {
        return userMaterial;
    }

    public void setOpponentMaterial(Material material) {
        this.opponentMaterial = material;
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


    public int getTimer() {
        return timer;
    }
}