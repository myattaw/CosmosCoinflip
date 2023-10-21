package me.rages.cosmosconflip.menu.impl;

import me.rages.cosmosconflip.menu.MenuBuilder;
import me.rages.cosmosconflip.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import static me.rages.cosmosconflip.util.Util.*;

public class CFViewMenu extends MenuBuilder {

    private Player user;
    private Player opponent;

    private double amount;
    private Material userMaterial;
    private Material opponentMaterial;
    private int timer = 3;

    public CFViewMenu(Player user, double amount, Material creatorMaterial) {
        super("Coin Flip Match - " + Util.getAbbreviatedCurrency(amount, true), InventoryType.HOPPER);
        this.user = user;
        this.userMaterial = creatorMaterial;
        this.amount = amount;
    }

    public CFViewMenu(String title, Material creatorMaterial, Material opponentMaterial) {
        super(title, InventoryType.HOPPER);
        this.userMaterial = creatorMaterial;
        this.opponentMaterial = opponentMaterial;
        this.amount = amount;
    }

    @Override
    public CFViewMenu init() {
        return this;
    }

    public void updateTimer() {
        inventory.setItem(2,
                setItemNameAndLore(
                        new ItemStack(Material.LIME_STAINED_GLASS_PANE, this.timer),
                        "&a&l" + timer,
                        "&7Rolling in " + timer + "s"
                )
        );
        timer -= 1;
    }

    public void updateCoin(boolean state, boolean winner) {

        if (state) {
            String displayUser = COLOR_CODE_MAP.get(userMaterial) + ChatColor.BOLD + user.getName();
            inventory.setItem(2,
                    setItemNameAndLore(
                            new ItemStack(userMaterial),
                            displayUser,
                            winner ? displayUser + " &7has won!" : "&7Rolling..."
                    )
            );
        } else {
            String displayOpponent = COLOR_CODE_MAP.get(opponentMaterial) + ChatColor.BOLD + opponent.getName();

            inventory.setItem(2,
                    setItemNameAndLore(
                            new ItemStack(opponentMaterial),
                            COLOR_CODE_MAP.get(opponentMaterial) + ChatColor.BOLD + opponent.getName(),
                            winner ? displayOpponent + " &7has won!" : "&7Rolling..."
                    )
            );
        }
    }

    public void redraw() {
        inventory.setItem(0,
                setItemNameAndLore(
                        new ItemStack(userMaterial),
                        COLOR_CODE_MAP.get(userMaterial) + ChatColor.BOLD + user.getName()
                )
        );
        inventory.setItem(4, setItemNameAndLore(
                        new ItemStack(opponentMaterial),
                        COLOR_CODE_MAP.get(opponentMaterial) + ChatColor.BOLD + opponent.getName()
                )
        );
    }

    public Player getUser() {
        return user;
    }

    public Material getUserMaterial() {
        return userMaterial;
    }

    public void setOpponent(Player opponent, Material material) {
        this.opponentMaterial = material;
        this.opponent = opponent;
        redraw();
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