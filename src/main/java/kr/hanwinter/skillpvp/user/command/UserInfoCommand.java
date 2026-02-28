package kr.hanwinter.skillpvp.user.command;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.user.Job;
import kr.hanwinter.skillpvp.user.User;
import kr.hanwinter.skillpvp.user.manager.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("플레이어만 입력할 수 있습니다.");
            return false;
        }

        Player player = (Player) commandSender;
        User user = Main.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Component.text(""));
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l>§r §e%s§r님의 정보 ", player.getName())));
        player.sendMessage(Component.text(""));
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l|§r 돈 §7-§r %d", user.getMoney())));
        if(user.getDeath() == 0) {
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l|§r 킬 / 데스 §7-§r %d / %d §e(%s K/D)", user.getKill(), user.getDeath(), "Perfect")));
        } else {
            double kda = (double) user.getKill() / user.getDeath();
            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l|§r 킬 / 데스 §7-§r %d / %d §e(%.2f K/D)",
                    user.getKill(),
                    user.getDeath(),
                    kda)));
        }
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l|§r 현재 아이템 §7-§r %s", user.getCurrentItem())));
        Job userJob = user.getCurrentJob();
        String job = null;
        if(userJob != null) {
            job = userJob.getDisplayName();
        }
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l|§r 현재 직업 §7-§r %s", job)));
        player.sendMessage(Component.text(""));
        return false;
    }
}
