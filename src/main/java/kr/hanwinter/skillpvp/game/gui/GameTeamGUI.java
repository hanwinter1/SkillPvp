package kr.hanwinter.skillpvp.game.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameTeamGUI implements InventoryHolder {
    private final Inventory inventory;

    public GameTeamGUI() {
        inventory = Bukkit.createInventory(this, 27, Component.text("게임 팀 메뉴"));

        ItemStack redTeamItem = new ItemStack(Material.RED_CONCRETE, 1);
        ItemMeta redTeamItemMeta = redTeamItem.getItemMeta();
        redTeamItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e레드팀").decoration(TextDecoration.ITALIC, false));
        redTeamItem.setItemMeta(redTeamItemMeta);
        inventory.setItem(10, redTeamItem);

        ItemStack blueTeamItem = new ItemStack(Material.BLUE_CONCRETE, 1);
        ItemMeta blueTeamItemMeta = blueTeamItem.getItemMeta();
        blueTeamItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e블루팀").decoration(TextDecoration.ITALIC, false));
        blueTeamItem.setItemMeta(blueTeamItemMeta);
        inventory.setItem(16, blueTeamItem);

        ItemStack blackStainedGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta blackStainedGlassPaneMeta = blackStainedGlassPane.getItemMeta();
        blackStainedGlassPaneMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§r"));
        blackStainedGlassPane.setItemMeta(blackStainedGlassPaneMeta);
        for(int i=0; i<9; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }
        for(int i=18; i<27; i++) {
            inventory.setItem(i, blackStainedGlassPane);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public static class GameTeamGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof GameTeamGUI) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                switch(event.getRawSlot()) {
                    case 10:
                        // 미구현
                        break;
                    case 16:
                        // 미구현
                }
            }
        }
    }
}
