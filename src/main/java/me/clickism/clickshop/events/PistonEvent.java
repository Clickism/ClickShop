package me.clickism.clickshop.events;

import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

public class PistonEvent implements Listener {

    @EventHandler
    public void onPiston(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (ItemShop.get(block.getLocation()) != null || Pile.get(block.getLocation()) != null) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPiston(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (ItemShop.get(block.getLocation()) != null || Pile.get(block.getLocation()) != null) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
