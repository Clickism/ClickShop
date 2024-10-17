package me.clickism.clickshop.events;

import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

public class ExplodeEvent implements Listener {

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        List<Block> blockList = event.blockList();
        blockList.removeIf(block ->
                ItemShop.get(block.getLocation()) != null || Pile.get(block.getLocation()) != null
        );
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        List<Block> blockList = event.blockList();
        blockList.removeIf(block ->
                ItemShop.get(block.getLocation()) != null || Pile.get(block.getLocation()) != null
        );
    }
}
