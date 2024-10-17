package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.FrameDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplayMoveToTopButton extends ModifyDisplayButton {

    public DisplayMoveToTopButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.FRAME);
    }

    private final ItemStack item =
            createItem(
                    Message.BUTTON_DISPLAY_MOVE_TO_TOP,
                    Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        FrameDisplay display = (FrameDisplay) getShop().getDisplay();
        if (display == null) return;
        display.moveToTop();
        Message.DISPLAY_FRAME_BUILD.sendSilently(player);
        playBuildDisplaySound(player);
        sendDisplayParticle(player);
    }
}
