package me.rages.cosmosconflip;

import me.rages.cosmosconflip.commands.ConflipCommand;
import me.rages.cosmosconflip.menu.MenuBuilder;
import me.rages.cosmosconflip.menu.impl.CFMainMenu;
import me.rages.cosmosconflip.menu.impl.CFViewMenu;
import me.rages.cosmosconflip.util.Util;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.persistence.PersistentDataType;
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

    public static NamespacedKey cfRatioKey;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        cfRatioKey = new NamespacedKey(this, "cfratio");
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
        Iterator<CoinFlipMatch> iterator = coinFlipMatchList.iterator();
        while (iterator.hasNext()) {
            CoinFlipMatch match = iterator.next();
            double amount = match.getAmount();
            iterator.remove(); // Safely removes the current element from the list
            getEconomy().depositPlayer(match.getCreator(), amount);
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

                                if (amount >= plugin.getConfig().getDouble("settings.broadcast.minimum")) {
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                                            plugin.getConfig().getString("settings.broadcast.message")
                                                    .replace("%winner%", coin ? creator.getName() : opponent.getName())
                                                    .replace("%loser%", coin ? opponent.getName() : creator.getName())
                                                    .replace("%amount%", Util.getAbbreviatedCurrency(amount, true))
                                    ));
                                }

                                if (coin) {
                                    addWinLoss(creator.getPlayer(), true, amount);
                                    addWinLoss(Objects.requireNonNull(opponent.getPlayer()), false, amount);
                                    CoinflipPlugin.getEconomy().depositPlayer(creator.getPlayer(), amount * 2);
                                } else {
                                    addWinLoss(opponent.getPlayer(), true, amount);
                                    addWinLoss(creator.getPlayer(), false, amount);
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

    private static void addWinLoss(Player player, boolean won, double amount) {
        int[] games = player.getPersistentDataContainer().getOrDefault(
                cfRatioKey,
                PersistentDataType.INTEGER_ARRAY,
                new int[2]
        );

        if (won) {
            games[0] += 1;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(plugin.getConfig().getString("settings.win-msg"))
                                    .replace("%amount%", Util.getAbbreviatedCurrency(amount, true))
                    )
            );
        } else {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            Objects.requireNonNull(plugin.getConfig().getString("settings.loss-msg"))
                                    .replace("%amount%", Util.getAbbreviatedCurrency(amount, true))
                    )
            );
            games[1] += 1;
        }

        player.getPersistentDataContainer().set(cfRatioKey, PersistentDataType.INTEGER_ARRAY, games);
    }

    public static boolean hasTimeElapsed(long startTime, long duration, TimeUnit timeUnit) {
        long targetTime = startTime + timeUnit.toMillis(duration);
        return System.currentTimeMillis() >= targetTime;
    }

    public CFMainMenu getCfMainMenu() {
        return cfMainMenu;
    }
}
