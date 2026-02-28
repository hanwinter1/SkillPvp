package kr.hanwinter.skillpvp.game.listener;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.util.SpellData;
import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import kr.hanwinter.skillpvp.user.Job;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameItemSkillUsingListener implements Listener {

    public enum ClickType {
        LEFT, RIGHT, SHIFT_LEFT, SHIFT_RIGHT
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }
        if(!(event.getItem().equals(GameManager.getSkillItem()))) {
            return;
        }
        if(Main.getUserManager().getUser(event.getPlayer().getUniqueId()).getLocation() == GameLocation.LOBBY) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Job job = Main.getUserManager().getUser(player.getUniqueId()).getCurrentJob();
        ClickType clickType;
        long cooldown;
        switch(event.getAction()) {
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (player.isSneaking()) {
                    clickType = ClickType.SHIFT_LEFT;
                    cooldown = job.getSkillThreeCooldown();
                } else {
                    clickType = ClickType.LEFT;
                    cooldown = job.getSkillOneCooldown();
                }
                break;
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                if (player.isSneaking()) {
                    clickType = ClickType.SHIFT_RIGHT;
                    cooldown = job.getSkillFourCooldown();
                } else {
                    clickType = ClickType.RIGHT;
                    cooldown = job.getSkillTwoCooldown();
                }
                break;
            default:
                return;
        }
        String skill = job.name() + "_" + clickType.name();
        if (Main.getCooldownManager().isCooldown(player, skill)) {
            return;
        }
        Spell spell = MagicSpells.getSpellByInternalName(skill);
        if(spell != null) {
            spell.cast(new SpellData(player));
        }

        Main.getCooldownManager().setCooldown(player, skill, System.currentTimeMillis() + cooldown);
    }
}
