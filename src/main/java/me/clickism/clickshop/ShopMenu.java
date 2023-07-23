package me.clickism.clickshop;

import me.clickism.clickshop.events.InteractEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopMenu {
    public static ItemStack getPriceButton() {
        return priceButton;
    }

    public static ItemStack getProductButton() {
        return productButton;
    }

    public static ItemStack getBlankButton() {
        return blankButton;
    }

    public static ItemStack getCreateShopButton() {
        return createShopButton;
    }

    public static List<Integer> getCreateShopProductArea() {
        return createShopProductArea;
    }

    public static List<Integer> getCreateShopPriceArea() {
        return createShopPriceArea;
    }

    public static ItemStack getShowBuyButton() {
        return showBuyButton;
    }

    public static ItemStack getStockConnectShopButton() {
        return stockConnectShopButton;
    }

    public static ItemStack getStockSelfShopButton() {
        return stockSelfShopButton;
    }

    public static ItemStack getChangeStyleShopButton() {
        return changeStyleShopButton;
    }

    public static ItemStack getCollectEarningsButton() {
        return collectEarningsButton;
    }
    public static ItemStack getCollectEarningsPileButton() {
        return collectEarningsPileButton;
    }

    public static ItemStack getConnectEarningsButton() {
        return connectEarningsButton;
    }

    public static ItemStack getBuyButton() {
        return buyButton;
    }

    public static ItemStack getNoStockButton() {
        return noStockButton;
    }

    public static ItemStack getPileConnectButton() {
        return pileConnectButton;
    }

    public static ItemStack getChangeShopColorButton() {
        return changeShopColorButton;
    }

    public static ItemStack getChangeChestBlockButton() {
        return changeChestBlockButton;
    }

    public static String getEditShopMenuTitle(Player clicker) {
        Location location = InteractEvent.getLastClickedChestPosition(clicker);
        if (ShopManager.isShopActive(location)) {
            return editShopMenuTitleOpen;
        } else {
            return editShopMenuTitleClosed;
        }
    }
    public static String getBuyMenuTitle(Location location) {
        if (ShopManager.getColor(location) != null) {
            return getColoredTitle(location, ShopManager.getColor(location));
        }
        return null;
    }

    public static String getStyleMenuTitle(Location location) {
        if (ShopManager.getColor(location) != null) {
            return getColoredTitle(ShopManager.getColor(location), styleMenuTitle);
        }
        return null;
    }

    public static String getCreateShopMenuTitle() {
        return createShopMenuTitle;
    }

    public static String getCollectMenuTitle() {
        return collectMenuTitle;
    }

    public static String getSelfStockTitle() {
        return selfStockTitle;
    }

    public static ItemStack getGlassDisplayButton() {
        return glassDisplayButton;
    }

    public static ItemStack getFrameDisplayButton() {
        return frameDisplayButton;
    }

    public static ItemStack getGlowingDisplayButton() {
        return glowingDisplayButton;
    }

    public static ItemStack getDeleteDisplayButton() {
        return deleteDisplayButton;
    }

    public static ItemStack getGlassDisplayBaseButton() {
        return glassDisplayBaseButton;
    }

    public static ItemStack getDeleteButton() {
        return deleteButton;
    }

    public static ItemStack getEditConfirmButton() {
        return editConfirmButton;
    }

    public static String getEditPriceMenuTitle() {
        return editPriceMenuTitle;
    }

    private static ItemStack priceButton;
    private static ItemStack productButton;
    private static ItemStack blankButton;
    private static ItemStack blankProductButton;
    private static ItemStack buyButton;
    private static ItemStack noStockButton;
    private static ItemStack createShopButton;
    private static ItemStack editConfirmButton;
    private static ItemStack showBuyButton;

    private static ItemStack deleteButton;
    private static ItemStack stockConnectShopButton;
    private static ItemStack stockSelfShopButton;
    private static ItemStack changeStyleShopButton;
    private static ItemStack collectEarningsButton;
    private static ItemStack connectEarningsButton;
    private static ItemStack collectEarningsPileButton;
    private static ItemStack pileConnectButton;

    //Color
    private static ItemStack changeShopColorButton;
    private static ItemStack changeChestBlockButton;
    private static ItemStack glassDisplayButton;
    private static ItemStack glassDisplayBaseButton;
    private static ItemStack frameDisplayButton;
    private static ItemStack glowingDisplayButton;
    private static ItemStack deleteDisplayButton;
    private static String createShopMenuTitle = Utils.colorize(" &8&lPrice&6&l / &2&lCreate&6&l / &8&lProduct");
    private static String editPriceMenuTitle = Utils.colorize("  &8&lPrice &6&l/&2&l Save &6&l/ &8&lProduct");
    private static String editShopMenuTitleOpen = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "⚒ Shop open";
    private static String editShopMenuTitleClosed = ChatColor.RED + "" + ChatColor.BOLD + "⚒ Shop out of stock";
    private static String buyMenuTitle = ChatColor.BOLD + "'s Shop";
    private static String collectMenuTitle = ChatColor.BLUE + "" + ChatColor.BOLD + "💰 Earnings pile";
    private static String styleMenuTitle = "🖌 Change style";
    private static String selfStockTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "📦 Stock chest";
    private static List<Integer> createShopProductArea = new ArrayList<>(Arrays.asList(14,15,16,23,24,25,32,33,34));

    private static List<Integer> createShopPriceArea = new ArrayList<>(Arrays.asList(19));

    private static List<Integer> line1 = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8));
    private static List<Integer> line2 = new ArrayList<>(Arrays.asList(9,10,11,12,13,14,15,16,17));
    private static List<Integer> line3 = new ArrayList<>(Arrays.asList(18,19,20,21,22,23,24,25,26));
    private static List<Integer> line4 = new ArrayList<>(Arrays.asList(27,28,29,30,31,32,33,34,35));
    private static List<Integer> line5 = new ArrayList<>(Arrays.asList(36,37,38,39,40,41,42,43,44));

    public static void setPlugin(ClickShop plugin) {
        ShopMenu.plugin = plugin;
    }

    private static ClickShop plugin;

    public static void setupInventories() {

        ItemStack blank = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blankMeta = blank.getItemMeta();
        blankMeta.setDisplayName(ChatColor.DARK_GRAY + "x");
        blank.setItemMeta(blankMeta);
        blankButton = blank;

        ItemStack blankProduct = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta blankProductMeta = blankProduct.getItemMeta();
        blankProductMeta.setDisplayName(ChatColor.GRAY + "x");
        blankProduct.setItemMeta(blankProductMeta);
        blankProductButton = blankProduct;

        ItemStack price = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName(ChatColor.BOLD + "PRICE");
        priceMeta.setLore(Arrays.asList(ChatColor.GRAY + "Put your price for the product you want to sell here."));
        price.setItemMeta(priceMeta);
        priceButton = price;

        ItemStack product = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta productMeta = product.getItemMeta();
        productMeta.setDisplayName(ChatColor.BOLD + "PRODUCT");
        productMeta.setLore(Arrays.asList(ChatColor.GRAY + "Put the product(s) you want to sell here."));
        product.setItemMeta(productMeta);
        productButton = product;

        ItemStack createShopGlass = new ItemStack(Material.ANVIL);
        ItemMeta createShopGlassMeta = createShopGlass.getItemMeta();
        createShopGlassMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "CREATE SHOP");
        createShopGlassMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Click to create a shop."));
        createShopGlass.setItemMeta(createShopGlassMeta);
        createShopButton = createShopGlass;

        ItemStack editConfirm = new ItemStack(Material.ANVIL);
        ItemMeta editConfirmMeta = editConfirm.getItemMeta();
        editConfirmMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "SAVE CHANGES");
        editConfirmMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Click to save changes made to the price and product(s)."));
        editConfirm.setItemMeta(editConfirmMeta);
        editConfirmButton = editConfirm;

        ItemStack buyItem = new ItemStack(Material.LIME_CONCRETE_POWDER);
        ItemMeta buyMeta = buyItem.getItemMeta();
        buyMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "BUY");
        buyMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + "Click to buy this product for the specified price"));
        buyItem.setItemMeta(buyMeta);
        buyButton = buyItem;

        ItemStack showBuy = new ItemStack(Material.ENDER_EYE);
        ItemMeta showBuyMeta = showBuy.getItemMeta();
        showBuyMeta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "CHANGE PRICE AND PRODUCTS");
        showBuyMeta.setLore(Arrays.asList(ChatColor.GREEN + "Click to change or see the price and product(s)", ChatColor.GREEN + "you're selling at this shop."));
        showBuy.setItemMeta(showBuyMeta);
        showBuyButton = showBuy;

        ItemStack delete = new ItemStack(Material.BARRIER);
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "DELETE SHOP");
        deleteMeta.setLore(Arrays.asList(ChatColor.RED + "Click to delete shop."));
        delete.setItemMeta(deleteMeta);
        deleteButton = delete;

        ItemStack noStock = new ItemStack(Material.RED_CONCRETE_POWDER);
        ItemMeta noStockMeta = noStock.getItemMeta();
        noStockMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "OUT OF STOCK");
        noStockMeta.setLore(Arrays.asList(ChatColor.RED + "This shop is out of stock."));
        noStock.setItemMeta(noStockMeta);
        noStockButton = noStock;

        ItemStack stockConnect = new ItemStack(Material.HOPPER);
        ItemMeta stockConnectMeta = stockConnect.getItemMeta();
        stockConnectMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "CONNECT STOCKPILE");
        stockConnectMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to connect a stockpile to the shop.", " ", ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Not: " + ChatColor.GRAY + "" + ChatColor.ITALIC + "You can click on an empty spot", ChatColor.GRAY + "" + ChatColor.ITALIC + "to cancel the stockpile selection."));
        stockConnect.setItemMeta(stockConnectMeta);
        stockConnectShopButton= stockConnect;

        ItemStack pileConnect = new ItemStack(Material.HOPPER);
        ItemMeta pileConnectMeta = pileConnect.getItemMeta();
        pileConnectMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "CONNECT SHOP");
        pileConnectMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to connect a shop to the earnings pile.", " ", ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Not: " + ChatColor.GRAY + "" + ChatColor.ITALIC + "You can click on an empty spot", ChatColor.GRAY + "" + ChatColor.ITALIC + "to cancel the earnings pile selection."));
        pileConnect.setItemMeta(pileConnectMeta);
        pileConnectButton = pileConnect;

        ItemStack stockSelf = new ItemStack(Material.CHEST);
        ItemMeta stockSelfMeta = stockSelf.getItemMeta();
        stockSelfMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "STOCK CHEST");
        stockSelfMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Click to stock the shop's internal storage.", " ", ChatColor.GOLD + "" + ChatColor.ITALIC + "Not: " + ChatColor.YELLOW + "" + ChatColor.ITALIC + "You can use this if you don't", ChatColor.YELLOW + "" + ChatColor.ITALIC + "want to add a stockpile"));
        stockSelf.setItemMeta(stockSelfMeta);
        stockSelfShopButton= stockSelf;

        ItemStack earningsCollect = new ItemStack(Material.DIAMOND);
        ItemMeta earningsCollectMeta = earningsCollect.getItemMeta();
        earningsCollectMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "COLLECT EARNINGS");
        earningsCollectMeta.setLore(Arrays.asList(ChatColor.AQUA + "Click to collect your earnings at this shop."));
        earningsCollect.setItemMeta(earningsCollectMeta);
        collectEarningsButton= earningsCollect;

        ItemStack earningsPileCollect = new ItemStack(Material.DIAMOND);
        ItemMeta earningsPileCollectMeta = earningsPileCollect.getItemMeta();
        earningsPileCollectMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "COLLECT EARNINGS");
        earningsPileCollectMeta.setLore(Arrays.asList(ChatColor.AQUA + "Click to collect your earnings from all the connected shops."));
        earningsPileCollect.setItemMeta(earningsPileCollectMeta);
        collectEarningsPileButton= earningsPileCollect;

        ItemStack connectEarnings = new ItemStack(Material.ENDER_CHEST);
        ItemMeta connectEarningsMeta = connectEarnings.getItemMeta();
        connectEarningsMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "CONNECT EARNINGS PILE");
        connectEarningsMeta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + "Click to connect an earnings pile to collect earnings", ChatColor.LIGHT_PURPLE + "from multiple shops and right click on an Ender Chest.", " ", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Not: " + ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "You can shift + right click to access", ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "the earnings pile"));
        connectEarnings.setItemMeta(connectEarningsMeta);
        connectEarningsButton = connectEarnings;

        ItemStack changeStyle = new ItemStack(Material.BRUSH);
        ItemMeta changeStyleMeta = changeStyle.getItemMeta();
        changeStyleMeta.setDisplayName(Utils.colorize("&c&lC&6&lU&e&lS&a&lT&3&lO&9&lM&c&lI&6&lZ&e&lE &3&lS&9&lH&c&lO&6&lP"));
        changeStyleMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Click to customize the shop."));
        changeStyle.setItemMeta(changeStyleMeta);
        changeStyleShopButton= changeStyle;

        //

        ItemStack changeShopColor = new ItemStack(Material.BLUE_DYE);
        ItemMeta changeShopColorMeta = changeShopColor.getItemMeta();
        changeShopColorMeta.setDisplayName(Utils.colorize("&c&lC&6&lH&e&lA&a&lN&3&lG&9&lE &6&lC&e&lO&a&lL&3&lO&9&lR"));
        changeShopColorMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Click to change the background color."));
        changeShopColor.setItemMeta(changeShopColorMeta);
        changeShopColorButton = changeShopColor;

        ItemStack changeChestBlock = new ItemStack(Material.CHEST);
        ItemMeta changeChestBlockMeta = changeChestBlock.getItemMeta();
        changeChestBlockMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "CHANGE SHOP BLOCK");
        changeChestBlockMeta.setLore(Arrays.asList(ChatColor.YELLOW + "Put the block you want to change the shop block to here."));
        changeChestBlock.setItemMeta(changeChestBlockMeta);
        changeChestBlockButton = changeChestBlock;

        ItemStack glassDisplay = new ItemStack(Material.GLASS);
        ItemMeta glassDisplayMeta = glassDisplay.getItemMeta();
        glassDisplayMeta.setDisplayName(Utils.colorize("&r&lGLASS DISPLAY &6&l// &r&lCHANGE GLASS COLOR"));
        glassDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click with an empty hand to create a glass display or put", ChatColor.GRAY + "the colored glass block you want here to change the glass color."));
        glassDisplay.setItemMeta(glassDisplayMeta);
        glassDisplayButton = glassDisplay;

        ItemStack glassDisplayBase = new ItemStack(Material.STRIPPED_OAK_WOOD);
        ItemMeta glassDisplayBaseMeta = glassDisplayBase.getItemMeta();
        glassDisplayBaseMeta.setDisplayName(Utils.colorize("&r&lGLASS DISPLAY &6&l// &r&lCHANGE BASE"));
        glassDisplayBaseMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click with an empty hand to create a glass display or", ChatColor.GRAY + "put the block you want here to change the display base."));
        glassDisplayBase.setItemMeta(glassDisplayBaseMeta);
        glassDisplayBaseButton = glassDisplayBase;

        ItemStack frameDisplay = new ItemStack(Material.PAINTING);
        ItemMeta frameDisplayMeta = frameDisplay.getItemMeta();
        frameDisplayMeta.setDisplayName(Utils.colorize("&r&e&lFRAME DISPLAY &6&l// &r&e&lADD OUTER FRAME"));
        frameDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click with an empty hand to create a frame display or put", ChatColor.GRAY + "the block you want here to add an outer frame."));
        frameDisplay.setItemMeta(frameDisplayMeta);
        frameDisplayButton = frameDisplay;

        ItemStack brightDisplay = new ItemStack(Material.GLOWSTONE_DUST);
        ItemMeta brightDisplayMeta = brightDisplay.getItemMeta();
        brightDisplayMeta.setDisplayName(Utils.colorize("&r&e&lADD LIGHT &6&l// &r&e&lREMOVE LIGHT"));
        brightDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + "Click to add/remove a backlight to the glass display"));
        brightDisplay.setItemMeta(brightDisplayMeta);
        glowingDisplayButton = brightDisplay;

        ItemStack deleteDisplay = new ItemStack(Material.BARRIER);
        ItemMeta deleteDisplayMeta = deleteDisplay.getItemMeta();
        deleteDisplayMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "REMOVE DISPLAY");
        deleteDisplayMeta.setLore(Arrays.asList(ChatColor.RED + "Click to remove the display."));
        deleteDisplay.setItemMeta(deleteDisplayMeta);
        deleteDisplayButton= deleteDisplay;
    }

    public static Inventory getCreateShopMenu() {
        Inventory createShopInv = Bukkit.createInventory(null, 45, getCreateShopMenuTitle());
        createShopInv.setContents(fillColor("black", "").getContents());
        createShopPriceArea.forEach(i -> {
            createShopInv.setItem(i, priceButton);
        });
        createShopProductArea.forEach(i -> {
            createShopInv.setItem(i, productButton);
        });
        createShopInv.setItem(21, createShopButton);

        return createShopInv;
    }

    public static Inventory getEditShopMenu(Player player) {
        Inventory editShopInv;
        Location loc = InteractEvent.getLastClickedChestPosition(player);
        if (ShopManager.isShopActive(loc)) {
            Inventory temp = fillColor(ShopManager.getColor(loc), editShopMenuTitleOpen);
            editShopInv = Bukkit.createInventory(null, 45, editShopMenuTitleOpen);
            editShopInv.setContents(temp.getContents());
        } else {
            Inventory temp = fillColor(ShopManager.getColor(loc), editShopMenuTitleClosed);
            editShopInv = Bukkit.createInventory(null, 45, editShopMenuTitleClosed);
            editShopInv.setContents(temp.getContents());
        }

        editShopInv.setItem(19, showBuyButton);
        editShopInv.setItem(12, connectEarningsButton);
        editShopInv.setItem(14, collectEarningsButton);
        editShopInv.setItem(25, changeStyleShopButton);
        editShopInv.setItem(30, stockConnectShopButton);
        editShopInv.setItem(32, stockSelfShopButton);

        return editShopInv;
    }

    public static Inventory getBuyMenu(Location location) {
        Inventory buyInv = fillColor(location, ShopManager.getColor(location));
        createShopPriceArea.forEach(i -> {
            buyInv.setItem(i, ShopManager.getPrice(location));
        });
        createShopProductArea.forEach(i -> {
            buyInv.setItem(i, blankProductButton);
        });
        List<ItemStack> products = ShopManager.getProducts(location);
        AtomicInteger i = new AtomicInteger();
        products.forEach(product -> {
            buyInv.setItem(createShopProductArea.get(i.getAndIncrement()), product);
        });
        if (ShopManager.hasStock(location)) {
            buyInv.setItem(21, buyButton);
        } else {
            buyInv.setItem(21, noStockButton);
        }
        return buyInv;
    }

    public static Inventory getEditPriceMenu(Location location) {
        Inventory inv = Bukkit.createInventory(null, 45, editPriceMenuTitle);
        inv.setContents(fillColor(location, ShopManager.getColor(location)).getContents());
        createShopPriceArea.forEach(i -> {
            inv.setItem(i, ShopManager.getPrice(location));
        });
        createShopProductArea.forEach(i -> {
            inv.setItem(i, productButton);
        });
        List<ItemStack> products = ShopManager.getProducts(location);
        AtomicInteger i = new AtomicInteger();
        products.forEach(product -> {
            inv.setItem(createShopProductArea.get(i.getAndIncrement()), product);
        });
        inv.setItem(12, editConfirmButton);
        inv.setItem(30, deleteButton);
        return inv;
    }

    public static Inventory getStyleMenu(Location location) {
        Inventory styleInv = fillColor(ShopManager.getColor(location), styleMenuTitle);

        ItemStack black = changeShopColorButton.clone();
        ItemStack blue = changeShopColorButton.clone();
        ItemStack purple = changeShopColorButton.clone();
        ItemStack red = changeShopColorButton.clone();
        ItemStack green = changeShopColorButton.clone();
        ItemStack yellow = changeShopColorButton.clone();
        ItemStack rainbow = changeShopColorButton.clone();
        black.setType(Material.BLACK_DYE);
        blue.setType(Material.BLUE_DYE);
        purple.setType(Material.PURPLE_DYE);
        red.setType(Material.RED_DYE);
        green.setType(Material.LIME_DYE);
        yellow.setType(Material.YELLOW_DYE);
        rainbow.setType(Material.CYAN_DYE);
        styleInv.setItem(10,black);
        styleInv.setItem(11,blue);
        styleInv.setItem(12,purple);
        styleInv.setItem(13,red);
        styleInv.setItem(14,green);
        styleInv.setItem(15,yellow);
        styleInv.setItem(16,rainbow);

        ItemStack glassItem = glassDisplayButton.clone();
        ItemStack baseItem = glassDisplayBaseButton.clone();
        ItemStack frameItem = frameDisplayButton.clone();
        ItemStack chestItem = changeChestBlockButton.clone();
        if (ShopManager.getDisplay(location).equals("glass") || ShopManager.getDisplay(location).equals("glass_glowing")) {
            baseItem.setType(((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(1)))).getBlock().getMaterial());
            glassItem.setType(((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(0)))).getBlock().getMaterial());
        } else if (ShopManager.getDisplay(location).equals("frame")) {
            if (!((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(0)))).getBlock().getMaterial().equals(Material.AIR)) {
                frameItem.setType(((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(0)))).getBlock().getMaterial());
            }
        }
        chestItem.setType(location.getBlock().getType());
        styleInv.setItem(28, chestItem);
        styleInv.setItem(30, glassItem);
        styleInv.setItem(31, baseItem);
        styleInv.setItem(32, frameItem);
        styleInv.setItem(33, glowingDisplayButton);
        styleInv.setItem(34, deleteDisplayButton);

        return styleInv;
    }

    public static Inventory getCollectMenu() {
        Inventory collectInv = Bukkit.createInventory(null, 27, collectMenuTitle);
        for (int i = 0; i < collectInv.getSize(); i++) {
            if (line2.contains(i)) {
                collectInv.setItem(i, blankButton);
            } else {
                ItemStack item = blankButton.clone();
                item.setType(Material.GRAY_STAINED_GLASS_PANE);
                collectInv.setItem(i, item);
            }
        }
        collectInv.setItem(12, pileConnectButton);
        collectInv.setItem(14, collectEarningsPileButton);
        return collectInv;
    }

    public static Inventory fillColor(String color, String title) {
        List<Material> colors = new ArrayList<>(Arrays.asList(
                Material.GRAY_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE));
        if (color.equals("black")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.GRAY_STAINED_GLASS_PANE,
                    Material.BLACK_STAINED_GLASS_PANE,
                    Material.GRAY_STAINED_GLASS_PANE,
                    Material.BLACK_STAINED_GLASS_PANE,
                    Material.GRAY_STAINED_GLASS_PANE));
        } else if (color.equals("blue")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.BLUE_STAINED_GLASS_PANE,
                    Material.CYAN_STAINED_GLASS_PANE,
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.CYAN_STAINED_GLASS_PANE,
                    Material.BLUE_STAINED_GLASS_PANE));
        } else if (color.equals("pink")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.PURPLE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.PINK_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.PURPLE_STAINED_GLASS_PANE));
        }  else if (color.equals("red")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.RED_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.BROWN_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE));
        } else if (color.equals("green")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.GREEN_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.GREEN_STAINED_GLASS_PANE));
        } else if (color.equals("yellow")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE));
        } else if (color.equals("rainbow")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE));
        }
        List<Material> finalColors = colors;
        Inventory inv = Bukkit.createInventory(null, 45, getColoredTitle(color, title));
        line1.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(0));
            inv.setItem(slot, item);
        });
        line2.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(1));
            inv.setItem(slot, item);
        });
        line3.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(2));
            inv.setItem(slot, item);
        });
        line4.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(3));
            inv.setItem(slot, item);
        });
        line5.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(4));
            inv.setItem(slot, item);
        });
        return inv;
    }

    public static String getColoredTitle(String color, String title) {
        String coloredTitle = "";
        if (color.equals("black")) {
            coloredTitle = ChatColor.DARK_GRAY + "" + ChatColor.BOLD + title;
        } else if (color.equals("blue")) {
            coloredTitle = ChatColor.BLUE + "" + ChatColor.BOLD + title;
        } else if (color.equals("pink")) {
            coloredTitle = ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + title;
        } else if (color.equals("red")) {
            coloredTitle = ChatColor.RED + "" + ChatColor.BOLD + title;
        } else if (color.equals("green")) {
            coloredTitle = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + title;
        } else if (color.equals("yellow")) {
            coloredTitle = ChatColor.GOLD + "" + ChatColor.BOLD + title;
        } else if (color.equals("rainbow")) {
            coloredTitle = ChatColor.BLUE + "" + ChatColor.BOLD + title;
        }
        return coloredTitle;
    }

    public static Inventory fillColor(Location location, String color) {
        List<Material> colors = new ArrayList<>(Arrays.asList(
                Material.GRAY_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE,
                Material.BLACK_STAINED_GLASS_PANE,
                Material.GRAY_STAINED_GLASS_PANE));
        if (color.equals("black")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.GRAY_STAINED_GLASS_PANE,
                    Material.BLACK_STAINED_GLASS_PANE,
                    Material.GRAY_STAINED_GLASS_PANE,
                    Material.BLACK_STAINED_GLASS_PANE,
                    Material.GRAY_STAINED_GLASS_PANE));
        } else if (color.equals("blue")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.BLUE_STAINED_GLASS_PANE,
                    Material.CYAN_STAINED_GLASS_PANE,
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.CYAN_STAINED_GLASS_PANE,
                    Material.BLUE_STAINED_GLASS_PANE));
        } else if (color.equals("pink")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.PURPLE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.PINK_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.PURPLE_STAINED_GLASS_PANE));
        } else if (color.equals("green")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.GREEN_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.GREEN_STAINED_GLASS_PANE));
        } else if (color.equals("red")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.RED_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.BROWN_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE));
        } else if (color.equals("yellow")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE));
        } else if (color.equals("rainbow")) {
            colors = new ArrayList<>(Arrays.asList(
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE));
        }
        List<Material> finalColors = colors;
        Inventory inv = Bukkit.createInventory(null, 45, getColoredTitle(location, color));
        line1.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(0));
            inv.setItem(slot, item);
        });
        line2.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(1));
            inv.setItem(slot, item);
        });
        line3.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(2));
            inv.setItem(slot, item);
        });
        line4.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(3));
            inv.setItem(slot, item);
        });
        line5.forEach(slot -> {
            ItemStack item = blankButton.clone();
            item.setType(finalColors.get(4));
            inv.setItem(slot, item);
        });
        return inv;
    }

    public static String getColoredTitle(Location location, String color) {
        String coloredTitle = "";
        String owner = ShopManager.getOwner(location);
        if (color.equals("black")) {
            coloredTitle = ChatColor.DARK_GRAY + "$ " + ChatColor.BLACK + "" + ChatColor.BOLD + owner + ChatColor.DARK_GRAY + buyMenuTitle;
        } else if (color.equals("blue")) {
            coloredTitle = ChatColor.BLUE + "$ " + ChatColor.DARK_BLUE + "" + ChatColor.BOLD + owner + ChatColor.BLUE + buyMenuTitle;
        } else if (color.equals("pink")) {
            coloredTitle = ChatColor.LIGHT_PURPLE + "$ " + ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + owner + ChatColor.LIGHT_PURPLE + buyMenuTitle;
        } else if (color.equals("green")) {
            coloredTitle = ChatColor.GREEN + "$ " + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + owner + ChatColor.GREEN + buyMenuTitle;
        } else if (color.equals("red")) {
            coloredTitle = ChatColor.RED + "$ " + ChatColor.DARK_RED + "" + ChatColor.BOLD + owner + ChatColor.RED + buyMenuTitle;
        } else if (color.equals("yellow")) {
            coloredTitle = ChatColor.YELLOW + "$ " + ChatColor.GOLD + "" + ChatColor.BOLD + owner + ChatColor.YELLOW + buyMenuTitle;
        } else if (color.equals("rainbow")) {
            coloredTitle = ChatColor.AQUA + "$ " + ChatColor.BLUE + "" + ChatColor.BOLD + owner + ChatColor.AQUA + buyMenuTitle;
        }
        return coloredTitle;
    }
}
