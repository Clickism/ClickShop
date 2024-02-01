package me.clickism.clickshop;

import me.clickism.clickshop.events.InteractEvent;
import org.bukkit.*;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ShopManager {
    private static LocalizationManager localizationManager = new LocalizationManager();

    public static void setPlugin(ClickShop plugin) {
        ShopManager.plugin = plugin;
    }

    private static ClickShop plugin;
    static DataManager data;
    private static HashMap<Player, List<Cod>> existingTethersList = new HashMap<>();
    private static HashMap<Player, Integer> selectingPile = new HashMap<>();
    public static void setupData(DataManager d) {
       data = d;
    }

    public static boolean createShop(ItemStack price, List<ItemStack> products, Location location, String owner) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return false;
        }
        List<Location> stockList = new ArrayList<>();
        List<Location> shopLocations;
        if (data.getConfig().get("shop_locations") == null) {
            shopLocations = new ArrayList<>();
        } else {
            shopLocations = (List<Location>) data.getConfig().get("shop_locations");
        }
        shopLocations.add(location);
        data.getConfig().set("shops." + location + ".shop.owner", owner);
        data.getConfig().set("shops." + location + ".shop.price", price);
        data.getConfig().set("shops." + location + ".shop.products", products);
        data.getConfig().set("shops." + location + ".shop.stock", stockList);
        data.getConfig().set("shops." + location + ".shop.earnings", 0);
        data.getConfig().set("shops." + location + ".shop.earnings_pile", null);
        data.getConfig().set("shops." + location + ".shop.color", "black");
        data.getConfig().set("shops." + location + ".shop.display", "default");
        data.getConfig().set("shops." + location + ".shop.entities", new ArrayList<>());
        data.getConfig().set("shop_locations", shopLocations);
        data.saveConfig();
        return true;
    }

    public static void editShopPrice(ItemStack price, List<ItemStack> products, Location location) {
        if (Bukkit.getPlayer(getOwner(location)) != null) {
            collectEarnings(Bukkit.getPlayer(getOwner(location)), location, false);
        }
        data.getConfig().set("shops." + location + ".shop.price", price);
        data.getConfig().set("shops." + location + ".shop.products", products);
        data.saveConfig();
    }

    public static String getDisplay(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (String) data.getConfig().get("shops." + location + ".shop.display");
        }
        return "";
    }

    public static void setDisplay(Location location, String display) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.display", display);
            data.saveConfig();
        }
    }

    public static List<String> getEntitiesList(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (List<String>) data.getConfig().get("shops." + location + ".shop.entities");
        }
        return new ArrayList<>();
    }

    public static void setEntitiesList(Location location, List<String> entities) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.entities", entities);
            data.saveConfig();
        }
    }

    public static boolean isShop(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return true;
        }
        return false;
    }

    public static String getColor(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop.color")) {
            return (String) data.getConfig().get("shops." + location + ".shop.color");
        }
        return null;
    }

    public static void setColor(Location location, String color) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.color", color);
            data.saveConfig();
        }
    }

    public static String getOwner(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (String) data.getConfig().get("shops." + location + ".shop.owner");
        }
        return "";
    }

    public static ItemStack getPrice(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (ItemStack) data.getConfig().get("shops." + location + ".shop.price");
        }
        return null;
    }

    public static List<ItemStack> getProducts(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (List<ItemStack>) data.getConfig().get("shops." + location + ".shop.products");
        }
        return new ArrayList<>();
    }

    public static int getEarnings(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (int) data.getConfig().get("shops." + location + ".shop.earnings");
        }
        return 0;
    }

    public static void setEarnings(Location location, int val) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.earnings", val);
            data.saveConfig();
        }
    }

    @Nullable
    public static List<Location> getStockPileList(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (List<Location>) data.getConfig().get("shops." + location + ".shop.stock");
        }
        return new ArrayList<>();
    }

    @Nullable
    public static List<Location> getShopLocations() {
        return (List<Location>) data.getConfig().get("shop_locations");
    }

    public static void setShopLocations(List<Location> list) {
        data.getConfig().set("shop_locations", list);
        data.saveConfig();
    }

    public static Location getEarningsPile(Location location) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            return (Location) data.getConfig().get("shops." + location + ".shop.earnings_pile");
        }
        return null;
    }

    public static void setEarningsPile(Location location, Location pile) {
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.earnings_pile", pile);
            data.saveConfig();
        }
    }

    public static void saveInventoryToConfig(Location location, Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack content : inventory.getContents()) {
            if (content != null) {
                items.add(content);
            }
        }
        if (data.getConfig().contains("shops." + location + ".shop")) {
            data.getConfig().set("shops." + location + ".shop.inventory", items);
            data.saveConfig();
        }
    }

    public static Inventory getInventoryFromConfig(Location location) {
        Inventory inv = Bukkit.createInventory(null, 27, ShopMenu.getSelfStockTitle());
        if (data.getConfig().get("shops." + location + ".shop.inventory") != null) {
            ((List<ItemStack>) data.getConfig().get("shops." + location + ".shop.inventory")).forEach(item -> {
                if (item != null) {
                    inv.addItem(item.clone());
                }
            });
        }
        return inv;
    }

    public static List<Location> getOutOfStockLocations(String player) {
        if (data.getConfig().get("to_be_warned." + player) != null) {
            return (List<Location>) data.getConfig().get("to_be_warned." + player);
        }
        return new ArrayList<>();
    }

    public static void setOutOfStockLocations(String player, List<Location> outOfStockLocations) {
        data.getConfig().set("to_be_warned." + player, outOfStockLocations);
    }

    public static boolean isShopActive(Location location) {
        return hasStock(location);
    }

    public static void deleteShop(Location location) {
        ShopManager.getEntitiesList(location).forEach(uuidString -> {
            UUID uuid = UUID.fromString(uuidString);
            if (Bukkit.getEntity(uuid) != null) {
                Bukkit.getEntity(uuid).remove();
            }
        });
        if (getOutOfStockLocations(getOwner(location)).contains(location)) {
            List<Location> list = getOutOfStockLocations(getOwner(location));
            list.remove(location);
            setOutOfStockLocations(getOwner(location), list);
        }
        if (Bukkit.getPlayer(getOwner(location)) != null) {
            collectEarnings(Bukkit.getPlayer(getOwner(location)), location, false);
        }
        for (ItemStack content : getInventoryFromConfig(location).getContents()) {
            if (content != null) {
                location.getWorld().dropItem(location, content);
            }
        }

        List<Location> list = getShopLocations();
        list.remove(location);
        setShopLocations(list);
        data.getConfig().set("shops." + location, null);
        data.saveConfig();
    }

    public static void selectStockPile(Player player, Location location) {
        List<Cod> codList = new ArrayList<>();
        if (!getStockPileList(location).isEmpty()) {
            getStockPileList(location).forEach(stock -> {
                codList.addAll(createTether(location, stock));
            });
        }
        codList.addAll(createTether(location, player));
        existingTethersList.put(player, codList);
        selectingPile.put(player, 1);
        checkTethers();
        player.sendTitle("",  ChatColor.GOLD + "<< " + ChatColor.YELLOW + localizationManager.getMessage("click.chest.barrel") +  ChatColor.GOLD + " >>", 1, 9999999, 1);
    }
    
    public static void selectEarningsPile(Player player, Location location) {
        existingTethersList.put(player, createTether(location, player));
        checkTethers();
        selectingPile.put(player, 2);
        player.sendTitle("",  ChatColor.GOLD + "<< " + ChatColor.YELLOW + localizationManager.getMessage("click.ender.chest") +  ChatColor.GOLD + " >>", 1, 9999999, 1);
    }

    public static Location getAnchorTetherLocationFromPlayer(Player p) {
        AtomicReference<Location> loc = new AtomicReference<>(null);
        existingTethersList.get(p).forEach(cod -> {
            if (cod.isLeashed()) {
                if (cod.getLeashHolder().equals(p)) {
                    loc.set(cod.getLocation());
                }
            }
        });
        return loc.get();
    }

    public static List<Location> getConnectedShopsToEarningPile(Location location) {
        List<Location> shopLocations = getShopLocations();
        List<Location> connectedShops = new ArrayList<>();
        if (shopLocations != null) {
            shopLocations.forEach(shop -> {
                if (getEarningsPile(shop) != null) {
                    if (getEarningsPile(shop).equals(location)) {
                        connectedShops.add(shop);
                    }
                }
            });
        }
        return connectedShops;
    }

    public static void selectShopFromEarningsPile(Player player, Location location) {
        List<Cod> codList = new ArrayList<>();
        List<Location> shopLocations = getShopLocations();
        shopLocations.forEach(shop -> {
            if (getEarningsPile(shop) != null) {
                if (getEarningsPile(shop).equals(location)) {
                    codList.addAll(createTether(location, shop));
                }
            }
        });
        codList.addAll(createTether(location, player));
        existingTethersList.put(player, codList);
        selectingPile.put(player, 3);
        checkTethers();
        player.sendTitle("",  ChatColor.GOLD + "<< " + ChatColor.YELLOW + localizationManager.getMessage("right.click.shop") +  ChatColor.GOLD + " >>", 1, 9999999, 1);
    }

    public static int getSelectingPile(Player p) {
        return selectingPile.get(p);
    }

    public static int removeSelectingPile(Player p) {
        return selectingPile.remove(p);
    }

    public static Location getShopLocationFromStockPile(Location stock) {
        List<Location> shopLocations = getShopLocations();
        if (shopLocations != null) {
            AtomicReference<Location> shop = new AtomicReference<>(null);
            shopLocations.forEach(loc -> {
                if (getStockPileList(loc) != null) {
                    if (getStockPileList(loc).contains(stock)) {
                        shop.set(loc);
                    }
                }
            });
            return shop.get();
        }
        return null;
    }

    private static boolean checkingTethers = false;
    public static void checkTethers() {
        if (!checkingTethers) {
            checkingTethers = true;
            List<Player> cancelList = new ArrayList<>();
            Bukkit.getScheduler().runTaskTimer(plugin, (task) -> {
                if (!existingTethersList.isEmpty()) {
                    existingTethersList.forEach((player, list) -> {
                        Player p = player;
                        list.forEach(cod -> {
                            if (p.getWorld().equals(cod.getWorld())) {
                                try {
                                    if (cod.getLeashHolder().equals(p) && p.getLocation().distance(cod.getLocation()) > 9d) {
                                        cancelList.add(p);
                                        p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1f,1f);
                                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("connecting.wirelessly"));
                                        Utils.playConfirmSound(p);
                                    }
                                } catch (IllegalStateException ignored) { }
                            }
                        });
                    });
                } else {
                    task.cancel();
                    checkingTethers = false;
                }
                cancelList.forEach(player -> {
                    clearTethers(player);
                });
                cancelList.clear();
            }, 2L, 2L);
        }
    }

    public static boolean isPlayerConnectingTethers(Player p) {
        return selectingPile.containsKey(p);
    }

    public static void clearTethers(Player p) {
        if (existingTethersList.get(p) != null) {
            if (!existingTethersList.get(p).isEmpty()) {
                existingTethersList.get(p).forEach(cod -> {
                    cod.setLeashHolder(null);
                    cod.teleport(new Location(p.getWorld(), p.getLocation().getX(), -100d, p.getLocation().getZ()));
                    cod.setHealth(0d);
                });
//                existingTethersList.remove(p);
            }
        }
        p.resetTitle();
    }

    public static void addStockPile(Player player, Location stock) {
        List<Location> stockList = (List<Location>) data.getConfig().get("shops." + InteractEvent.getLastClickedChestPosition(player) + ".shop.stock");
        Location loc = stock;
        if (((InventoryHolder) stock.getBlock().getState()).getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) ((Chest) stock.getBlock().getState()).getInventory().getHolder();
            loc = doubleChest.getInventory().getLocation();
        }
        if (!(stockList.contains(loc))) {
            if (InteractEvent.getLastClickedChestPosition(player).distance(loc) < 10d) {
                stockList.add(loc);
                data.getConfig().set("shops." + InteractEvent.getLastClickedChestPosition(player) + ".shop.stock", stockList);
                data.saveConfig();
                clearTethers(player);
                Utils.playConfirmSound(player);
                player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("stockpile.connection.added"));
            } else {
                stockList.add(loc);
                data.getConfig().set("shops." + InteractEvent.getLastClickedChestPosition(player) + ".shop.stock", stockList);
                data.saveConfig();
                clearTethers(player);
                Utils.playConfirmSound(player);
                player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("wireless.stockpile.connection.added"));
            }
        } else {
            removeStockPile(player, stock);
        }
    }

    public static void addEarningsPile(Player player, Location pile) {
        if (InteractEvent.getLastClickedChestPosition(player).distance(pile) < 10d) {
            setEarningsPile(InteractEvent.getLastClickedChestPosition(player), pile);
            clearTethers(player);
            Utils.playConfirmSound(player);
            player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("earningspile.connection.added"));
        } else {
            Utils.playFailSound(player);
            player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("earningspile.distance.error"));
        }
    }

    public static void addEarningsPileToShop(Player player, Location shop) {
        if (InteractEvent.getLastClickedChestPosition(player).distance(shop) < 10d) {
            if (shop.equals(getShopLocationFromStockPile(InteractEvent.getLastClickedChestPosition(player)))) {
                setEarningsPile(shop, null);
                Utils.playFailSound(player);
                player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("earningspile.connection.removed"));
            } else {
                setEarningsPile(shop, InteractEvent.getLastClickedChestPosition(player));
                Utils.playConfirmSound(player);
                player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("earningspile.connection.added"));
            }
            clearTethers(player);
        } else {
            Utils.playFailSound(player);
            player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("earningspile.distance.error"));
        }
    }

    public static void removeStockPile(Player player, Location stock) {
        List<Location> stockList = (List<Location>) data.getConfig().get("shops." + InteractEvent.getLastClickedChestPosition(player) + ".shop.stock");
        Location loc = stock;
        if (((InventoryHolder) stock.getBlock().getState()).getInventory().getHolder() instanceof DoubleChest) {
            DoubleChest doubleChest = (DoubleChest) ((Chest) stock.getBlock().getState()).getInventory().getHolder();
            loc = doubleChest.getInventory().getLocation();
        }
        if (stockList.contains(loc)) {
            stockList.remove(loc);
            data.getConfig().set("shops." + InteractEvent.getLastClickedChestPosition(player) + ".shop.stock", stockList);
            data.saveConfig();
            clearTethers(player);
            player.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("stockpile.connection.removed"));
            Utils.playFailSound(player);
        }
    }

    public static List<Cod> createTether(Location from, Location to) {
        if (from.distance(to) < 10d) {
            Location loc = new Location(from.getWorld(), from.getX() + .5, from.getY() + .6d, from.getZ() + .5);
            Location loc2 = new Location(to.getWorld(), to.getX() + .5, to.getY() + .6d, to.getZ() + .5);
            Cod a = (Cod) loc.getWorld().spawnEntity(loc, EntityType.COD);
            Cod b = (Cod) loc2.getWorld().spawnEntity(loc2, EntityType.COD);
            a.setAI(false);
            a.setInvisible(true);
            a.setSilent(true);
            a.setGravity(false);
            a.setInvulnerable(true);
            a.setCollidable(false);
            b.setAI(false);
            b.setInvisible(true);
            b.setSilent(true);
            b.setGravity(false);
            b.setInvulnerable(true);
            b.setCollidable(false);
            a.setLeashHolder(b);
            List<Cod> tethers = new ArrayList<>();
            tethers.add(a);
            tethers.add(b);
            return tethers;
        } else {
            return new ArrayList<>();
        }
    }

    public static List<Cod> createTether(Location from, Player holder) {
        Location loc = new Location(from.getWorld(), from.getX() + .5, from.getY() + .6d, from.getZ() + .5);
        Cod x = (Cod) loc.getWorld().spawnEntity(loc, EntityType.COD);
        x.setAI(false);
        x.setInvisible(true);
        x.setInvulnerable(true);
        x.setSilent(true);
        x.setLeashHolder(holder);
        x.setGravity(false);
        x.setCollidable(false);
        return Arrays.asList(x);
    }

    public static void buyFromShop(Player buyer, Location location) {
        ItemStack price = getPrice(location);
        List<ItemStack> products = getProducts(location);
        //Check if player has money
        if (buyer.getInventory().containsAtLeast(price, price.getAmount())) {
            //Buy
            buyer.getInventory().removeItem(price);
            AtomicBoolean singleType = new AtomicBoolean(true);
            AtomicInteger amount = new AtomicInteger();
            ItemStack previouslyChecked = products.get(0);
            products.forEach(product -> {
                if (previouslyChecked.getType() != product.getType()) {
                    singleType.set(false);
                }
                Utils.addItem(buyer, product.clone());
                amount.addAndGet(product.getAmount());
            });

            String owner = getOwner(location);
            String priceName;
            if (price.getItemMeta().hasDisplayName()) {
                priceName = price.getAmount() + " " + Utils.capitalize(ChatColor.stripColor(price.getItemMeta().getDisplayName()));
            } else {
                priceName = price.getAmount() + " " + Utils.capitalize(price.getType().toString());
            }
            String productName;
            if (previouslyChecked.getItemMeta().hasDisplayName()) {
                productName = amount.get() + " " + Utils.capitalize(ChatColor.stripColor(previouslyChecked.getItemMeta().getDisplayName()));
            } else {
                productName = amount.get() + " " + Utils.capitalize(previouslyChecked.getType().toString());
            }

            if (singleType.get()) {
                buyer.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("you.bought") + ChatColor.WHITE + productName + ChatColor.GREEN + localizationManager.getMessage("from") + ChatColor.BOLD + owner +  localizationManager.getMessage("for") + ChatColor.WHITE + priceName + ".");
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "" + ChatColor.BOLD + buyer.getName() + ChatColor.RESET + "" + ChatColor.GREEN + localizationManager.getMessage("bought") + ChatColor.WHITE + productName + ChatColor.GREEN + localizationManager.getMessage("from.you.for") + ChatColor.WHITE + priceName + ".");
                }
            } else {
                buyer.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + localizationManager.getMessage("you.bought.various") + ChatColor.BOLD + owner + ChatColor.GREEN + localizationManager.getMessage("for") + ChatColor.WHITE + priceName + ".");
                if (Bukkit.getPlayer(owner) != null) {
                    Bukkit.getPlayer(owner).sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "" + ChatColor.BOLD + buyer.getName() + ChatColor.RESET + "" + ChatColor.GREEN + localizationManager.getMessage("bought.various.from.you.for") + ChatColor.WHITE + priceName + ".");
                }
            }

            //Add earning
            addEarning(location);
            //Reduce stock
            reduceStock(location);

            if (!hasStock(location)) {
                List<Location> list = new ArrayList<>();
                if (getOutOfStockLocations(owner) != null) {
                    list = getOutOfStockLocations(owner);
                }
                if (!list.contains(location)) {
                    list.add(location);
                }
                setOutOfStockLocations(owner, list);
                if (Bukkit.getPlayer(owner) != null) {
                    if (warnedPlayerLocations.containsKey(Bukkit.getPlayer(owner))) {
                        if (!warnedPlayerLocations.get(Bukkit.getPlayer(owner)).contains(location)) {
                            warnedPlayerLocations.get(Bukkit.getPlayer(owner)).add(location);
                            String locationString = (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ();
                            Bukkit.getPlayer(owner).sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + String.format(localizationManager.getMessage("shop.out.of.stock"), locationString));
                            Utils.playFailSound(Bukkit.getPlayer(owner));
                        }
                    } else {
                        List<Location> locationList = new ArrayList<>();
                        locationList.add(location);
                        warnedPlayerLocations.put(Bukkit.getPlayer(owner), locationList);
                        String locationString = (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ();
                        Bukkit.getPlayer(owner).sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + String.format(localizationManager.getMessage("shop.out.of.stock"), locationString));
                        Utils.playFailSound(Bukkit.getPlayer(owner));
                    }
                }
            } else {
                List<Location> list = new ArrayList<>();
                if (getOutOfStockLocations(owner) != null) {
                    list = getOutOfStockLocations(owner);
                }
                list.remove(location);
                setOutOfStockLocations(owner, list);
            }
        } else {
            buyer.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("not.enough.money"));
        }
    }

    public static void reduceStock(Location location) {
        boolean self = false;
        Inventory stockInv = getAvailableStockInventory(location);
        List<ItemStack> products = getProducts(location);
        if (stockInv.getLocation() == null) {
            self = true;
        }
        products.forEach(product -> {
            stockInv.removeItem(product);
        });
        if (self) {
            saveInventoryToConfig(location, stockInv);
        }
    }

    public static void addEarning(Location location) {
        int earnings = getEarnings(location);
        int amount = getPrice(location).getAmount();
        setEarnings(location, earnings + amount);
    }

    public static void collectEarningsPile(Player p, Location pile) {
        List<Location> shops = getConnectedShopsToEarningPile(pile);
        AtomicInteger total_earnings = new AtomicInteger();
        shops.forEach(shop -> {
            total_earnings.addAndGet(collectEarnings(p, shop, false));
        });
        if (total_earnings.get() == 0) {
            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("no.earnings.yet"));
        }
    }

    public static HashMap<Player, List<Location>> warnedPlayerLocations = new HashMap<>();

    public static boolean hasStock(Location location) {
        return getAvailableStockInventory(location) != null;
    }

    public static Inventory getAvailableStockInventory(Location location) {
        List<ItemStack> products = getProducts(location);
        List<Inventory> inventories = new ArrayList<>(Arrays.asList(getInventoryFromConfig(location)));
        inventories.addAll(getStockInventoriesList(location));
        AtomicReference<Inventory> selectedInv = new AtomicReference<>(null);
        inventories.forEach(inv -> {
            AtomicInteger foundSlots = new AtomicInteger();
            if (products != null) {
                products.forEach(product -> {
                    if (inv.containsAtLeast(product, product.getAmount())) {
                        foundSlots.getAndIncrement();
                    }
                });
                if (foundSlots.get() == products.size() && selectedInv.get() == null) {
                    selectedInv.set(inv);
                }
            }
        });
        return selectedInv.get();
    }

    public static List<Inventory> getStockInventoriesList(Location location) {
        List<Location> stockList = getStockPileList(location);
        List<Inventory> inventories = new ArrayList<>();
        if (stockList != null) {
            stockList.forEach(stock -> {
                if (stock.getBlock().getState() instanceof Chest || stock.getBlock().getState() instanceof Barrel) {
                    if (stock != location) {
                        inventories.add(((InventoryHolder) stock.getBlock().getState()).getInventory());
                    }
                }
            });
        }
        return inventories;
    }

    public static int collectEarnings(Player p, Location location, boolean message) {
        int earnings = ShopManager.getEarnings(location);
        int earningsOld = ShopManager.getEarnings(location);
        if (earnings > 0) {
            ItemStack price = ShopManager.getPrice(location).clone();
            ShopManager.setEarnings(location, 0);
            price.setAmount(earnings);
            Utils.addItem(p, price);
            p.closeInventory();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        } else {
            p.closeInventory();
            if (message) {
                p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + localizationManager.getMessage("no.earnings.yet"));
            }
            Utils.playFailSound(p);
        }
        return earningsOld;
    }
}
