package kr.hanwinter.skillpvp.user.listener;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import kr.hanwinter.skillpvp.user.manager.UserManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class UserConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UserManager userManager = Main.getUserManager();
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(userManager.isFileExist(uuid)) {
            userManager.loadUser(uuid);
            userManager.getUser(uuid).reloadActionBar(uuid);
            player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(userManager.getUser(uuid).getCurrentJob().getHealth());
        } else {
            userManager.addNewUser(uuid);
            userManager.userTeleport(player, GameLocation.LOBBY);
            userManager.getUser(uuid).reloadActionBar(uuid);
        }
        if(userManager.getUser(uuid).getLocation() == GameLocation.PRACTICE) {
            userManager.getUser(uuid).makeCooldownBar();
            userManager.getUser(uuid).getCooldownBar().addPlayer(player);
        }
        if(!(userManager.getUser(uuid).getLocation() == GameLocation.LOBBY || userManager.getUser(uuid).getLocation() == GameLocation.PRACTICE)) {
            userManager.userTeleport(player, GameLocation.LOBBY);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UserManager userManager = Main.getUserManager();
        userManager.saveDataFile(event.getPlayer().getUniqueId());
        if(userManager.getUser(event.getPlayer().getUniqueId()).getLocation() == GameLocation.SOLO_MATCH_WAITING) {
            Main.getGameManager().soloMatchQuit(event.getPlayer());
        }
        if(!(userManager.getUser(event.getPlayer().getUniqueId()).getLocation() == GameLocation.LOBBY || userManager.getUser(event.getPlayer().getUniqueId()).getLocation() == GameLocation.TEAM_MATCH_WAITING || userManager.getUser(event.getPlayer().getUniqueId()).getLocation() == GameLocation.SOLO_MATCH_WAITING)) {
            Main.getCooldownManager().removeCooldown(event.getPlayer().getUniqueId());
        }
        userManager.removeUser(event.getPlayer().getUniqueId());
        if(Main.getGameManager().getGameState() == GameManager.GameState.SOLO_MATCH_PLAYING) {
            if(Main.getGameManager().getPlayers().contains(event.getPlayer())) {
                Main.getGameManager().quitSoloGame(event.getPlayer());
                if(Main.getGameManager().getPlayers().isEmpty()) {
                    Main.getGameManager().endSoloGame();
                }
            }
        }
    }
}
