package kr.hanwinter.skillpvp.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public enum Job {
    FIRE("화염", 30, 2, 800, 10000, 30000, 100000)
    , LIGHTNING("번개", 20, 3, 800, 10000, 30000, 100000)
    , FLOWER("꽃", 20, 3, 800, 10000, 30000, 100000)
    , NATURE("자연", 40, 1.5, 800, 10000, 30000, 100000);

    private final String displayName;
    private final int health;
    private final double damage;

    private final long skillOneCooldown;
    private final long skillTwoCooldown;
    private final long skillThreeCooldown;
    private final long skillFourCooldown;

    Job(String displayName, int health, double damage, long skillOneCooldown, long skillTwoCooldown, long skillThreeCooldown, long skillFourCooldown) {
        this.displayName = displayName;
        this.health = health;
        this.damage = damage;
        this.skillOneCooldown = skillOneCooldown;
        this.skillTwoCooldown = skillTwoCooldown;
        this.skillThreeCooldown = skillThreeCooldown;
        this.skillFourCooldown = skillFourCooldown;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public long getSkillOneCooldown() {
        return skillOneCooldown;
    }

    public long getSkillTwoCooldown() {
        return skillTwoCooldown;
    }

    public long getSkillThreeCooldown() {
        return skillThreeCooldown;
    }

    public long getSkillFourCooldown() {
        return skillFourCooldown;
    }

    public ItemStack getJobItem() {
        ItemStack jobItem = new ItemStack(Material.KNOWLEDGE_BOOK, 1);
        ItemMeta jobItemMeta = jobItem.getItemMeta();
        jobItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize(String.format("§a§l[직업]§r §f%s", displayName)).decoration(TextDecoration.ITALIC,false));
        jobItemMeta.addEnchant(Enchantment.DENSITY, 1, true);
        jobItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        jobItemMeta.lore(List.of(Component.text(""), LegacyComponentSerializer.legacySection().deserialize(String.format(" §a§l>§r §f직업 체력 §7-§f %d", health)).decoration(TextDecoration.ITALIC,false), LegacyComponentSerializer.legacySection().deserialize(String.format(" §a§l>§r §f직업 공격력 §7-§f %.1f", damage)).decoration(TextDecoration.ITALIC,false), Component.text("")));

        jobItem.setItemMeta(jobItemMeta);
        return jobItem;
    }
}
