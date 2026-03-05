package kr.hanwinter.skillpvp.user;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class User {
    public User(long money, int kill, int death, Job currentJob, String currentItem, HashSet<Job> unlockedJobs, HashSet<String> clearedAchievements, boolean isItemUnlocked, GameLocation location) {
        this.money = money;
        this.kill = kill;
        this.death = death;
        this.currentJob = currentJob;
        this.currentItem = currentItem;
        this.unlockedJobs = unlockedJobs;
        this.clearedAchievements = clearedAchievements;
        this.isItemUnlocked = isItemUnlocked;
        this.location = location;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public String getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(String currentItem) {
        this.currentItem = currentItem;
    }

    public HashSet<Job> getUnlockedJobs() {
        return unlockedJobs;
    }

    public HashSet<String> getClearedAchievements() {
        return clearedAchievements;
    }

    public boolean isItemUnlocked() {
        return isItemUnlocked;
    }

    public void setItemUnlocked(boolean itemUnlocked) {
        isItemUnlocked = itemUnlocked;
    }

    public void setLocation(GameLocation location) {
        this.location = location;
    }

    public GameLocation getLocation() {
        return location;
    }

    public void reloadActionBar(UUID uuid) {
        String currentItemString = null;
        if (currentItem == null) {
            currentItemString = "없음";
        } else {
            currentItemString = currentItem;
        }

        String currentJobString = null;
        if (currentJob == null) {
            currentJobString = "없음";
        } else {
            currentJobString = currentJob.getDisplayName();
        }

        actionBar = String.format("§r플레이어 §c§l>§r %s §8|§r 돈 §6§l>§r %d §8|§r 현재 직업 §e§l>§r %s §8|§r 현재 아이템 §a§l>§r %s §8|§r 현재 위치 §b§l>§r %s", Main.getServerInstance().getServer().getPlayer(uuid).getName(), money, currentJobString, currentItemString, location.getDisplayName());
    }

    public String getActionBar() {
        return actionBar;
    }

    public void makeCooldownBar() {
        String title = "좌클릭 쿨타임 §7-§r §e0.0초§r §8|§r 우클릭 쿨타임 §7-§r §e0.0초§r §8|§r 쉬프트 좌클릭 쿨타임 §7-§r §e0.0초§r §8|§r 쉬프트 우클릭 쿨타임 §7-§r §e0.0초§r";
        cooldownBar = Bukkit.createBossBar(title, BarColor.BLUE, BarStyle.SOLID);
    }

    public void removeCooldownBar(Player player) {
        cooldownBar.removePlayer(player);
        cooldownBar = null;
    }

    public void reloadCooldownBar(Player player) {
        double cool1 = Main.getCooldownManager().getCooldown(player, currentJob.name() + "_LEFT");
        double cool2 = Main.getCooldownManager().getCooldown(player, currentJob.name() + "_RIGHT");
        double cool3 = Main.getCooldownManager().getCooldown(player, currentJob.name() + "_SHIFT_LEFT");
        double cool4 = Main.getCooldownManager().getCooldown(player, currentJob.name() + "_SHIFT_RIGHT");

        String title = String.format("좌클릭 쿨타임 §7-§r §e%.1f초§r §8|§r 우클릭 쿨타임 §7-§r §e%.1f초§r §8|§r 쉬프트 좌클릭 쿨타임 §7-§r §e%.1f초§r §8|§r 쉬프트 우클릭 쿨타임 §7-§r §e%.1f초§r", cool1, cool2, cool3, cool4);
        cooldownBar.setTitle(title);
    }

    public BossBar getCooldownBar() {
        return cooldownBar;
    }

    private long money;
    private int kill;
    private int death;
    private Job currentJob;
    private String currentItem;
    private final HashSet<Job> unlockedJobs;
    private final HashSet<String> clearedAchievements;
    private boolean isItemUnlocked;
    private String actionBar;
    private GameLocation location;
    private BossBar cooldownBar = null;
}
