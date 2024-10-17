package me.clickism.clickshop.menu;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class MenuListener implements Listener {

    public final Map<Inventory, Menu> activeMenus = new HashMap<>();

    public void registerActiveMenu(Inventory inv, Menu menu) {
        activeMenus.put(inv, menu);
    }

    public void unregisterActiveMenu(Inventory inv) {
        activeMenus.remove(inv);
    }

    public void closeActiveMenus() {
        Iterator<Map.Entry<Inventory, Menu>> iterator = activeMenus.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Inventory, Menu> entry = iterator.next();
            iterator.remove();
            List<HumanEntity> viewers = entry.getKey().getViewers();
            new ArrayList<>(viewers).forEach(HumanEntity::closeInventory);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Menu menu = activeMenus.get(event.getInventory());
        if (menu != null) menu.onClick(event);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        Menu menu = activeMenus.get(event.getInventory());
        if (menu != null) menu.onDrag(event);
    }


    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Menu menu = activeMenus.get(inv);
        if (menu != null) {
            unregisterActiveMenu(inv);
            menu.onClose(event);
        }
    }
}
