package kr.hanwinter.skillpvp;

import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.command.GameLocationManagementCommand;
import kr.hanwinter.skillpvp.game.command.GameManagementCommand;
import kr.hanwinter.skillpvp.game.gui.GameGUI;
import kr.hanwinter.skillpvp.game.listener.GameItemSkillUsingListener;
import kr.hanwinter.skillpvp.game.listener.GamePlayersListener;
import kr.hanwinter.skillpvp.game.manager.CooldownManager;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import kr.hanwinter.skillpvp.user.command.UserInfoCommand;
import kr.hanwinter.skillpvp.user.command.UserJobSelectCommand;
import kr.hanwinter.skillpvp.user.command.UserMenuCommand;
import kr.hanwinter.skillpvp.user.gui.UserJobSelectGUI;
import kr.hanwinter.skillpvp.user.gui.UserMenuGUI;
import kr.hanwinter.skillpvp.user.listener.UserConnectionListener;
import kr.hanwinter.skillpvp.user.manager.UserManager;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.nisovin.magicspells.MagicSpells;

public final class Main extends JavaPlugin {

    private static Main serverInstance;
    private final static UserManager USER_MANAGER = new UserManager();
    private final static GameManager GAME_MANAGER = new GameManager();
    private final static CooldownManager COOLDOWN_MANAGER = new CooldownManager();
    private MagicSpells magicSpells;

    @Override
    public void onEnable() {
        // Plugin startup logic
        serverInstance = this;
        registerEvents();
        USER_MANAGER.basicFileSet();
        GAME_MANAGER.basicFileSet();
        registerCommands();
        startActionBarTask();
        startCooldownBossBarTask();
        magicSpells = (MagicSpells) Bukkit.getPluginManager().getPlugin("MagicSpells");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getServerInstance() {
        return serverInstance;
    }

    public static UserManager getUserManager() {
        return USER_MANAGER;
    }

    public static GameManager getGameManager() {
        return GAME_MANAGER;
    }

    public static CooldownManager getCooldownManager() { return COOLDOWN_MANAGER; }

    private void registerEvents() {
        serverInstance.getServer().getPluginManager().registerEvents(new UserConnectionListener(), this);
        serverInstance.getServer().getPluginManager().registerEvents(new UserJobSelectGUI.UserJobSelectGUIListener(), this);
        serverInstance.getServer().getPluginManager().registerEvents(new UserMenuGUI.UserMenuGUIListener(), this);
        serverInstance.getServer().getPluginManager().registerEvents(new GameGUI.GameGUIListener(), this);
        serverInstance.getServer().getPluginManager().registerEvents(new GameItemSkillUsingListener(), this);
        serverInstance.getServer().getPluginManager().registerEvents(new GamePlayersListener(), this);
    }

    private void registerCommands() {
        serverInstance.getServer().getPluginCommand("정보").setExecutor(new UserInfoCommand());
        serverInstance.getServer().getPluginCommand("직업").setExecutor(new UserJobSelectCommand());
        serverInstance.getServer().getPluginCommand("메뉴").setExecutor(new UserMenuCommand());
        serverInstance.getServer().getPluginCommand("위치설정").setExecutor(new GameLocationManagementCommand());
        serverInstance.getServer().getPluginCommand("게임종료").setExecutor(new GameManagementCommand());
    }

    private void startActionBarTask() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : getServer().getOnlinePlayers()) {
                    player.sendActionBar(LegacyComponentSerializer.legacySection().deserialize(USER_MANAGER.getUser(player.getUniqueId()).getActionBar()));
                }
            }
        }.runTaskTimer(serverInstance, 0L, 34L);
    }

    private void startCooldownBossBarTask() {
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : serverInstance.getServer().getOnlinePlayers()) {
                    if(!(USER_MANAGER.getUser(player.getUniqueId()).getLocation() == GameLocation.LOBBY || USER_MANAGER.getUser(player.getUniqueId()).getLocation() == GameLocation.TEAM_MATCH_WAITING || USER_MANAGER.getUser(player.getUniqueId()).getLocation() == GameLocation.SOLO_MATCH_WAITING)) {
                        USER_MANAGER.getUser(player.getUniqueId()).reloadCooldownBar(player);
                    }
                }
            }
        }.runTaskTimer(serverInstance, 0L, 2L);
    }
}
