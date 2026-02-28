package kr.hanwinter.skillpvp.user.gui;

import kr.hanwinter.skillpvp.Main;
import kr.hanwinter.skillpvp.game.GameLocation;
import kr.hanwinter.skillpvp.game.gui.GameGUI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class UserMenuGUI implements InventoryHolder {
    private final Inventory inventory;

    public UserMenuGUI(UUID uuid) {
        inventory = Bukkit.createInventory(this, 27, Component.text("메뉴"));

        if(Main.getUserManager().getUser(uuid).getLocation() == GameLocation.LOBBY) {
            ItemStack jobSelectItem = new ItemStack(Material.BOOK, 1);
            ItemMeta jobSelectItemMeta = jobSelectItem.getItemMeta();
            jobSelectItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e직업 선택").decoration(TextDecoration.ITALIC, false));
            jobSelectItem.setItemMeta(jobSelectItemMeta);
            inventory.setItem(10, jobSelectItem);

            ItemStack gameStartItem = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta gameStartItemMeta = gameStartItem.getItemMeta();
            gameStartItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e게임 시작").decoration(TextDecoration.ITALIC, false));
            gameStartItem.setItemMeta(gameStartItemMeta);
            inventory.setItem(13, gameStartItem);

            ItemStack achievementItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
            ItemMeta achievementItemMeta = achievementItem.getItemMeta();
            achievementItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e업적").decoration(TextDecoration.ITALIC, false));
            achievementItem.setItemMeta(achievementItemMeta);
            inventory.setItem(16, achievementItem);
        }

        if(Main.getUserManager().getUser(uuid).getLocation() == GameLocation.PRACTICE) {
            ItemStack lobbyItem = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta lobbyItemMeta = lobbyItem.getItemMeta();
            lobbyItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§e로비").decoration(TextDecoration.ITALIC, false));
            lobbyItem.setItemMeta(lobbyItemMeta);
            inventory.setItem(13, lobbyItem);
        }

        if(Main.getUserManager().getUser(uuid).getLocation() == GameLocation.SOLO_MATCH_WAITING) {
            ItemStack lobbyItem = new ItemStack(Material.NETHER_STAR, 1);
            ItemMeta lobbyItemMeta = lobbyItem.getItemMeta();
            lobbyItemMeta.displayName(LegacyComponentSerializer.legacySection().deserialize("§c취소").decoration(TextDecoration.ITALIC, false));
            lobbyItem.setItemMeta(lobbyItemMeta);
            inventory.setItem(13, lobbyItem);
        }

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

    public static class UserMenuGUIListener implements Listener {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event) {
            if(event.getInventory().getHolder() instanceof UserMenuGUI) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                if(Main.getUserManager().getUser(player.getUniqueId()).getLocation() == GameLocation.LOBBY) {
                    switch (event.getRawSlot()) {
                        case 10:
                            player.openInventory(new UserJobSelectGUI(Main.getUserManager().getUser(player.getUniqueId())).getInventory());
                            break;
                        case 13:
                            player.openInventory(new GameGUI().getInventory());
                            break;
                        case 16:
                            player.closeInventory();
                            player.sendMessage(LegacyComponentSerializer.legacySection().deserialize("§c제작 중입니다."));
                    }
                }
                if(Main.getUserManager().getUser(player.getUniqueId()).getLocation() == GameLocation.PRACTICE) {
                    if (event.getRawSlot() == 13) {
                        Main.getUserManager().userTeleport(player, GameLocation.LOBBY);
                        player.closeInventory();
                        player.getInventory().clear();
                        Main.getUserManager().getUser(player.getUniqueId()).removeCooldownBar(player);
                    }
                }
                if(Main.getUserManager().getUser(player.getUniqueId()).getLocation() == GameLocation.SOLO_MATCH_WAITING) {
                    if (event.getRawSlot() == 13) {
                        Main.getGameManager().soloMatchQuit(player);
                        player.closeInventory();
                    }
                }
            }
        }

        @EventHandler
        public void onSwitchHand(PlayerSwapHandItemsEvent event) {
            if(event.getPlayer().isSneaking()) {
                event.setCancelled(true);
                event.getPlayer().openInventory(new UserMenuGUI(event.getPlayer().getUniqueId()).getInventory());
            }
        }
    }
}
