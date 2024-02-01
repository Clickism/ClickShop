package me.clickism.clickshop;

import me.clickism.clickshop.LocalizationManager;
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
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ShopMenu {
    private LocalizationManager localizationManager;
    private FileConfiguration langConfig;

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
        LocalizationManager localizationManager = new LocalizationManager();
        localizationManager.setup();
        FileConfiguration langConfig = localizationManager.getLangConfig();

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

        ItemStack price = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE); //Re
        ItemMeta priceMeta = price.getItemMeta();
        priceMeta.setDisplayName(ChatColor.BOLD + langConfig.getString("price.title"));
        priceMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("price.lore")));
        price.setItemMeta(priceMeta);
        priceButton = price;
        
        ItemStack product = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE); //Re
        ItemMeta productMeta = product.getItemMeta();
        productMeta.setDisplayName(ChatColor.BOLD + langConfig.getString("product.title"));
        productMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("product.lore")));
        product.setItemMeta(productMeta);
        productButton = product;

        ItemStack createShopGlass = new ItemStack(Material.ANVIL); //Re
        ItemMeta createShopGlassMeta = createShopGlass.getItemMeta();
        createShopGlassMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + langConfig.getString("createShop.title"));
        createShopGlassMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + langConfig.getString("createShop.lore")));
        createShopGlass.setItemMeta(createShopGlassMeta);
        createShopButton = createShopGlass;

        ItemStack editConfirm = new ItemStack(Material.ANVIL); //Re
        ItemMeta editConfirmMeta = editConfirm.getItemMeta();
        editConfirmMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + langConfig.getString("editConfirm.title"));
        editConfirmMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + langConfig.getString("editConfirm.lore")));
        editConfirm.setItemMeta(editConfirmMeta);
        editConfirmButton = editConfirm;

        ItemStack buyItem = new ItemStack(Material.LIME_CONCRETE_POWDER); //Re
        ItemMeta buyMeta = buyItem.getItemMeta();
        buyMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + langConfig.getString("buyItem.title"));
        buyMeta.setLore(Arrays.asList(ChatColor.DARK_GREEN + langConfig.getString("buyItem.lore")));
        buyItem.setItemMeta(buyMeta);
        buyButton = buyItem;

        ItemStack showBuy = new ItemStack(Material.ENDER_EYE); //Re
        ItemMeta showBuyMeta = showBuy.getItemMeta();
        showBuyMeta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + langConfig.getString("showBuy.title"));
        showBuyMeta.setLore(Arrays.asList(ChatColor.GREEN + langConfig.getString("showBuy.lore1"), ChatColor.GREEN + langConfig.getString("showBuy.lore2")));
        showBuy.setItemMeta(showBuyMeta);
        showBuyButton = showBuy;

        ItemStack delete = new ItemStack(Material.BARRIER); //Re
        ItemMeta deleteMeta = delete.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + langConfig.getString("delete.title"));
        deleteMeta.setLore(Arrays.asList(ChatColor.RED + langConfig.getString("delete.lore")));
        delete.setItemMeta(deleteMeta);
        deleteButton = delete;

        ItemStack noStock = new ItemStack(Material.RED_CONCRETE_POWDER); //Re
        ItemMeta noStockMeta = noStock.getItemMeta();
        noStockMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + langConfig.getString("noStock.title"));
        noStockMeta.setLore(Arrays.asList(ChatColor.RED + langConfig.getString("noStock.lore")));
        noStock.setItemMeta(noStockMeta);
        noStockButton = noStock;

        ItemStack stockConnect = new ItemStack(Material.HOPPER); //Re
        ItemMeta stockConnectMeta = stockConnect.getItemMeta();
        stockConnectMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + langConfig.getString("stockConnect.title"));
        stockConnectMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("stockConnect.lore1"), " ", ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + langConfig.getString("stockConnect.note"), ChatColor.GRAY + "" + ChatColor.ITALIC + langConfig.getString("stockConnect.lore2")));
        stockConnect.setItemMeta(stockConnectMeta);
        stockConnectShopButton= stockConnect;

        ItemStack pileConnect = new ItemStack(Material.HOPPER); //Re
        ItemMeta pileConnectMeta = pileConnect.getItemMeta();
        pileConnectMeta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + langConfig.getString("pileConnect.title"));
        pileConnectMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("pileConnect.lore1"), " ", ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + langConfig.getString("pileConnect.note"), ChatColor.GRAY + "" + ChatColor.ITALIC + langConfig.getString("pileConnect.lore2")));
        pileConnect.setItemMeta(pileConnectMeta);
        pileConnectButton = pileConnect;
        
        ItemStack stockSelf = new ItemStack(Material.CHEST); //Re
        ItemMeta stockSelfMeta = stockSelf.getItemMeta();
        stockSelfMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + langConfig.getString("stockSelf.title"));
        stockSelfMeta.setLore(Arrays.asList(ChatColor.YELLOW + langConfig.getString("stockSelf.lore"), " ", ChatColor.GOLD + "" + ChatColor.ITALIC + langConfig.getString("stockSelf.note"), ChatColor.YELLOW + "" + ChatColor.ITALIC + langConfig.getString("stockSelf.lore2")));
        stockSelf.setItemMeta(stockSelfMeta);
        stockSelfShopButton= stockSelf;

        ItemStack earningsCollect = new ItemStack(Material.DIAMOND); //Re
        ItemMeta earningsCollectMeta = earningsCollect.getItemMeta();
        earningsCollectMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + langConfig.getString("earningsCollect.title"));
        earningsCollectMeta.setLore(Arrays.asList(ChatColor.AQUA + langConfig.getString("earningsCollect.lore")));
        earningsCollect.setItemMeta(earningsCollectMeta);
        collectEarningsButton= earningsCollect;
        
        ItemStack earningsPileCollect = new ItemStack(Material.DIAMOND); //Re
        ItemMeta earningsPileCollectMeta = earningsPileCollect.getItemMeta();
        earningsPileCollectMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + langConfig.getString("earningsPileCollect.title"));
        earningsPileCollectMeta.setLore(Arrays.asList(ChatColor.AQUA + langConfig.getString("earningsPileCollect.lore")));
        earningsPileCollect.setItemMeta(earningsPileCollectMeta);
        collectEarningsPileButton= earningsPileCollect;

        ItemStack connectEarnings = new ItemStack(Material.ENDER_CHEST); //Re
        ItemMeta connectEarningsMeta = connectEarnings.getItemMeta();
        connectEarningsMeta.setDisplayName(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + langConfig.getString("connectEarnings.title"));
        connectEarningsMeta.setLore(Arrays.asList(ChatColor.LIGHT_PURPLE + langConfig.getString("connectEarnings.lore1"), ChatColor.LIGHT_PURPLE + langConfig.getString("connectEarnings.lore2"), " ", ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + langConfig.getString("connectEarnings.note"), ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + langConfig.getString("connectEarnings.lore3"), ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + langConfig.getString("connectEarnings.lore4")));
        connectEarnings.setItemMeta(connectEarningsMeta);
        connectEarningsButton = connectEarnings;
        
        ItemStack changeStyle = new ItemStack(Material.BRUSH); //Re
        ItemMeta changeStyleMeta = changeStyle.getItemMeta();
        changeStyleMeta.setDisplayName(Utils.colorize(langConfig.getString("changeStyle.title")));
        changeStyleMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + langConfig.getString("changeStyle.lore")));
        changeStyle.setItemMeta(changeStyleMeta);
        changeStyleShopButton= changeStyle;

        //

        ItemStack changeShopColor = new ItemStack(Material.BLUE_DYE); //Re
        ItemMeta changeShopColorMeta = changeShopColor.getItemMeta();
        changeShopColorMeta.setDisplayName(Utils.colorize(langConfig.getString("changeShopColor.title")));
        changeShopColorMeta.setLore(Arrays.asList(ChatColor.DARK_GRAY + langConfig.getString("changeShopColor.lore")));
        changeShopColor.setItemMeta(changeShopColorMeta);
        changeShopColorButton = changeShopColor;
        
        ItemStack changeChestBlock = new ItemStack(Material.CHEST); //Re
        ItemMeta changeChestBlockMeta = changeChestBlock.getItemMeta();
        changeChestBlockMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + langConfig.getString("changeChestBlock.title"));
        changeChestBlockMeta.setLore(Arrays.asList(ChatColor.YELLOW + langConfig.getString("changeChestBlock.lore")));
        changeChestBlock.setItemMeta(changeChestBlockMeta);
        changeChestBlockButton = changeChestBlock;

        ItemStack glassDisplay = new ItemStack(Material.GLASS); //Re
        ItemMeta glassDisplayMeta = glassDisplay.getItemMeta();
        glassDisplayMeta.setDisplayName(Utils.colorize(langConfig.getString("glassDisplay.title")));
        glassDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("glassDisplay.lore1"), ChatColor.GRAY + langConfig.getString("glassDisplay.lore2")));
        glassDisplay.setItemMeta(glassDisplayMeta);
        glassDisplayButton = glassDisplay;
        
        ItemStack glassDisplayBase = new ItemStack(Material.STRIPPED_OAK_WOOD); //Re
        ItemMeta glassDisplayBaseMeta = glassDisplayBase.getItemMeta();
        glassDisplayBaseMeta.setDisplayName(Utils.colorize(langConfig.getString("glassDisplayBase.title")));
        glassDisplayBaseMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("glassDisplayBase.lore1"), ChatColor.GRAY + langConfig.getString("glassDisplayBase.lore2")));
        glassDisplayBase.setItemMeta(glassDisplayBaseMeta);
        glassDisplayBaseButton = glassDisplayBase;

        ItemStack frameDisplay = new ItemStack(Material.PAINTING); //Re
        ItemMeta frameDisplayMeta = frameDisplay.getItemMeta();
        frameDisplayMeta.setDisplayName(Utils.colorize(langConfig.getString("frameDisplay.title")));
        frameDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("frameDisplay.lore1"), ChatColor.GRAY + langConfig.getString("frameDisplay.lore2")));
        frameDisplay.setItemMeta(frameDisplayMeta);
        frameDisplayButton = frameDisplay;
        
        ItemStack brightDisplay = new ItemStack(Material.GLOWSTONE_DUST); //Re
        ItemMeta brightDisplayMeta = brightDisplay.getItemMeta();
        brightDisplayMeta.setDisplayName(Utils.colorize(langConfig.getString("brightDisplay.title")));
        brightDisplayMeta.setLore(Arrays.asList(ChatColor.GRAY + langConfig.getString("brightDisplay.lore")));
        brightDisplay.setItemMeta(brightDisplayMeta);
        glowingDisplayButton = brightDisplay;
        
        ItemStack deleteDisplay = new ItemStack(Material.BARRIER); //Re
        ItemMeta deleteDisplayMeta = deleteDisplay.getItemMeta();
        deleteDisplayMeta.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + langConfig.getString("deleteDisplay.title"));
        deleteDisplayMeta.setLore(Arrays.asList(ChatColor.RED + langConfig.getString("deleteDisplay.lore")));
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