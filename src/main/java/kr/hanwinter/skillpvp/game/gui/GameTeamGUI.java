package kr.hanwinter.skillpvp.game.gui;

import kr.hanwinter.skillpvp.game.util.ItemUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.hover.content.Item;
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

        inventory.setItem(10, ItemUtil.createItem(Material.RED_CONCRETE, LegacyComponentSerializer.legacySection().deserialize("§e레드팀"), null));
        inventory.setItem(16, ItemUtil.createItem(Material.BLUE_CONCRETE, LegacyComponentSerializer.legacySection().deserialize("§e블루팀"), null));

        ItemStack blackStainedGlassPane = ItemUtil.createItem(Material.BLACK_STAINED_GLASS_PANE, Component.text(" "), null);
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
