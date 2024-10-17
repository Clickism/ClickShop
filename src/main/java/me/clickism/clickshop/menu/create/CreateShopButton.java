package me.clickism.clickshop.menu.create;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.ChangeShopButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CreateShopButton extends ChangeShopButton {

    private final Location location;

    public CreateShopButton(int slot, Location location) {
        super(slot, null);
        this.location = location;
    }

    private final ItemStack item = createItem(
            Message.BUTTON_CREATE_SHOP, Material.ANVIL, false
    );

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void clickSound(Player player) {
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        //Shop logic
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();

        List<ItemStack> products = getProducts(event.getInventory());
        ItemStack price = getPrice(event.getInventory());

        player.closeInventory();

        if (Permission.CREATE.lacksAndNotify(player)) {
            return;
        }
        if (products == null || price == null) {
            Message.CREATE_INVALID.send(player);
            return;
        }
        // Fail if shop already exists at location
        if (ItemShop.get(location) != null) {
            Message.CREATE_SHOP_EXIST.send(player);
            return;
        }

        ItemShop shop = new ItemShop(location, player, price, products);
        Main.getMain().getShopManager().registerShop(shop);
        shop.saveData();

        Message.CREATE_SHOP.sendSilently(player);
        player.playSound(location, Sound.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, .5f, 1f);
    }
}
