package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.GlassDisplay;
import me.clickism.clickshop.shop.display.ShopDisplay;
import me.clickism.clickshop.shop.display.ShopDisplayType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class DisplayChangeGlassButton extends ModifyDisplayButton {

    public DisplayChangeGlassButton(int slot, ItemShop shop) {
        super(slot, shop, ShopDisplayType.GLASS);
    }

    private final ItemStack item =
            createItem(Message.BUTTON_DISPLAY_CHANGE_GLASS, Material.GLASS, false);

    @Override
    public ItemStack getItem() {
        ItemShop shop = getShop();
        ShopDisplay display = shop.getDisplay();
        if (display instanceof GlassDisplay) {
            Material material = ((GlassDisplay) display).getGlassMaterial();
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
        if (cursor == null) return;
        GlassDisplay display = (GlassDisplay) shop.getDisplay();
        if (display == null) return;
        if (!display.setGlassMaterial(cursor.getType())) {
            Message.GLASS_NOT_GLASS.send(player);
            return;
        }

        Message.GLASS_CHANGE_GLASS.send(player);
        sendDisplayParticle(player);
        // Update inventory
        event.getCurrentItem().setType(cursor.getType());

    }
}
