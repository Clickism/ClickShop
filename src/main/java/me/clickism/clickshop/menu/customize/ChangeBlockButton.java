package me.clickism.clickshop.menu.customize;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.ShopButton;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ChangeBlockButton extends ShopButton {

    private final Block shopBlock;

    public ChangeBlockButton(int slot, ItemShop shop) {
        super(slot, shop);
        this.shopBlock = shop.getLocation().getBlock();
        this.item = createItem(
                Message.BUTTON_CHANGE_BLOCK, shopBlock.getType(), false
        );
    }

    private final ItemStack item;

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

        ItemStack item = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        if (item == null) return;
        if (cursor == null) return;

        if (!cursor.getType().isBlock() || cursor.getType() == Material.AIR) {
            Message.NOT_BLOCK.send(player);
            return;
        }

        Material newType = cursor.getType();
        Message.BLOCK_CHANGE.parameterizer()
                .put("block", Utils.capitalize(newType.toString().replace('_', ' ').toLowerCase()))
                .send(player);
        shopBlock.setType(newType);
        if (shopBlock.getBlockData() instanceof Directional) {
            Directional blockData = (Directional) shopBlock.getBlockData();
            if (player.getLocation().getPitch() < -45) {
                // Looking up
                blockData.setFacing(BlockFace.UP);
            } else if (player.getLocation().getPitch() > 45) {
                // Looking down
                blockData.setFacing(BlockFace.DOWN);
            } else {
                blockData.setFacing(player.getFacing().getOppositeFace());
            }
            shopBlock.setBlockData(blockData);
        }

        //Reduce cursor item amount
        cursor.setAmount(cursor.getAmount() - 1);
        if (cursor.getAmount() <= 0) {
            event.setCursor(new ItemStack(Material.AIR));
        } else {
            event.setCursor(cursor);
        }

        shop.updateDisplay();

        Utils.addItem(player, new ItemStack(item.getType()));
        // Update inventory
        item.setType(newType);
    }
}
