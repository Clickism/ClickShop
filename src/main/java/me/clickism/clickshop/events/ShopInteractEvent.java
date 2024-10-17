package me.clickism.clickshop.events;

import me.clickism.clickshop.Debug;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.buy.BuyMenu;
import me.clickism.clickshop.menu.delete.DeleteShopMenu;
import me.clickism.clickshop.menu.edit.EditShopMenu;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class ShopInteractEvent extends InteractEvent {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handleInteract(event);
    }

    @Override
    protected boolean onInteract(Player player, Block block) {
        ItemShop shop = ItemShop.get(block.getLocation());
        if (shop == null) return false;
        boolean sneaking = player.isSneaking();
        //Edit Menu
        if (shop.isOwner(player) && (!Debug.CAN_BUY_FROM_OWN_SHOP || sneaking)) {
            new EditShopMenu(player, shop).open();
            player.playSound(player, Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, .5f, .8f);
            return true;
        }
        // Delete by Operator
        if (Permission.DELETE.has(player) && sneaking) {
            new DeleteShopMenu(player, shop).open();
            return true;
        }
        //Buy Menu
        if (Permission.BUY.lacksAndNotify(player)) {
            return true;
        }
        new BuyMenu(player, shop).open();
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, .5f, .8f);
        return true;
    }
}
