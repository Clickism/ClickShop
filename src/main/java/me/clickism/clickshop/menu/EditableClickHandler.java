package me.clickism.clickshop.menu;

import me.clickism.clickshop.menu.create.PriceButton;
import me.clickism.clickshop.menu.create.ProductButton;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

import static me.clickism.clickshop.menu.Menu.EDITABLE_SLOTS;
import static me.clickism.clickshop.menu.Menu.PRICE_SLOT;

public class EditableClickHandler extends ClickHandler {

    @Override
    public boolean handleClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        if (!EDITABLE_SLOTS.contains(slot)) {
            return super.handleClick(event);
        }

        event.setCancelled(true);
        Inventory inv = event.getInventory();
        ItemStack cursor = event.getCursor();

        Player player = (Player) event.getWhoClicked();

        if (cursor == null || cursor.getType() == Material.AIR) {
            ItemStack clicked = event.getCurrentItem();
            if (event.getClick() == ClickType.RIGHT && clicked.getAmount() > 1) {
                // Half item count if right click
                clicked.setAmount(clicked.getAmount() / 2);
            } else {
                inv.setItem(slot, createEmptyButton(slot).getItem());
            }
            player.playSound(player, Sound.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1, 1);
        } else {
            ItemStack copy = cursor.clone();
            if (event.getClick() == ClickType.RIGHT) {
                // Put single item
                ItemStack clicked = event.getCurrentItem();
                int amount = clicked.isSimilar(cursor) ? clicked.getAmount() : 0;
                copy.setAmount(amount + 1);
            }
            inv.setItem(slot, copy);
            player.playSound(player, Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 1, 1);
        }
        return false;
    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        Set<Integer> slots = event.getRawSlots();

        if (slots.size() != 1) {
            super.handleDrag(event);
            return;
        }

        int slot = slots.iterator().next();

        if (!EDITABLE_SLOTS.contains(slot)) {
            super.handleDrag(event);
            return;
        }

        event.setCancelled(true);

        Inventory inventory = event.getInventory();
        ItemStack oldItem = inventory.getItem(slot);
        ItemStack newItem = event.getNewItems().get(slot);

        if (oldItem == null || newItem == null) return;

        int oldAmount = oldItem.getAmount();
        int newAmount = newItem.getAmount();

        ItemStack copy = event.getOldCursor().clone();
        if (newAmount - oldAmount == 1) {
            // Right click
            copy.setAmount(newAmount);
        }
        // Left click
        event.getInventory().setItem(slot, copy);

        Player player = (Player) event.getWhoClicked();
        player.playSound(player, Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 1, 1);
    }

    private Button createEmptyButton(int slot) {
        if (slot == PRICE_SLOT) {
            return new PriceButton(slot);
        }
        return new ProductButton(slot);
    }
}
