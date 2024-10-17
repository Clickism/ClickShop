package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.FrameDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplayRemoveFrameButton extends ModifyDisplayButton {

    public DisplayRemoveFrameButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.FRAME);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_DISPLAY_REMOVE_FRAME, Material.STRUCTURE_VOID, false);

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void clickSound(Player player) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();
        FrameDisplay display = (FrameDisplay) shop.getDisplay();
        if (display == null) return;
        display.setFrame(Material.AIR);
        Message.FRAME_REMOVE_FRAME.send(player);
        new CustomizeShopMenu(player, shop).open();

    }
}
