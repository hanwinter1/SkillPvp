package kr.hanwinter.skillpvp.game.key;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkillCooldownKey {
    private final Map<UUID, String> cooldownMap = new HashMap<>();
    public SkillCooldownKey(UUID uuid, String skill) {
        cooldownMap.put(uuid, skill);
    }
}
