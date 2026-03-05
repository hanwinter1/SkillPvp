package kr.hanwinter.skillpvp.game.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemUtil {
    public static ItemStack createItem(Material material, Component displayName, List<Component> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(displayName != null) {
            itemMeta.displayName(displayName);
        }
        if(lore != null) {
            itemMeta.lore(lore);
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
