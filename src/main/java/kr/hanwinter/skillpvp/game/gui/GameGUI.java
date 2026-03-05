package kr.hanwinter.skillpvp.game.gui;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.manager.GameManager;
import kr.hanwinter.skillpvp.game.util.ItemUtil;
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

public class GameGUI implements InventoryHolder {
    private final Inventory inventory;

    public GameGUI() {
        inventory = Bukkit.createInventory(this, 27, Component.text("게임 메뉴"));

        inventory.setItem(10, ItemUtil.createItem(Material.WOODEN_SWORD, LegacyComponentSerializer.legacySection().deserialize("§e개인전").decoration(TextDecoration.ITALIC, false), null));
        inventory.setItem(13, ItemUtil.createItem(Material.IRON_SWORD, LegacyComponentSerializer.legacySection().deserialize("§e팀전").decoration(TextDecoration.ITALIC, false), null));
        inventory.setItem(16, ItemUtil.createItem(Material.SHIELD, LegacyComponentSerializer.legacySection().deserialize("§e연습장").decoration(TextDecoration.ITALIC, false), null));

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

    public static class GameGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof GameGUI) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                switch(event.getRawSlot()) {
                    case 10:
                        if(Main.getUserManager().getUser(player.getUniqueId()).getCurrentJob() == null) {
                            player.closeInventory();
                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c직업을 선택하세요."));
                            break;
                        }
                        player.closeInventory();
                        Main.getGameManager().soloMatchJoin(player);
                        break;
                    case 13:
                        // 미구현
                        break;
                    case 16:
                        if(Main.getUserManager().getUser(player.getUniqueId()).getCurrentJob() == null) {
                            player.closeInventory();
                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c직업을 선택하세요."));
                            break;
                        }
                        player.closeInventory();
                        Main.getUserManager().userTeleport(player, GameLocation.PRACTICE);
                        player.give(GameManager.getSkillItem());
                        Main.getUserManager().getUser(player.getUniqueId()).makeCooldownBar(player);
                        Main.getUserManager().getUser(player.getUniqueId()).getCooldownBar().addPlayer(player);
                }
            }
        }
    }
}
