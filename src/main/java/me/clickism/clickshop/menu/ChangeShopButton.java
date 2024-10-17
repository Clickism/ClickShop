package me.clickism.clickshop.menu;

import me.clickism.clickshop.menu.create.PriceButton;
import me.clickism.clickshop.menu.create.ProductButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class ChangeShopButton extends ShopButton {

    public ChangeShopButton(int slot, ItemShop shop) {
        super(slot, shop);
    }

    @Nullable
    public static List<ItemStack> getProducts(Inventory inv) {
        List<ItemStack> product = new ArrayList<>();
        Menu.PRODUCT_SLOTS.forEach(slot -> {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                if (!item.isSimilar(new ProductButton(0).getItem())) {
                    product.add(item);
                }
            }
        });
        if (product.isEmpty()) return null;
        return product;
    }

    @Nullable
    public static ItemStack getPrice(Inventory inv) {
        ItemStack item = inv.getItem(Menu.PRICE_SLOT);
        if (item == null) return null;
        if (item.getType() == Material.AIR) return null;
        if (item.isSimilar(new PriceButton(0).getItem())) return null;

        return item;
    }

}
