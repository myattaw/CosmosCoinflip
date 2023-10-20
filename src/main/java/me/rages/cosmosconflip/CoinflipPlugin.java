package me.rages.cosmosconflip;

import me.rages.cosmosconflip.commands.ConflipCommand;
import me.rages.cosmosconflip.ui.CFMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class CoinflipPlugin extends JavaPlugin {

    public static CoinflipPlugin plugin;

    public static final Material[] COLOR_TYPES = {
            Material.PURPLE_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.RED_CONCRETE,
            Material.LIME_CONCRETE, Material.YELLOW_CONCRETE, Material.ORANGE_CONCRETE,
            Material.WHITE_CONCRETE, Material.GRAY_CONCRETE, Material.BLACK_CONCRETE,
    };

    @Override
    public void onEnable() {
        // Plugin startup logic
        new ConflipCommand(this);
    }

    public CoinflipPlugin() {
        plugin = this;
    }

    public static class CoinFlipMatch {
        private Player creator;
        private Player opponent;

        private CFMenu creatorMenu;

        private static final Random random = new Random();

        private Stack<Boolean> flips = new Stack<>();


        CoinFlipMatch(Player creator, CFMenu creatorMenu) {
            this.creator = creator;
            this.creatorMenu = creatorMenu;
        }

        public static CoinFlipMatch create(Player creator, CFMenu creatorMenu) {
            return new CoinFlipMatch(creator, creatorMenu);
        }

        public boolean coinFlip() {
            return random.nextBoolean();
        }

        public void startGame(Player joiner, CFMenu opponentMenu) {
            this.opponent = joiner;

            boolean value = coinFlip();
            for (int i = 0; i < (random.nextInt(10) + 25); i++) {
                flips.push(value);
                value = !value;
            }

            boolean coin = coinFlip();
            flips.push(coin);

            AtomicLong startTime = new AtomicLong(System.currentTimeMillis());
            AtomicInteger taskID = new AtomicInteger(-1);

            taskID.set(Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (creatorMenu.getTimer() > 0) {
                    if (hasTimeElapsed(startTime.get(), 1, TimeUnit.SECONDS)) {
                        startTime.set(System.currentTimeMillis());
                        creatorMenu.setOpponentMaterial(opponentMenu.getUserMaterial());
                        opponentMenu.setOpponentMaterial(creatorMenu.getUserMaterial());

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
                            creatorMenu.updateCoin(flip);
                            opponentMenu.updateCoin(!flip);
                        }
                    } else if (hasTimeElapsed(startTime.get(), 1, TimeUnit.SECONDS)){
                        Bukkit.broadcastMessage(coin ? "Creator has won" : "Opponent has won");
                        Bukkit.getScheduler().cancelTask(taskID.get());
                    }
                }
            }, 2L, 2L));
        }


    }

    public static boolean hasTimeElapsed(long startTime, long duration, TimeUnit timeUnit) {
        long targetTime = startTime + timeUnit.toMillis(duration);
        return System.currentTimeMillis() >= targetTime;
    }

}
