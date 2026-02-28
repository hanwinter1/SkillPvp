package kr.hanwinter.skillpvp.user.command;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.user.gui.UserJobSelectGUI;
import kr.hanwinter.skillpvp.user.manager.UserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserJobSelectCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("플레이어만 입력할 수 있습니다.");
            return false;
        }

        Player player = (Player) commandSender;
        player.openInventory(new UserJobSelectGUI(Main.getUserManager().getUser(player.getUniqueId())).getInventory());
        return false;
    }
}
