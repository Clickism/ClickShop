package me.clickism.clickshop.shop;

import me.clickism.clickshop.Debug;
import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.utils.HoverableMessageParametizer;
import me.clickism.clickshop.utils.MessageParametizer;
import me.clickism.clickshop.utils.Utils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Transaction {
    private final Player buyer;
    private final ItemShop shop;

    /**
     * Use the make() method to make the transaction
     *
     * @param buyer buyer
     * @param shop  shop to buy from
     */
    public Transaction(Player buyer, ItemShop shop) {
        this.buyer = buyer;
        this.shop = shop;
    }

    private static final String HOVER_LINE_FORMAT = "&f%dx &a%s\n";

    /**
     * Handles all messages.
     *
     * @return if transaction is successful
     */
    public boolean make() {
        if (!shop.hasStock()) {
            //No stock
            Message.BUY_NO_STOCK.send(buyer);
            buyer.closeInventory();
            return false;
        }

        List<ItemStack> products = shop.getProducts();
        ItemStack price = shop.getPrice();

        if (!buyer.getInventory().containsAtLeast(price, price.getAmount())) {
            //No money
            Message.BUY_NO_MONEY.send(buyer);
            buyer.closeInventory();
            return false;
        }

        //Do transaction
        Inventory from = shop.getNextStockedInventory();
        if (!shop.isAdminShop()) { // Skip removing item if admin shop
            products.forEach(product -> {
                from.removeItem(product);
                Utils.addItem(buyer, product);
            });
        } else {
            products.forEach(product -> {
                Utils.addItem(buyer, product);
            });
        }
        buyer.getInventory().removeItem(price);
        shop.incrementEarnings();
        shop.saveData();

        //Buy message
        ItemStack product = products.get(0);
        StringBuilder boughtProducts = new StringBuilder();
        products.forEach(item -> boughtProducts.append(Utils.formatItemWithAmount(HOVER_LINE_FORMAT, item)));
        boolean hasCustomName = shop.hasCustomName();
        String customName = shop.getCustomName() != null ? shop.getCustomName() : "";
        String underlinedCustomName = Utils.colorize("&a&l&n" + customName);
        String shopAt = new MessageParametizer()
                .setMessage(hasCustomName ? Message.SHOP_AT_CUSTOM : Message.SHOP_AT)
                .put("name", customName)
                .put("location", Utils.formatLocation(shop.getLocation()))
                .toString();
        String shopDetails = Message.SHOP_DETAILS.parameterizer()
                .put("location", Utils.formatLocation(shop.getLocation()))
                .put("owner", shop.getOwnerName())
                .toString();

        BaseComponent[] customNameHover = getHoverText(underlinedCustomName, shopDetails);
        BaseComponent[] productsHover = getHoverText(Message.HOVER_VARIOUS_PRODUCTS.toString(),
                Utils.colorize(boughtProducts.toString()));
        BaseComponent[] shopHover = getHoverText(Message.HOVER_YOUR_SHOP.toString(), shopAt);

        HoverableMessageParametizer parameterizer = Message.BUY_SINGLE.parameterizer()
                .put("seller", shop.getOwnerName())
                .put("buyer", buyer.getDisplayName())
                .put("price", Utils.formatItemWithAmount(price))
                .put("product", Utils.formatItemWithAmount(product))
                .hoverable()
                .putHover("hoverVariousProducts", productsHover)
                .putHover("hoverYourShop", shopHover)
                .putHover("hoverCustomName", customNameHover);

        //Send buy message
        BuySound buySound = shop.getBuySound();
        Message buyerMessage;
        if (shop.getProducts().size() <= 1) {
            buyerMessage = hasCustomName ? Message.BUY_SINGLE_CUSTOM : Message.BUY_SINGLE;
        } else {
            buyerMessage = hasCustomName ? Message.BUY_MULTIPLE_CUSTOM : Message.BUY_MULTIPLE;
        }
        parameterizer.setMessage(buyerMessage).sendSilently(buyer);
        buySound.playSound(buyer);

        Player owner = Bukkit.getPlayer(shop.getOwnerUUID());
        if (owner == null || (shop.isAdminShop() && !Debug.SEND_ADMIN_SHOP_NOTIFICATIONS)) return true;
        if (!shop.isNotificationsEnabled()) return true;

        Message ownerMessage;
        if (shop.getProducts().size() <= 1) {
            ownerMessage = Message.SELL_SINGLE;
        } else {
            ownerMessage = Message.SELL_MULTIPLE;
        }
        parameterizer.setMessage(ownerMessage).sendSilently(owner);
        buySound.playSound(owner);

        // Just ran out of stock
        if (!shop.hasStock()) {
            Main.getMain().getShopManager().addWarning(shop);
        }
        return true;
    }

    private BaseComponent[] getHoverText(@NotNull String message, @NotNull String hoverText) {
        BaseComponent[] messageComponents = TextComponent.fromLegacyText(message);
        BaseComponent[] hoverComponents = TextComponent.fromLegacyText(hoverText);
        for (BaseComponent messageComponent : messageComponents) {
            messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponents)));
        }
        return messageComponents;
    }
}
