package kr.hanwinter.skillpvp.user.manager;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.user.Job;
import kr.hanwinter.skillpvp.user.User;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserManager {
    private HashMap<UUID, User> userData = new HashMap<>();
    private final File dataFile;
    private final Main serverInstance;

    public UserManager(Main serverInstance) {
        this.serverInstance = serverInstance;
        dataFile = new File(serverInstance.getDataFolder(), "playerdata");
    }

    public void addNewUser(UUID uuid) {
        User user = new User(0L, 0, 0, null, null, new HashSet<>(Set.of(Job.FIRE, Job.FLOWER, Job.LIGHTNING, Job.NATURE)), new HashSet<>(), false, GameLocation.LOBBY);
        userData.put(uuid, user);
    }

    public void loadUser(UUID uuid) {
        User user = loadDataFile(uuid);
        userData.put(uuid, user);
    }

    public User getUser(UUID uuid) {
        return userData.get(uuid);
    }

    public void removeUser(UUID uuid) {
        userData.remove(uuid);
    }

    public void saveDataFile(UUID uuid) {
        User user = getUser(uuid);
        user.reloadActionBar(uuid);
        File playerDataFile = new File(dataFile, uuid.toString() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        playerData.set("playerName", serverInstance.getServer().getOfflinePlayer(uuid).getName());
        playerData.set("money", user.getMoney());
        playerData.set("kill", user.getKill());
        playerData.set("death", user.getDeath());
        playerData.set("currentItem", user.getCurrentItem());
        playerData.set("currentJob", user.getCurrentJob() == null ? null : user.getCurrentJob().name());
        HashSet<Job> unlockedJobs = user.getUnlockedJobs();
        List<String> jobNames = new ArrayList<>();
        for(Job job : unlockedJobs) {
            jobNames.add(job.name());
        }
        playerData.set("unlockedJobs", jobNames);
        playerData.set("clearedAchievements", new ArrayList<>(user.getClearedAchievements()));
        playerData.set("isItemUnlocked", user.isItemUnlocked());
        playerData.set("location", user.getLocation().name());
        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User loadDataFile(UUID uuid) {
        File playerDataFile = new File(dataFile, uuid.toString() + ".yml");
        FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        long money = playerData.getLong("money");
        int kill = playerData.getInt("kill");
        int death = playerData.getInt("death");
        String currentItem = playerData.getString("currentItem");
        String currentJobString = playerData.getString("currentJob");
        Job currentJob = null;
        if(!(currentJobString == null)) {
            currentJob = Job.valueOf(currentJobString);
        }
        List<String> unlockedJobsList = playerData.getStringList("unlockedJobs");
        HashSet<Job> unlockedJobs = new HashSet<Job>();
        for(String unlocked : unlockedJobsList) {
            unlockedJobs.add(Job.valueOf(unlocked));
        }
        List<String> clearedAchievementsList = playerData.getStringList("clearedAchievements");
        HashSet<String> clearedAchievements = new HashSet<>(clearedAchievementsList);
        Boolean isItemUnlocked = playerData.getBoolean("isItemUnlocked");
        String location = playerData.getString("location");
        User user = new User(money, kill, death, currentJob, currentItem, unlockedJobs, clearedAchievements, isItemUnlocked, GameLocation.valueOf(location));
        user.reloadActionBar(uuid);
        return user;
    }

    public boolean isFileExist(UUID uuid) {
        File playerDataFile = new File(dataFile, uuid.toString() + ".yml");
        if(playerDataFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public void basicFileSet() {
        if(!serverInstance.getDataFolder().exists()) {
            serverInstance.getDataFolder().mkdirs();
        }

        if(!dataFile.exists()) {
            dataFile.mkdirs();
        }
    }

    public void userTeleport(Player player, GameLocation location) {
        player.teleport(Main.getGameManager().loadLocationFile(location));
        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §b§l>§r §e%s§r(으)로 이동했습니다.", location.getDisplayName())));
        Main.getUserManager().getUser(player.getUniqueId()).setLocation(location);
        Main.getUserManager().saveDataFile(player.getUniqueId());
        Main.getUserManager().getUser(player.getUniqueId()).reloadActionBar(player.getUniqueId());
    }
}
