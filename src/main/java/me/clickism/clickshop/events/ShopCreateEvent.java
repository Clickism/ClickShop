package me.clickism.clickshop.events;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.create.CreateShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

public class ShopCreateEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Location location = block.getLocation();

        if (ItemShop.get(location) != null) {
            return; //Ignore if shop
        }
        if (Main.getMain().getShopManager().getStockpileOwner(location) != null) {
            return; //Ignore if stockpile
        }
        Material hand = player.getInventory().getItemInMainHand().getType();
        if (!Tag.ALL_SIGNS.isTagged(hand)) {
            return;
        }
        if (!isValidShopHolderAndWarn(block, player)) {
            return;
        }
        //Create Shop
        event.setCancelled(true);
        if (Main.getMain().getShopManager().isShopLimitReachedForAndWarn(player)) return;
        if (Permission.CREATE.lacksAndNotify(player)) {
            return;
        }
        new CreateShopMenu(player, location).open();
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, .5f, .8f);
    }

    private boolean isValidShopHolderAndWarn(Block block, Player player) {
        Material mat = block.getType();
        if (mat != Material.CHEST && mat != Material.BARREL) {
            return false;
        }
        Container container = (Container) block.getState();
        InventoryHolder holder = container.getInventory().getHolder();
        if (holder instanceof DoubleChest) {
            Message.CREATE_DOUBLE_CHEST.send(player);
            return false;
        }
        if (!container.getInventory().isEmpty()) {
            Message.CREATE_NOT_EMPTY.send(player);
            return false;
        }
        return holder instanceof Chest || holder instanceof Barrel;
    }
}
