package kr.hanwinter.skillpvp.game.manager;

import kr.hanwinter.skillpvp.game.key.SkillCooldownKey;
import org.bukkit.entity.Player;

import java.util.*;

public class CooldownManager {

    private final Map<SkillCooldownKey, Long> cooldowns = new HashMap<>();
    private final Map<UUID, List<String>> usedSkills = new HashMap<>();

    public boolean isCooldown(UUID uuid, String skill) {
        if (!cooldowns.containsKey(new SkillCooldownKey(uuid, skill))) {
            return false;
        }
        return System.currentTimeMillis() < cooldowns.get(new SkillCooldownKey(uuid, skill));
    }

    public double getCooldown(UUID uuid, String skill) {
        if(!(cooldowns.containsKey(new SkillCooldownKey(uuid, skill)))) {
            return 0;
        }
        if(System.currentTimeMillis() > cooldowns.get(new SkillCooldownKey(uuid, skill))) {
            return 0;
        } else {
            return (cooldowns.get(new SkillCooldownKey(uuid, skill)) - System.currentTimeMillis()) / 1000.0;
        }
    }

    public void setCooldown(UUID uuid, String skill, long cooldownMillis) {
        cooldowns.put(new SkillCooldownKey(uuid, skill), Long.valueOf(cooldownMillis));
        usedSkills.computeIfAbsent(uuid, k -> new ArrayList<>());
        usedSkills.get(uuid).add(skill);
    }

    public void removeCooldown(UUID uuid) {
        for(String skill : usedSkills.get(uuid)) {
            cooldowns.remove(new SkillCooldownKey(uuid, skill));
        }
        usedSkills.remove(uuid);
    }

}
