package kr.hanwinter.skillpvp.game.command;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class GameManagementCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(Main.getGameManager().getGameState() == GameManager.GameState.SOLO_MATCH_PLAYING) {
            Main.getGameManager().endSoloGame();
        }

        return false;
    }
}
