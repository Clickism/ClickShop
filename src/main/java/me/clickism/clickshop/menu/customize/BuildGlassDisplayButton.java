package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.MessageType;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.display.GlassDisplay;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class BuildGlassDisplayButton extends DisplayButton {

    public BuildGlassDisplayButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    @Override
    public void clickSound(Player player) {
    }

    private final ItemStack item = createItem(
            Message.BUTTON_GLASS_DISPLAY, Material.GLASS, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemShop shop = getShop();

        if (shop.getDisplay() instanceof GlassDisplay) {
            MessageType.FAIL.playSound(player);
            return;
        }
        if (Permission.DISPLAY.lacksAndNotify(player)) {
            player.closeInventory();
            return;
        }

        try {
            GlassDisplay display = new GlassDisplay(shop);
            shop.setDisplay(display);
            new CustomizeShopMenu(player, shop).open();
            Message.DISPLAY_GLASS_BUILD.sendSilently(player);
            playBuildDisplaySound(player);
            sendDisplayParticle(player);
        } catch (IllegalArgumentException exception) {
            // Shop block is a chest
            Message.DISPLAY_GLASS_INVALID_BLOCK.send(player);
        }
    }
}
