package me.clickism.clickshop.events;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ShopBreakEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        ItemShop shop = ItemShop.get(event.getBlock().getLocation());
        if (shop == null) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        // Break by owner
        if (shop.isOwner(player)) {
            Message.BREAK_CONFIRM.send(player);
            return;
        }
        // No permissions
        if (Permission.DELETE.lacks(player)) {
            Message.SHOP_NO_BREAK.send(player);
            return;
        }
        // Break by operator
        Message.BREAK_OPERATOR_CONFIRM.send(player);
    }
}
