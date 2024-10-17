package me.clickism.clickshop.menu;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.menu.change.ItemButton;
import me.clickism.clickshop.menu.create.PriceButton;
import me.clickism.clickshop.menu.create.ProductButton;
import me.clickism.clickshop.shop.ItemShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ShopMenu extends Menu {

    private final ItemShop shop;

    public ShopMenu(Player player, ItemShop shop, int size, ClickHandler clickHandler, Message title) {
        this(player, shop, size, clickHandler, title.toString());
    }

    public ShopMenu(Player player, ItemShop shop, int size, ClickHandler clickHandler, String title) {
        this(player, shop, size, clickHandler, shop.getColor(), title);
    }

    public ShopMenu(Player player, ItemShop shop, int size, ClickHandler clickHandler, MenuColor color, String title) {
        super(player, shop.getLocation(), size, clickHandler, color, title);
        this.shop = shop;
    }

    protected ItemShop getShop() {
        return shop;
    }

    protected void addPriceAndProductButtons(boolean productButtons) {
        List<ItemStack> products = shop.getProducts();
        ItemStack price = shop.getPrice();

        //Display Price
        if (price != null) {
            addButton(new ItemButton(PRICE_SLOT, price));
        } else {
            addButton(new PriceButton(PRICE_SLOT));
        }

        //Display Products
        for (int i = 0; i < PRODUCT_SLOTS.size(); i++) {
            int slot = PRODUCT_SLOTS.get(i);
            if (i < products.size()) {
                ItemStack item = products.get(i);
                if (item != null) {
                    addButton(new ItemButton(slot, item));
                    continue;
                }
            }
            if (productButtons) {
                addButton(new ProductButton(slot));
            } else {
                addButton(new ItemButton(slot, Material.AIR));
            }
        }
    }
}
