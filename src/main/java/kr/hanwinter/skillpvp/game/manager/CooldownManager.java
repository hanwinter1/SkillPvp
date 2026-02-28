package kr.hanwinter.skillpvp.game.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();

    public boolean isCooldown(Player player, String skill) {
        if (!cooldowns.containsKey(player.getUniqueId())) {
            return false;
        }
        if(cooldowns.get(player.getUniqueId()).get(skill) == null) {
            return false;
        }
        return System.currentTimeMillis() < cooldowns.get(player.getUniqueId()).get(skill);
    }

    public double getCooldown(Player player, String skill) {
        if(!(cooldowns.containsKey(player.getUniqueId()))) {
            return 0;
        }
        if(cooldowns.get(player.getUniqueId()).get(skill) == null) {
            return 0;
        }
        if(System.currentTimeMillis() > cooldowns.get(player.getUniqueId()).get(skill)) {
            return 0;
        } else {
            return (cooldowns.get(player.getUniqueId()).get(skill) - System.currentTimeMillis()) / 1000.0;
        }
    }

    public void setCooldown(Player player, String skill, long cooldownMillis) {
        if(!(cooldowns.containsKey(player.getUniqueId()))) {
            cooldowns.put(player.getUniqueId(), new HashMap<>());
        }

        cooldowns.get(player.getUniqueId()).put(skill, cooldownMillis);
    }

    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }

}
