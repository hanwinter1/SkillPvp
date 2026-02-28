package kr.hanwinter.skillpvp.game.gui;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.manager.GameManager;
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

        ItemStack soloMatchItem = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemMeta soloMatchItemMeta = soloMatchItem.getItemMeta();
        soloMatchItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e개인전").decoration(TextDecoration.ITALIC, false));
        soloMatchItem.setItemMeta(soloMatchItemMeta);
        inventory.setItem(10, soloMatchItem);

        ItemStack teamMatchItem = new ItemStack(Material.IRON_SWORD, 1);
        ItemMeta teamMatchItemMeta = teamMatchItem.getItemMeta();
        teamMatchItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e팀전").decoration(TextDecoration.ITALIC, false));
        teamMatchItem.setItemMeta(teamMatchItemMeta);
        inventory.setItem(13, teamMatchItem);

        ItemStack practiceItem = new ItemStack(Material.SHIELD, 1);
        ItemMeta practiceItemMeta = practiceItem.getItemMeta();
        practiceItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e연습장").decoration(TextDecoration.ITALIC, false));
        practiceItem.setItemMeta(practiceItemMeta);
        inventory.setItem(16, practiceItem);

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
