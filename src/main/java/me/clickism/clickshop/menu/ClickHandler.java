package me.clickism.clickshop.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static me.clickism.clickshop.menu.Menu.EDITABLE_SLOTS;

public class ClickHandler {

    /**
     * Handles the click on the menu
     *
     * @param event event to handle
     * @return true if the click was processed, false otherwise
     */
    public boolean handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        return true;
    }

    /**
     * Handles the drag on the menu
     *
     * @param event event to handle
     */
    public void handleDrag(InventoryDragEvent event) {
        int size = event.getView().getTopInventory().getSize();
        for (Integer slot : event.getRawSlots()) {
            if (slot < size) {
                event.setCancelled(true);
                return;
            }
        }
    }

    /**
     * Checks if the click event is valid
     *
     * @param event event to validate
     * @return true if click is valid, false otherwise
     */
    public boolean isValidClick(InventoryClickEvent event) {
        if (isIllegalShiftClick(event)) return false;
        if (isIllegalDoubleClick(event)) return false;
        if (event.getClickedInventory() != null &&
                !event.getClickedInventory().equals(event.getView().getTopInventory())) {
            // Clicked outside menu
            return true;
        }
        return event.getCurrentItem() != null;
    }

    protected boolean isIllegalShiftClick(InventoryClickEvent event) {
        if (!event.getClick().isShiftClick()) return false;
        return event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY;
    }

    protected boolean isIllegalDoubleClick(InventoryClickEvent event) {
        if (event.getClick() != ClickType.DOUBLE_CLICK) return false;
        if (event.getAction() != InventoryAction.COLLECT_TO_CURSOR) return false;

        Inventory inv = event.getInventory();
        ItemStack cursor = event.getCursor();

        if (cursor == null) return false;

        for (Integer slot : EDITABLE_SLOTS) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() == cursor.getType()) {
                return true;
            }
        }

        return false;
    }
}
