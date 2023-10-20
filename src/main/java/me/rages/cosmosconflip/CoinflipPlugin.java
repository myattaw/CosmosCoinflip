package me.rages.cosmosconflip;

import com.google.common.collect.ImmutableMap;
import me.rages.cosmosconflip.commands.ConflipCommand;
import me.rages.cosmosconflip.ui.CFMenu;
import me.rages.cosmosconflip.ui.ColorMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class CoinflipPlugin extends JavaPlugin {

    public static CoinflipPlugin plugin;

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
        private CFMenu opponentMenu;

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

        public void startGame(Player opponent, CFMenu opponentMenu) {
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
                                Bukkit.broadcastMessage(coin ? "Creator has won" : "Opponent has won");

                                System.out.println("good");
                            }

                        }
                    } else if (hasTimeElapsed(startTime.get(), 1, TimeUnit.SECONDS)) {
                        Bukkit.getScheduler().cancelTask(taskID.get());
                    }
                }
            }, 2L, 2L));
        }

        public CFMenu getCreatorMenu() {
            return creatorMenu;
        }

        public CFMenu getOpponentMenu() {
            return opponentMenu;
        }
    }

    public static boolean hasTimeElapsed(long startTime, long duration, TimeUnit timeUnit) {
        long targetTime = startTime + timeUnit.toMillis(duration);
        return System.currentTimeMillis() >= targetTime;
    }

}
