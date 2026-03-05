package kr.hanwinter.skillpvp.game.listener;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.Map;

public class GamePlayersListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = Main.getGameManager();
        if (gameManager.getGameState() != GameManager.GameState.SOLO_MATCH_PLAYING) { return; }
        if(gameManager.getPlayers().contains(player)) {
            Map<Player, Integer> playerLifeMap = gameManager.getPlayerLifeMap();
            playerLifeMap.put(player, playerLifeMap.get(player)-1);
            gameManager.updateScoreboard();
            if(playerLifeMap.get(player) == 0) {
                player.setGameMode(GameMode.SPECTATOR);
                int alivePlayerNumber = 0;
                Player alivePlayer = null;
                for(Player player1 : gameManager.getPlayers()) {
                    if (playerLifeMap.get(player1) > 0) {
                        alivePlayerNumber++;
                        alivePlayer = player1;
                    }
                }
                if(alivePlayerNumber == 1) {
                    gameManager.endSoloGame();
                    Main.getServerInstance().getServer().broadcast(Component.text(alivePlayer.name() + "님이 우승하셨습니다!"));
                    Main.getUserManager().getUser(alivePlayer.getUniqueId()).setMoney(Main.getUserManager().getUser(alivePlayer.getUniqueId()).getMoney() + 300);
                    Main.getUserManager().getUser(alivePlayer.getUniqueId()).reloadActionBar(alivePlayer.getUniqueId());
                } else {
                    for (Player player1 : gameManager.getPlayers()) {
                        player1.showTitle(Title.title(Component.text(""), Component.text(player + "님이 탈락했습니다!")));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = Main.getGameManager();
        if (gameManager.getGameState() != GameManager.GameState.SOLO_MATCH_PLAYING) { return; }
        if(gameManager.getPlayers().contains(player)) {
            if(gameManager.getPlayerLifeMap().get(player) > 0) {
                String location = "SOLO_MATCH_LOCATION1_" + (int) (Math.random() * 10 + 1);
                Main.getUserManager().userTeleport(player, GameLocation.valueOf(location));
            } else {
                player.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
