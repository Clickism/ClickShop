package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class Button {

    private final int slot;

    public Button(int slot) {
        this.slot = slot;
    }

    public abstract ItemStack getItem();

    protected abstract void onClick(InventoryClickEvent event);

    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (isValidClick(event)) {
            onClick(event);
            clickSound(player);
        } else {
            MessageType.FAIL.playSound(player);
        }
    }

    public boolean isValidClick(InventoryClickEvent event) {
        return true;
    }

    public void clickSound(Player player) {
        MessageType.CONFIRM.playSound(player);
    }

    public int getSlot() {
        return slot;
    }

    protected static ItemStack createItem(Message title, Material mat, boolean enchanted) {
        return createItem(title.toString(), title.getLore(), mat, enchanted);
    }

    protected static ItemStack createItem(String name, List<String> lore, Material mat, boolean enchanted) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(lore);
        if (enchanted) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        }
        item.setItemMeta(meta);
        return item;
    }

    protected static String rainbow(String string) {
        String prefix = Utils.colorize("&c&l" + string.substring(0, 2));
        return prefix + Utils.rainbow(string.substring(2), true);
    }

    protected static boolean isBlockAndNotify(ItemStack cursor, Player player) {
        if (cursor == null) return false;
        if (!cursor.getType().isBlock() || cursor.getType() == Material.AIR) {
            Message.NOT_BLOCK.send(player);
            return false;
        }
        return true;
    }
}
