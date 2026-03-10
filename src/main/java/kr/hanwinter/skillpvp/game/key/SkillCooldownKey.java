package kr.hanwinter.skillpvp.game.key;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class SkillCooldownKey {
    private UUID uuid;
    private String skill;

    public SkillCooldownKey(UUID uuid, String skill) {
        this.uuid = uuid;
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof SkillCooldownKey skillCooldownKey)) return false;
        return Objects.equals(skillCooldownKey.uuid, uuid) && Objects.equals(skillCooldownKey.skill, skill);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, skill);
    }
}
