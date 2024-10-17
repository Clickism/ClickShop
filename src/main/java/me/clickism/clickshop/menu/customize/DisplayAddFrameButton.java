package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.FrameDisplay;
import me.clickism.clickshop.shop.display.ShopDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplayAddFrameButton extends ModifyDisplayButton {

    public DisplayAddFrameButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.FRAME);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_DISPLAY_ADD_FRAME, Material.ITEM_FRAME, false);

    @Override
    public ItemStack getItem() {
        ShopDisplay display = getShop().getDisplay();
        if (display instanceof FrameDisplay) {
            Material material = ((FrameDisplay) display).getFrame();
            if (material != null) {
                item.setType(material);
            }
        }
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
        ItemStack cursor = event.getCursor();
        if (!isBlockAndNotify(cursor, player)) {
            return;
        }
        FrameDisplay display = (FrameDisplay) shop.getDisplay();
        if (display == null) return;
        display.setFrame(cursor.getType());
        Message.FRAME_ADD_FRAME.send(player);
        sendDisplayParticle(player);
        // Update inventory
        event.getCurrentItem().setType(cursor.getType());
    }
}
