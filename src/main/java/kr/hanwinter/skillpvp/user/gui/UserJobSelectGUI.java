package kr.hanwinter.skillpvp.user.gui;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.util.ItemUtil;
import kr.hanwinter.skillpvp.user.Job;
import kr.hanwinter.skillpvp.user.User;
import kr.hanwinter.skillpvp.user.manager.UserManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class UserJobSelectGUI implements InventoryHolder {
    private final Inventory inventory;

    public UserJobSelectGUI(User user) {
        int unlockedJobsNumber = user.getUnlockedJobs().size();
        int inventorySize = 0;
        while(unlockedJobsNumber > 9) {
            unlockedJobsNumber -= 9;
            inventorySize++;
        }
        inventory = Bukkit.createInventory(this, 27 + inventorySize*9, LegacyComponentSerializer.legacySection().deserialize("§e직업 선택"));
        ItemStack blackStainedGlassPane = ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, Component.text(" "), null);
        for(int i=0; i<9; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }
        for(int i=18+inventorySize*9; i<27+inventorySize*9; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }

        int slotNum = 9;
        for(Job job : user.getUnlockedJobs()) {
            inventory.setItem(slotNum, job.getJobItem());
            slotNum++;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static class UserJobSelectGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            UserManager userManager = Main.getUserManager();
            if (event.getInventory().getHolder() instanceof UserJobSelectGUI) {
                event.setCancelled(true);

                Player player = (Player) event.getWhoClicked();

                User user = userManager.getUser(player.getUniqueId());
                for (Job job : user.getUnlockedJobs()) {
                    if (event.getCurrentItem().equals(job.getJobItem())) {
                        player.closeInventory();
                        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(job.getHealth());
                        player.sendMessage(LegacyComponentSerializer.legacySection().deserialize(String.format(" §a§l>§r §e%s§r 직업을 선택했습니다.", job.getDisplayName())));
                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1, 0);
                        user.setCurrentJob(job);
                        userManager.saveDataFile(player.getUniqueId());
                    }
                }
            }
        }
    }
}
