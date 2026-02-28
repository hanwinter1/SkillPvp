package kr.hanwinter.skillpvp.game.command;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GameLocationManagementCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("플레이어만 입력할 수 있습니다.");
            return false;
        }

        Player player = (Player) commandSender;

        for (GameLocation location : GameLocation.values()) {
            if (location.name().equals(args[0])) {
                Main.getGameManager().saveLocationFile(location, player.getLocation());
                player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l>§r §e%s§r 위치를 설정했습니다.", location.getDisplayName())));
                return false;
            }
        }

        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c없는 위치입니다."));

        return false;
    }
}
