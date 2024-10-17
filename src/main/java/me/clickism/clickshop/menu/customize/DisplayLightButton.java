package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplayLightButton extends ModifyDisplayButton {

    @Override
    public void clickSound(Player player) {
    }

    private final Block blockAbove;

    public DisplayLightButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.GLASS);
        blockAbove = getShop().getLocation().getBlock().getRelative(BlockFace.UP);
    }

    private final ItemStack removeLightButton =
            createItem(Message.BUTTON_DISPLAY_REMOVE_LIGHT, Material.GLOWSTONE_DUST, false);
    private final ItemStack addLightButton =
            createItem(Message.BUTTON_DISPLAY_ADD_LIGHT, Material.GUNPOWDER, false);

    @Override
    public ItemStack getItem() {
        if (blockAbove.getType() == Material.LIGHT) {
            return removeLightButton;
        }
        return addLightButton;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        if (blockAbove.getType() == Material.LIGHT) {
            // Remove light
            blockAbove.setType(Material.AIR);
            MessageType.WARN.playSound(player);
            sendDisplayParticle(player);
        } else if (blockAbove.getType() == Material.AIR) {
            // Add light
            blockAbove.setType(Material.LIGHT);
            MessageType.CONFIRM.playSound(player);
            sendDisplayParticle(player);
        } else {
            // Not empty
            Message.DISPLAY_GLASS_INVALID_LIGHT.send(player);
            return;
        }

        event.setCurrentItem(getItem());
    }
}
