package kr.hanwinter.skillpvp.game.manager;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameManager {
    private final File locationFile;
    private final Main serverInstance;
    private GameState gameState;
    private final List<Player> players = new ArrayList<>();
    private final Map<Player, Team> playerTeamMap = new HashMap<>();
    private final Map<Player, Integer> playerLifeMap = new HashMap<>();
    private BukkitRunnable waitingTask;
    private int countdown;
    private BossBar timeBar = null;
    private final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private Scoreboard scoreboard;
    private Objective objective;

    public enum GameState {
        SOLO_MATCH_WAITING, SOLO_MATCH_PLAYING, TEAM_MATCH_PLAYING, TEAM_MATCH_WAITING
    }

    public enum Team {
        RED, BLUE
    }

    public GameManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        locationFile = new File(serverInstance.getDataFolder(), "location");
    }

    public void saveLocationFile(GameLocation location, Location playerLocation) {
        File locationDataFile = new File(locationFile, location.name() + ".yml");
        FileConfiguration locationData = YamlConfiguration.loadConfiguration(locationDataFile);
        locationData.set("location", playerLocation);
        try {
            locationData.save(locationDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location loadLocationFile(GameLocation location) {
        File locationDataFile = new File(locationFile, location.name() + ".yml");
        FileConfiguration locationData = YamlConfiguration.loadConfiguration(locationDataFile);
        return locationData.getLocation("location");
    }

    public boolean isFileExist(GameLocation location) {
        File playerDataFile = new File(locationFile, location.name() + ".yml");
        if(playerDataFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void basicFileSet() {
        if(!serverInstance.getDataFolder().exists()) {
            serverInstance.getDataFolder().mkdirs();
        }

        if(!locationFile.exists()) {
            locationFile.mkdirs();
        }
    }

    public void soloMatchJoin(Player player) {
        if(gameState == null || gameState == GameState.SOLO_MATCH_WAITING) {
            if(gameState == null) {
                gameState = GameState.SOLO_MATCH_WAITING;
            }
            players.add(player);
            Main.getUserManager().userTeleport(player, GameLocation.SOLO_MATCH_WAITING);
            serverInstance.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize(String.format("§e§l[안내]§r 개인전에 플레이어가 참여했습니다. §7(총 %d명)", players.size())));
            if(players.size() > 1) {
                if(waitingTask == null) {
                    startSoloGameWaitingTask();
                }
                countdown = 10;
            }
        }
    }

    public void soloMatchQuit(Player player) {
        players.remove(player);
        if(player.isOnline()) {
            Main.getUserManager().userTeleport(player, GameLocation.LOBBY);
        }
        serverInstance.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize(String.format("§e§l[안내]§r 개인전에 플레이어가 퇴장했습니다. §7(총 %d명)", players.size())));
        if(players.isEmpty()) {
            gameState = null;
            serverInstance.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize("§e§l[안내]§r 개인전 대기가 종료되었습니다."));
        }
    }

    public void updateScoreboard() {
        scoreboard = scoreboardManager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("lifeBar", Criteria.DUMMY, Component.text("플레이어 목숨 개수"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(Player player : players) {
            if(playerLifeMap.get(player) != 0) {
                objective.getScore(player.getName()).setScore(playerLifeMap.get(player));
            }
            player.setScoreboard(scoreboard);
        }
    }

    private void startSoloGame() {
        countdown = 600;
        updateScoreboard();
        gameState = GameState.SOLO_MATCH_PLAYING;
        timeBar = Bukkit.createBossBar(String.format("남은 시간 - §e%d초", countdown), BarColor.BLUE, BarStyle.SEGMENTED_12);
        timeBar.setProgress(countdown / 600.0);
        if(players.size() < 10) {
            List<Integer> numList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
            for(Player player : players) {
                int b = (int) (Math.random() * numList.size() + 1);
                numList.remove(b);
                String location = "SOLO_MATCH_LOCATION1_" + b;
                Main.getUserManager().userTeleport(player, GameLocation.valueOf(location));
            }
        } else {
            for(Player player : players) {
                int b = (int) (Math.random() * 10 + 1);
                String location = "SOLO_MATCH_LOCATION1_" + b;
                Main.getUserManager().userTeleport(player, GameLocation.valueOf(location));
            }
        }
        for(Player player : players) {
            playerLifeMap.put(player, 3);
            player.give(GameManager.getSkillItem());
            Main.getUserManager().getUser(player.getUniqueId()).makeCooldownBar(player);
            Main.getUserManager().getUser(player.getUniqueId()).getCooldownBar().addPlayer(player);
            timeBar.addPlayer(player);
            player.showTitle(Title.title(Component.text("게임이 시작되었습니다!"), Component.text("")));
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        }

        startSoloGameTimeTask();
    }

    public void endSoloGame() {
        playerLifeMap.clear();
        gameState = null;
        objective.unregister();
        objective = null;
        scoreboard = null;
        for(Player player : players) {
            timeBar.removePlayer(player);
            player.getInventory().clear();
            Main.getUserManager().userTeleport(player, GameLocation.LOBBY);
            player.showTitle(Title.title(Component.text("게임이 종료되었습니다!"), Component.text("")));
            player.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            Main.getUserManager().getUser(player.getUniqueId()).removeCooldownBar(player);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        players.clear();
        timeBar = null;
    }


    public void quitSoloGame(Player player) {
        timeBar.removePlayer(player);
        player.getInventory().clear();
        Main.getUserManager().getUser(player.getUniqueId()).removeCooldownBar(player);
        playerLifeMap.remove(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        players.remove(player);
    }

    public Map<Player, Integer> getPlayerLifeMap() {
        return playerLifeMap;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getGameState() {
        return gameState;
    }

    public static ItemStack getSkillItem() {
        ItemStack usingSkillItem = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemMeta usingSkillItemMeta = usingSkillItem.getItemMeta();
        usingSkillItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§r§e스킬 사용용 무기"));
        usingSkillItem.setItemMeta(usingSkillItemMeta);
        return usingSkillItem;
    }

    private void startSoloGameWaitingTask() {
        waitingTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(players.size() < 2) {
                    serverInstance.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize("§e§l[안내]§r 개인전 인원이 충족되지 않아 취소됩니다."));
                    cancel();
                    waitingTask = null;
                    return;
                }
                if(countdown < 1) {
                    cancel();
                    waitingTask = null;
                    startSoloGame();
                    return;
                }

                serverInstance.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize(String.format("§e§l[안내]§r 개인전 대기가 §e%d§r초 남았습니다.", countdown)));
                countdown--;
            }
        };

        waitingTask.runTaskTimer(serverInstance, 0L, 20L);
    }

    private void startSoloGameTimeTask() {
        waitingTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(countdown < 1) {
                    endSoloGame();
                    cancel();
                    waitingTask = null;
                    return;
                }
                if(players.isEmpty()) {
                    cancel();
                    waitingTask = null;
                    return;
                }

                timeBar.setTitle(String.format("남은 시간 - §e%d초", countdown));
                timeBar.setProgress(countdown / 600.0);
                countdown--;
            }
        };

        waitingTask.runTaskTimer(serverInstance, 0L, 20L);
    }
}
