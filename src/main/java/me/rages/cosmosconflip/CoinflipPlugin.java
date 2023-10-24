package me.rages.cosmosconflip;

import me.rages.cosmosconflip.commands.ConflipCommand;
import me.rages.cosmosconflip.menu.MenuBuilder;
import me.rages.cosmosconflip.menu.impl.CFMainMenu;
import me.rages.cosmosconflip.menu.impl.CFViewMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class CoinflipPlugin extends JavaPlugin implements Listener {

    public static CoinflipPlugin plugin;
    public List<CoinFlipMatch> coinFlipMatchList = new ArrayList<>();
    private CFMainMenu cfMainMenu;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new ConflipCommand(this);
        getServer().getPluginManager().registerEvents(this, this);
        this.cfMainMenu = new CFMainMenu(this).init();
    }

    @Override
    public void onDisable() {
        // give back all the money to all players

        for (CoinFlipMatch matches : coinFlipMatchList) {

        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) event.getInventory().getHolder();
            menuBuilder.onInventoryClick(event);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) event.getInventory().getHolder();
            menuBuilder.onInventoryOpen(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof MenuBuilder) {
            MenuBuilder menuBuilder = (MenuBuilder) event.getInventory().getHolder();
            menuBuilder.onInventoryClose(event);
        }
    }

    public CoinflipPlugin() {
        plugin = this;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static class CoinFlipMatch {
        private Player creator;
        private Material creatorMaterial;
        private Player opponent;

        private CFViewMenu creatorMenu;
        private CFViewMenu opponentMenu;
        private int ignored;
        private double amount;

        private static final Random random = new Random();

        private Stack<Boolean> flips = new Stack<>();


        CoinFlipMatch(Player creator, CFViewMenu creatorMenu, int ignored) {
            this.creator = creator;
            this.creatorMenu = creatorMenu;
            this.ignored = ignored;
            this.amount = creatorMenu.getAmount();
            this.creatorMaterial = creatorMenu.getUserMaterial();
        }

        public static CoinFlipMatch create(Player creator, CFViewMenu creatorMenu, int ignored) {
            return new CoinFlipMatch(creator, creatorMenu, ignored);
        }

        public boolean coinFlip() {
            return random.nextBoolean();
        }

        public void startGame(Player opponent, CFViewMenu opponentMenu) {
            this.opponent = opponent;
            this.opponentMenu = opponentMenu;

            boolean coin = coinFlip();
            flips.push(coin);

            boolean filler = coinFlip();
            for (int i = 0; i < (random.nextInt(10) + 25); i++) {
                flips.push(filler);
                filler = !filler;
            }

            AtomicLong startTime = new AtomicLong(0);
            AtomicInteger taskID = new AtomicInteger(-1);

            taskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (creatorMenu.getTimer() >= 0) {
                    if (hasTimeElapsed(startTime.get(), 1, TimeUnit.SECONDS)) {
                        startTime.set(System.currentTimeMillis());

                        creatorMenu.updateTimer();
                        opponentMenu.updateTimer();

                        creatorMenu.redraw();
                        opponentMenu.redraw();
                    }
                } else {
                    if (!flips.isEmpty()) {
                        if (hasTimeElapsed(startTime.get(), 100, TimeUnit.MILLISECONDS)) {
                            startTime.set(System.currentTimeMillis());
                            boolean flip = flips.pop();
                            creatorMenu.updateCoin(flip, flips.isEmpty());
                            opponentMenu.updateCoin(!flip, flips.isEmpty());

                            if (flips.isEmpty()) {
                                if (coin) {
                                    Bukkit.broadcastMessage("Creator has won");
                                    CoinflipPlugin.getEconomy().depositPlayer(creator.getPlayer(), amount * 2);
                                } else {
                                    Bukkit.broadcastMessage("Opponent has won");
                                    CoinflipPlugin.getEconomy().depositPlayer(opponent.getPlayer(), amount * 2);
                                }
                            }

                        }
                    } else if (hasTimeElapsed(startTime.get(), 3, TimeUnit.SECONDS)) {
                        Bukkit.getScheduler().cancelTask(taskID.get());
                    }
                }
            }, 2L, 2L));
        }

        public Player getCreator() {
            return creator;
        }

        public Material getCreatorMaterial() {
            return creatorMaterial;
        }

        public CFViewMenu getCreatorMenu() {
            return creatorMenu;
        }

        public CFViewMenu getOpponentMenu() {
            return opponentMenu;
        }

        public double getAmount() {
            return amount;
        }

        public int getIgnored() {
            return ignored;
        }
    }

    public static boolean hasTimeElapsed(long startTime, long duration, TimeUnit timeUnit) {
        long targetTime = startTime + timeUnit.toMillis(duration);
        return System.currentTimeMillis() >= targetTime;
    }

    public CFMainMenu getCfMainMenu() {
        return cfMainMenu;
    }
}
