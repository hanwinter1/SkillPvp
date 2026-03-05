package kr.hanwinter.skillpvp.user.command;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.user.gui.UserJobSelectGUI;
import kr.hanwinter.skillpvp.user.gui.UserMenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage("플레이어만 입력할 수 있습니다.");
            return false;
        }

        player.openInventory(new UserMenuGUI(player.getUniqueId()).getInventory());
        return false;
    }
}
