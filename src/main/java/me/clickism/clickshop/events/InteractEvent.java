package me.clickism.clickshop.events;

import me.clickism.clickshop.ClickShop;
import me.clickism.clickshop.ShopManager;
import me.clickism.clickshop.ShopMenu;
import me.clickism.clickshop.Utils;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class InteractEvent implements Listener {
    public static HashMap<Player, Location> lastClickedChestLocations = new HashMap<>();

    public static void setPlugin(ClickShop pl) {
        plugin = pl;
    }

    private static ClickShop plugin;

    public static Location getLastClickedChestPosition(Player p) {
        return lastClickedChestLocations.get(p);
    }

    public static void setLastClickedChestLocations(Player p, Location loc) {
        lastClickedChestLocations.put(p, loc);
    }

    @EventHandler
    public void onInteract (InventoryClickEvent e) {
        Player p = (Player) e.getView().getPlayer();
        int i = e.getSlot();
        if (e.getClickedInventory() == null || e.getClickedInventory().equals(p.getInventory())) {
            return;
        }
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getView().getTitle().equals(ShopMenu.getCreateShopMenuTitle())) {
            e.setCancelled(true);
            if (ShopMenu.getCreateShopPriceArea().contains(i)) {
                if (e.getCursor().getType() != Material.AIR) {
                    e.setCurrentItem(e.getCursor());
                } else {
                    e.setCurrentItem(ShopMenu.getPriceButton());
                }
            } else if (ShopMenu.getCreateShopProductArea().contains(i)) {
                if (e.getCursor().getType() != Material.AIR) {
                    e.setCurrentItem(e.getCursor());
                } else {
                    e.setCurrentItem(ShopMenu.getProductButton());
                }
            } else if (e.getCurrentItem().equals(ShopMenu.getCreateShopButton())) {
                ItemStack price = e.getInventory().getItem(ShopMenu.getCreateShopPriceArea().get(0));
                List<ItemStack> products = new ArrayList<>();
                ShopMenu.getCreateShopProductArea().forEach(integer -> {
                    if (e.getInventory().getItem(integer) != null) {
                        if (!e.getInventory().getItem(integer).equals(ShopMenu.getProductButton())) {
                            products.add(e.getInventory().getItem(integer));
                        }
                    }
                });
                if (products.isEmpty() || price.equals(ShopMenu.getPriceButton())) {
                    e.getView().getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't leave the price and product areas empty.");
                    Utils.playFailSound(p);
                    p.closeInventory();
                } else {
                    //Create shop
                    if (!ShopManager.isShop(getLastClickedChestPosition(p))) {
                        ShopManager.createShop(price, products, getLastClickedChestPosition(p), p.getName());
                        p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY, .3f, 1f);
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Shop succesfully created.");
//                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GOLD + "" + ChatColor.ITALIC + "Not: " + ChatColor.YELLOW + "" + ChatColor.ITALIC + "Dükkanı kaldırmak için bloğu kır.");
                        p.closeInventory();
                    } else {
                        e.getView().getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "There's already a shop here.");
                        Utils.playFailSound(p);
                        p.closeInventory();
                    }
                }
            }
        }
        if (e.getView().getTitle().equals(ShopMenu.getEditShopMenuTitle(p))) {
            e.setCancelled(true);
            if (e.getCurrentItem().equals(ShopMenu.getStockConnectShopButton())) {
                p.closeInventory();
                ShopManager.selectStockPile(p, getLastClickedChestPosition(p));
            } else if (e.getCurrentItem().equals(ShopMenu.getStockSelfShopButton())) {
                //Self Stock
                p.openInventory(ShopManager.getInventoryFromConfig(getLastClickedChestPosition(p)));
                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, .3f,1f);
            } else if (e.getCurrentItem().equals(ShopMenu.getShowBuyButton())) {
                //Show Products Screen
                p.openInventory(ShopMenu.getEditPriceMenu(getLastClickedChestPosition(p)));
            } else if (e.getCurrentItem().equals(ShopMenu.getChangeStyleShopButton())) {
                //Change Style
                Inventory inv = ShopMenu.getStyleMenu(getLastClickedChestPosition(p));
                ItemStack item = ShopMenu.getChangeChestBlockButton().clone();
                item.setType(getLastClickedChestPosition(p).getBlock().getType());
                inv.setItem(28,item);
                e.getView().getPlayer().openInventory(inv);
            } else if (e.getCurrentItem().equals(ShopMenu.getCollectEarningsButton())) {
                //Collect Earnings
                ShopManager.collectEarnings(p, getLastClickedChestPosition(p), true);
            } else if (e.getCurrentItem().equals(ShopMenu.getConnectEarningsButton())) {
                //Connect Earnings Pile
                p.closeInventory();
                if (ShopManager.getEarningsPile(getLastClickedChestPosition(p)) != null) {
                    ShopManager.setEarningsPile(getLastClickedChestPosition(p), null);
                    p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Previous earnings pile removed.");
                    Utils.playFailSound(p);
                }
                ShopManager.selectEarningsPile(p, getLastClickedChestPosition(p));
            }
        }
        if (ShopMenu.getBuyMenuTitle(getLastClickedChestPosition(p)) != null) {
            if (e.getView().getTitle().equals(ShopMenu.getBuyMenuTitle(getLastClickedChestPosition(p)))) {
                e.setCancelled(true);
                if (e.getCurrentItem().equals(ShopMenu.getBuyButton())) {
                    //Buy
                    ShopManager.buyFromShop(p, getLastClickedChestPosition(p));
                    Utils.playConfirmSound(p);
                    if (!ShopManager.hasStock(getLastClickedChestPosition(p))) {
                        e.getInventory().setItem(21, ShopMenu.getNoStockButton());
                    }
                }
            }
        }
        if (e.getView().getTitle().equals(ShopMenu.getEditPriceMenuTitle())) {
            e.setCancelled(true);
            if (ShopMenu.getCreateShopPriceArea().contains(i)) {
                if (e.getCursor().getType() != Material.AIR) {
                    e.setCurrentItem(e.getCursor());
                } else {
                    e.setCurrentItem(ShopMenu.getPriceButton());
                }
            } else if (ShopMenu.getCreateShopProductArea().contains(i)) {
                if (e.getCursor().getType() != Material.AIR) {
                    e.setCurrentItem(e.getCursor());
                } else {
                    e.setCurrentItem(ShopMenu.getProductButton());
                }
            } else if (e.getCurrentItem().equals(ShopMenu.getEditConfirmButton())) {
                ItemStack price = e.getInventory().getItem(ShopMenu.getCreateShopPriceArea().get(0));
                List<ItemStack> products = new ArrayList<>();
                ShopMenu.getCreateShopProductArea().forEach(integer -> {
                    if (e.getInventory().getItem(integer) != null) {
                        if (!e.getInventory().getItem(integer).equals(ShopMenu.getProductButton())) {
                            products.add(e.getInventory().getItem(integer));
                        }
                    }
                });
                if (products.isEmpty() || price.equals(ShopMenu.getPriceButton())) {
                    e.getView().getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please don't leave the price and product are empty.");
                    Utils.playFailSound(p);
                    e.setCancelled(true);
                    p.closeInventory();
                } else {
                    //Edit price
                    e.setCancelled(true);
                    ShopManager.editShopPrice(price, products, getLastClickedChestPosition(p));
                    p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY, .3f, 1f);
                    p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Saved changes.");
                    p.closeInventory();
                }
            } else if (e.getCurrentItem().equals(ShopMenu.getDeleteButton())) {
                e.setCancelled(true);
                ShopManager.deleteShop(getLastClickedChestPosition(p));
                p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Shop removed.");
                p.closeInventory();
            } else {
                e.setCancelled(true);
            }
        }
        if (e.getView().getTitle().equals(ShopMenu.getCollectMenuTitle())) {
            //Enderchest
            e.setCancelled(true);
            if (e.getCurrentItem().equals(ShopMenu.getPileConnectButton())) {
                p.closeInventory();
                ShopManager.selectShopFromEarningsPile(p, getLastClickedChestPosition(p));
            } else if (e.getCurrentItem().equals(ShopMenu.getCollectEarningsPileButton())) {
                ShopManager.collectEarningsPile(p, getLastClickedChestPosition(p));
                Utils.playConfirmSound(p);
            }
        }
        if (ShopMenu.getStyleMenuTitle(getLastClickedChestPosition(p)) != null) {
            if (e.getView().getTitle().equals(ShopMenu.getStyleMenuTitle(getLastClickedChestPosition(p)))) {
                e.setCancelled(true);
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ShopMenu.getChangeShopColorButton().getItemMeta().getDisplayName())) {
                    e.setCancelled(true);
                    //Change color
                    Material mat = e.getCurrentItem().getType();
                    if (mat == Material.BLACK_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "black");
                    } else if (mat == Material.BLUE_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "blue");
                    } else if (mat == Material.PURPLE_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "pink");
                    } else if (mat == Material.RED_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "red");
                    } else if (mat == Material.LIME_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "green");
                    } else if (mat == Material.YELLOW_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "yellow");
                    } else if (mat == Material.CYAN_DYE) {
                        ShopManager.setColor(getLastClickedChestPosition(p), "rainbow");
                    }
                    p.openInventory(ShopMenu.getStyleMenu(getLastClickedChestPosition(p)));
                    Utils.playConfirmSound(p);
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ShopMenu.getChangeChestBlockButton().getItemMeta().getDisplayName()) && !e.getCursor().getType().equals(Material.AIR)) {
                    //Change Block
                    if (e.getCursor().getType().isBlock() && e.getCursor().getType().isSolid() && !Tag.LEAVES.getValues().contains(e.getCursor().getType())) {
                        checkGlassDisplay(getLastClickedChestPosition(p), e.getCurrentItem().getType(), e.getCursor().getType());
                        Block block = getLastClickedChestPosition(p).getBlock();
                        block.setType(e.getCursor().getType());
                        if (block.getBlockData() instanceof Directional) {
                            Directional blockData = (Directional) block.getBlockData();
                            blockData.setFacing(e.getView().getPlayer().getFacing().getOppositeFace());
                            block.setBlockData(blockData);
                        }

                        //Add or remove item from hand
                        ItemStack item = e.getCursor();
                        item.setAmount(item.getAmount() - 1);
                        if (item.getAmount() <= 0) {
                            e.setCursor(new ItemStack(Material.AIR));
                        } else {
                            e.setCursor(item);
                        }

                        Utils.addItem(p, new ItemStack(e.getCurrentItem().getType()));
                        p.openInventory(ShopMenu.getStyleMenu(getLastClickedChestPosition(p)));

                        Utils.playConfirmSound(p);
                    } else {
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please put a valid block.");
                        Utils.playFailSound(p);
                    }
                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ShopMenu.getGlassDisplayButton().getItemMeta().getDisplayName())) {
                    //Change Glass
                    List<String> displayEntitiesList = new ArrayList<>();
                    Location loc = getLastClickedChestPosition(p).clone().add(.5,1,.5);
                    Location shop = getLastClickedChestPosition(p).clone();
                    Material oldGlassDisplayMaterial = Material.GLASS;
                    Material oldBaseDisplayMaterial = Material.STRIPPED_OAK_WOOD;

                    if (!ShopManager.getEntitiesList(shop).isEmpty()) {
                        if ((Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(1)))) != null && (ShopManager.getDisplay(shop).equals("glass") || ShopManager.getDisplay(shop).equals("glass_glowing"))) {
                            oldGlassDisplayMaterial = ((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(0)))).getBlock().getMaterial();
                            oldBaseDisplayMaterial = ((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(1)))).getBlock().getMaterial();
                        }
                    }

                    ShopManager.getEntitiesList(shop).forEach(uuidString -> {
                        UUID uuid = UUID.fromString(uuidString);
                        if (Bukkit.getEntity(uuid) != null) {
                            Bukkit.getEntity(uuid).remove();
                        }
                    });

                    BlockDisplay glassDisplay = (BlockDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                    if (shop.getBlock().getType() == Material.CHEST) {
                        glassDisplay.setTransformation(new Transformation(new Vector3f(-.3f, -0.05f, -.3f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.6f, 0.6f, 0.6f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        glassDisplay.setTransformation(new Transformation(new Vector3f(-.3f, .075f, -.3f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.6f, 0.6f, 0.6f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    }
                    glassDisplay.setViewRange(.2f);
                    glassDisplay.setShadowRadius(0f);
                    displayEntitiesList.add(glassDisplay.getUniqueId().toString());

                    if (e.getCursor() != null && glassList.contains(e.getCursor().getType())) {
                        glassDisplay.setBlock(e.getCursor().getType().createBlockData());
//                        e.getCurrentItem().setType(e.getCursor().getType());
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Glass color changed.");
                        Utils.playConfirmSound(p);
                    } else {
                        glassDisplay.setBlock(oldGlassDisplayMaterial.createBlockData());
                        if (e.getCursor().getType() == Material.AIR) {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Glass display created.");
                            Utils.playConfirmSound(p);
                        } else {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please put a valid colored glass block.");
                            Utils.playFailSound(p);
                        }
                    }

                    BlockDisplay baseDisplay = (BlockDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                    if (shop.getBlock().getType() == Material.CHEST) {
                        baseDisplay.setTransformation(new Transformation(new Vector3f(-.35f, -.15f, -.35f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.7f, 0.1f, 0.7f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        baseDisplay.setTransformation(new Transformation(new Vector3f(-.4f,-.025f,-.4f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(.8f,.1f,.8f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    }
                    baseDisplay.setBlock(oldBaseDisplayMaterial.createBlockData());
                    baseDisplay.setViewRange(.2f);
                    baseDisplay.setShadowRadius(0f);
                    displayEntitiesList.add(baseDisplay.getUniqueId().toString());

                    ItemDisplay itemDisplay = (ItemDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);
                    Vector3f scale;
                    if (ShopManager.getProducts(getLastClickedChestPosition(p)).get(0).getType() == Material.WITHER_SKELETON_SKULL) {
                        scale = new Vector3f(.5f,.5f,.5f);
                    } else if (ShopManager.getProducts(getLastClickedChestPosition(p)).get(0).getType().isBlock()) {
                        scale = new Vector3f(.3f,.3f,.3f);
                    } else {
                        scale = new Vector3f(.4f,.4f,.4f);
                    }
                    if (shop.getBlock().getType() == Material.CHEST) {
                        itemDisplay.setTransformation(new Transformation(new Vector3f(0f, .25f, 0f), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        itemDisplay.setTransformation(new Transformation(new Vector3f(0f, .375f, 0f), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                    }
                    itemDisplay.setItemStack(ShopManager.getProducts(shop).get(0));
                    itemDisplay.setViewRange(.2f);
                    itemDisplay.setShadowRadius(0f);
                    itemDisplay.setBillboard(Display.Billboard.VERTICAL);
                    displayEntitiesList.add(itemDisplay.getUniqueId().toString());
                    ShopManager.setEntitiesList(shop, displayEntitiesList);
                    ShopManager.setDisplay(shop, "glass");

                    p.openInventory(ShopMenu.getStyleMenu(shop));

                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ShopMenu.getGlassDisplayBaseButton().getItemMeta().getDisplayName())) {
                    //Change Base
                    List<String> displayEntitiesList = new ArrayList<>();
                    Location loc = getLastClickedChestPosition(p).clone().add(.5,1,.5);
                    Location shop = getLastClickedChestPosition(p).clone();
                    Material oldBaseDisplayMaterial = Material.STRIPPED_OAK_WOOD;
                    Material oldGlassDisplayMaterial = Material.GLASS;

                    if (!ShopManager.getEntitiesList(shop).isEmpty()) {
                        if ((Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(1)))) != null && (ShopManager.getDisplay(shop).equals("glass") || ShopManager.getDisplay(shop).equals("glass_glowing"))) {
                            oldBaseDisplayMaterial = ((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(1)))).getBlock().getMaterial();
                            oldGlassDisplayMaterial = ((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(0)))).getBlock().getMaterial();
                        }
                    }

                    ShopManager.getEntitiesList(shop).forEach(uuidString -> {
                        UUID uuid = UUID.fromString(uuidString);
                        if (Bukkit.getEntity(uuid) != null) {
                            Bukkit.getEntity(uuid).remove();
                        }
                    });

                    BlockDisplay glassDisplay = (BlockDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                    if (shop.getBlock().getType() == Material.CHEST) {
                        glassDisplay.setTransformation(new Transformation(new Vector3f(-.3f, -0.05f, -.3f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.6f, 0.6f, 0.6f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        glassDisplay.setTransformation(new Transformation(new Vector3f(-.3f, .075f, -.3f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.6f, 0.6f, 0.6f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    }
                    glassDisplay.setViewRange(.2f);
                    glassDisplay.setShadowRadius(0f);
                    glassDisplay.setBlock(oldGlassDisplayMaterial.createBlockData());
                    displayEntitiesList.add(glassDisplay.getUniqueId().toString());

                    BlockDisplay baseDisplay = (BlockDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                    if (shop.getBlock().getType() == Material.CHEST) {
                        baseDisplay.setTransformation(new Transformation(new Vector3f(-.35f, -.15f, -.35f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.7f, 0.1f, 0.7f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        baseDisplay.setTransformation(new Transformation(new Vector3f(-.4f,-.025f,-.4f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(.8f,.1f,.8f), new AxisAngle4f(0f, 0f, 0f, 0f)));
                    }
                    baseDisplay.setViewRange(.2f);
                    baseDisplay.setShadowRadius(0f);
                    displayEntitiesList.add(baseDisplay.getUniqueId().toString());

                    if (e.getCursor() != null && e.getCursor().getType().isBlock() && e.getCursor().getType().isSolid()) {
                        baseDisplay.setBlock(e.getCursor().getType().createBlockData());
//                        e.getCurrentItem().setType(e.getCursor().getType());
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Glass display base changed.");
                        Utils.playConfirmSound(p);
                    } else {
                        baseDisplay.setBlock(oldBaseDisplayMaterial.createBlockData());
                        if (e.getCursor().getType() == Material.AIR) {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Glass display created.");
                            Utils.playConfirmSound(p);
                        } else {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please put a valid block.");
                            Utils.playFailSound(p);
                        }
                    }

                    ItemDisplay itemDisplay = (ItemDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);
                    Vector3f scale;
                    if (ShopManager.getProducts(getLastClickedChestPosition(p)).get(0).getType() == Material.WITHER_SKELETON_SKULL) {
                        scale = new Vector3f(.5f,.5f,.5f);
                    } else if (ShopManager.getProducts(getLastClickedChestPosition(p)).get(0).getType().isBlock()) {
                        scale = new Vector3f(.3f,.3f,.3f);
                    } else {
                        scale = new Vector3f(.4f,.4f,.4f);
                    }
                    if (shop.getBlock().getType() == Material.CHEST) {
                        itemDisplay.setTransformation(new Transformation(new Vector3f(0f, .25f, 0f), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                    } else {
                        itemDisplay.setTransformation(new Transformation(new Vector3f(0f, .375f, 0f), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));

                    }
                    itemDisplay.setItemStack(ShopManager.getProducts(shop).get(0));
                    itemDisplay.setViewRange(.2f);
                    itemDisplay.setShadowRadius(0f);
                    itemDisplay.setBillboard(Display.Billboard.VERTICAL);
                    displayEntitiesList.add(itemDisplay.getUniqueId().toString());
                    ShopManager.setEntitiesList(shop, displayEntitiesList);
                    ShopManager.setDisplay(shop, "glass");

                    p.openInventory(ShopMenu.getStyleMenu(shop));

                }
                if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ShopMenu.getFrameDisplayButton().getItemMeta().getDisplayName())) {
                    //Frame Display

                    //Check if block
                    List<String> displayEntitiesList = new ArrayList<>();
                    Location shop = getLastClickedChestPosition(p).clone();
                    Location loc = getLastClickedChestPosition(p).clone().add(.5, 1, .5);
                    if (shop.getBlock().getType() != Material.CHEST) {

                        Material oldFrameDisplayMaterial = Material.CYAN_TERRACOTTA;

                        if (!ShopManager.getEntitiesList(shop).isEmpty()) {
                            if ((Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(0)))) != null && ShopManager.getDisplay(shop).equals("frame")) {
                                if (((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(0)))).getBlock().getMaterial() != Material.AIR) {
                                    oldFrameDisplayMaterial = ((BlockDisplay) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(shop).get(0)))).getBlock().getMaterial();
                                }
                            }
                        }

                        ShopManager.getEntitiesList(getLastClickedChestPosition(p)).forEach(uuidString -> {
                            UUID uuid = UUID.fromString(uuidString);
                            if (Bukkit.getEntity(uuid) != null) {
                                Bukkit.getEntity(uuid).remove();
                            }
                        });

                        BlockDisplay topFrame = (BlockDisplay) loc.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                        topFrame.setTransformation(new Transformation(new Vector3f(-.525f,-.135f,-.525f), new AxisAngle4f(), new Vector3f(1.05f,.075f,1.05f), new AxisAngle4f()));
                        topFrame.setViewRange(.2f);
                        topFrame.setShadowRadius(0f);
                        displayEntitiesList.add(topFrame.getUniqueId().toString());

                        BlockDisplay bottomFrame = (BlockDisplay) loc.getWorld().spawnEntity(loc, EntityType.BLOCK_DISPLAY);
                        bottomFrame.setTransformation(new Transformation(new Vector3f(-.525f,-.925f,-.525f), new AxisAngle4f(), new Vector3f(1.05f,.075f,1.05f), new AxisAngle4f()));
                        bottomFrame.setViewRange(.2f);
                        bottomFrame.setShadowRadius(0f);
                        displayEntitiesList.add(bottomFrame.getUniqueId().toString());

                        if (e.getCursor() != null && e.getCursor().getType().isBlock() && e.getCursor().getType().isSolid()) {
                            topFrame.setBlock(e.getCursor().getType().createBlockData());
                            bottomFrame.setBlock(e.getCursor().getType().createBlockData());
//                            e.getCurrentItem().setType(e.getCursor().getType());
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Frame display with outer frame created.");
                            Utils.playConfirmSound(p);
                        } else {
                            e.getCurrentItem().setType(ShopMenu.getFrameDisplayButton().getType());
                            if (e.getCursor().getType() == Material.AIR) {
                                topFrame.setBlock(Material.AIR.createBlockData());
                                bottomFrame.setBlock(Material.AIR.createBlockData());
                                p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Frame display with no outer frame created.");
                                p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GOLD + "" + ChatColor.ITALIC + "Not: " + ChatColor.YELLOW + "" + ChatColor.ITALIC + "Click the button with a block to add an outer frame.");
                                Utils.playConfirmSound(p);
                            } else {
                                p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please put a valid block.");
                                topFrame.setBlock(oldFrameDisplayMaterial.createBlockData());
                                bottomFrame.setBlock(oldFrameDisplayMaterial.createBlockData());
                                Utils.playFailSound(p);
                            }
                        }

                        ItemDisplay itemDisplay = (ItemDisplay) e.getView().getPlayer().getWorld().spawnEntity(loc, EntityType.ITEM_DISPLAY);
                        Vector3f scale;
                        Float offset;
                        if (ShopManager.getProducts(getLastClickedChestPosition(p)).get(0).getType().isBlock()) {
                            scale = new Vector3f(.5f,.5f,.5f);
                            offset = .3f;
                        } else {
                            scale = new Vector3f(.5f,.5f,.5f);
                            offset = .5f;
                        }
                        if (p.getFacing() == BlockFace.NORTH) {
                            itemDisplay.setTransformation(new Transformation(new Vector3f(0f, -.49f, offset), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                        } else if (p.getFacing() == BlockFace.SOUTH) {
                            itemDisplay.setTransformation(new Transformation(new Vector3f(0f, -.49f, -offset), new AxisAngle4f(0f, 0f, 0f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                        } else if (p.getFacing() == BlockFace.WEST) {
                            itemDisplay.setTransformation(new Transformation(new Vector3f(offset, -.49f, 0f), new AxisAngle4f(3.14f/2f, 0f, 1f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                        } else {
                            itemDisplay.setTransformation(new Transformation(new Vector3f(-offset, -.49f, 0f), new AxisAngle4f(3.14f/2f, 0f, 1f, 0f), scale, new AxisAngle4f(0f, 0f, 0f, 0f)));
                        }
                        itemDisplay.setItemStack(ShopManager.getProducts(shop).get(0));
                        itemDisplay.setViewRange(.2f);
                        itemDisplay.setShadowRadius(0f);
                        displayEntitiesList.add(itemDisplay.getUniqueId().toString());

                        ShopManager.setEntitiesList(shop, displayEntitiesList);
                        ShopManager.setDisplay(getLastClickedChestPosition(p), "frame");

                        p.openInventory(ShopMenu.getStyleMenu(shop));

                        if (loc.getBlock().getType() == Material.LIGHT) {
                            loc.getBlock().setType(Material.AIR);
                        }
                    } else {
                        Utils.playFailSound(p);
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't add a frame display when the shop block is a chest.");
                    }
                }
                if (e.getCurrentItem().equals(ShopMenu.getGlowingDisplayButton())) {
                    //Glow
                    if (ShopManager.getDisplay(getLastClickedChestPosition(p)).equals("glass")) {
                        //Add Glow
                        Location loc = getLastClickedChestPosition(p).clone().add(0,1,0);
                        if (loc.getBlock().getType() == Material.AIR) {
                            loc.getBlock().setType(Material.LIGHT);
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Light added.");
                            ShopManager.setDisplay(getLastClickedChestPosition(p), "glass_glowing");
                            Utils.playConfirmSound(p);
                        } else {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please break the block above the shop.");
                            Utils.playFailSound(p);
                        }
                        p.closeInventory();
                    } else if (ShopManager.getDisplay(getLastClickedChestPosition(p)).equals("glass_glowing")) {
                        //Remove Glow
                        if (getLastClickedChestPosition(p).clone().add(0,1,0).getBlock().getType() == Material.LIGHT) {
                            getLastClickedChestPosition(p).clone().add(0, 1, 0).getBlock().setType(Material.AIR);
                        }
                        p.closeInventory();
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Light removed.");
                        Utils.playConfirmSound(p);
                        ShopManager.setDisplay(getLastClickedChestPosition(p), "glass");
                    } else {
                        p.closeInventory();
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can only add light to a glass display.");
                        Utils.playFailSound(p);
                    }
                }
                if (e.getCurrentItem().equals(ShopMenu.getDeleteDisplayButton())) {
                    getLastClickedChestPosition(p).clone().add(0,1,0).getBlock().setType(Material.AIR);
                    ShopManager.getEntitiesList(getLastClickedChestPosition(p)).forEach(uuidString -> {
                        UUID uuid = UUID.fromString(uuidString);
                        if (Bukkit.getEntity(uuid) != null) {
                            Bukkit.getEntity(uuid).remove();
                        }
                    });
                    p.closeInventory();
                    p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.GREEN + "Display removed.");
                    Utils.playConfirmSound(p);
                    ShopManager.setDisplay(getLastClickedChestPosition(p), "default");
                }
            }
        }
    }

    List<Material> glassList = new ArrayList<>(Arrays.asList(Material.GLASS, Material.TINTED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.BLACK_STAINED_GLASS,
            Material.BLUE_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.ORANGE_STAINED_GLASS,
            Material.MAGENTA_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.WHITE_STAINED_GLASS,
            Material.GRAY_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.YELLOW_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.PINK_STAINED_GLASS));

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (ShopManager.isPlayerConnectingTethers(e.getPlayer())) {
            if (ShopManager.getSelectingPile(e.getPlayer()) == 1) {
                //Stock Pile connect
                if (e.getClickedBlock() != null) {
                    if (e.getClickedBlock().getType() == Material.CHEST || e.getClickedBlock().getType() == Material.BARREL) {
                        e.setCancelled(true);
                        if (ShopManager.isShop(e.getClickedBlock().getLocation())) {
                            e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't connect this chest.");
                            Utils.playFailSound(e.getPlayer());
                        } else {
                            ShopManager.addStockPile(e.getPlayer(), e.getClickedBlock().getLocation());
                            ShopManager.removeSelectingPile(e.getPlayer());
                        }
                    } else {
                        e.setCancelled(true);
                        ShopManager.clearTethers(e.getPlayer());
                        ShopManager.removeSelectingPile(e.getPlayer());
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Stockpile selection cancelled.");
                        Utils.playFailSound(e.getPlayer());
                    }
                } else {
                    e.setCancelled(true);
                    ShopManager.clearTethers(e.getPlayer());
                    ShopManager.removeSelectingPile(e.getPlayer());
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Stockpile selection cancelled.");
                    Utils.playFailSound(e.getPlayer());
                }
            } else if (ShopManager.getSelectingPile(e.getPlayer()) == 2) {
                //Earnings Pile connect
                if (e.getClickedBlock() != null) {
                    if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                        e.setCancelled(true);
                        ShopManager.addEarningsPile(e.getPlayer(), e.getClickedBlock().getLocation());
                        ShopManager.removeSelectingPile(e.getPlayer());
                        Utils.playConfirmSound(e.getPlayer());
                    } else {
                        e.setCancelled(true);
                        ShopManager.clearTethers(e.getPlayer());
                        ShopManager.removeSelectingPile(e.getPlayer());
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Earnings pile selection cancelled.");
                        Utils.playFailSound(e.getPlayer());
                    }
                } else {
                    e.setCancelled(true);
                    ShopManager.clearTethers(e.getPlayer());
                    ShopManager.removeSelectingPile(e.getPlayer());
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Earnings pile selection cancelled.");
                    Utils.playFailSound(e.getPlayer());
                }
            } else if (ShopManager.getSelectingPile(e.getPlayer()) == 3) {
                //Pile-Earnings connect
                if (e.getClickedBlock() != null) {
                    if (ShopManager.isShop(e.getClickedBlock().getLocation())) {
                        e.setCancelled(true);
                        if (ShopManager.getOwner(e.getClickedBlock().getLocation()).equals(e.getPlayer().getName())) {
                            ShopManager.addEarningsPileToShop(e.getPlayer(), e.getClickedBlock().getLocation());
                            ShopManager.removeSelectingPile(e.getPlayer());
                            Utils.playConfirmSound(e.getPlayer());
                        } else {
                            e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can only add an earnings pile to your shops.");
                            Utils.playFailSound(e.getPlayer());
                        }
                    } else {
                        e.setCancelled(true);
                        ShopManager.clearTethers(e.getPlayer());
                        ShopManager.removeSelectingPile(e.getPlayer());
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Please click a shop.");
                        Utils.playFailSound(e.getPlayer());
                    }
                } else {
                    e.setCancelled(true);
                    ShopManager.clearTethers(e.getPlayer());
                    ShopManager.removeSelectingPile(e.getPlayer());
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Shop selection cancelled.");
                    Utils.playFailSound(e.getPlayer());
                }
            }
        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (e.getClickedBlock() != null) {
                if (ShopManager.isShop(e.getClickedBlock().getLocation())) {
                    if (!e.getPlayer().isSneaking() || !(e.getPlayer().getInventory().getItemInMainHand().getType().isBlock() && !e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
                        e.setCancelled(true);
                        setLastClickedChestLocations(e.getPlayer(), e.getClickedBlock().getLocation());
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_CHEST_OPEN, .3f, 1f);
                        if (ShopManager.getOwner(e.getClickedBlock().getLocation()).equals(e.getPlayer().getName())) {
                            //Open edit screen
                            e.getPlayer().openInventory(ShopMenu.getEditShopMenu(e.getPlayer()));
                        } else {
                            //Open Buy screen
                            e.getPlayer().openInventory(ShopMenu.getBuyMenu(e.getClickedBlock().getLocation()));
                        }
                    }
                } else if (e.getClickedBlock().getType() == Material.ENDER_CHEST) {
                    if (e.getPlayer().isSneaking() && !Tag.ALL_SIGNS.getValues().contains(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                        e.setCancelled(true);
                        List<Location> shops = ShopManager.getConnectedShopsToEarningPile(e.getClickedBlock().getLocation());
                        AtomicBoolean canOpen = new AtomicBoolean(true);
                        shops.forEach(shop -> {
                            if (!ShopManager.getOwner(shop).equals(e.getPlayer().getName())) {
                                canOpen.set(false);
                            }
                        });
                        if (canOpen.get()) {
                            setLastClickedChestLocations(e.getPlayer(), e.getClickedBlock().getLocation());
                            e.getPlayer().openInventory(ShopMenu.getCollectMenu());
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, .3f,1f);
                        } else {
                            e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't access someone else's stockpile.");
                            Utils.playFailSound(e.getPlayer());
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest || e.getInventory().getHolder() instanceof Barrel) {
            Location location = e.getInventory().getLocation();
            Location shopLocationFromStock = ShopManager.getShopLocationFromStockPile(location);
            Player p = (Player) e.getPlayer();
            if (Tag.ALL_SIGNS.getValues().contains(p.getInventory().getItemInMainHand().getType())) {
                e.setCancelled(true);
                ShopManager.clearTethers(p);
                if (e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof Barrel) {
                    if (shopLocationFromStock != null) {
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't create a shop on a stockpile.");
                        Utils.playFailSound(p);
                    } else {
                        setLastClickedChestLocations(p, location);
                        InventoryHolder invHolder = (InventoryHolder) e.getInventory().getLocation().getBlock().getState();
                        if (ShopManager.isShop(e.getInventory().getLocation())) {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "There's already a shop here.");
                            Utils.playFailSound(p);
                        } else if (invHolder.getInventory().isEmpty()) {
                            p.openInventory(ShopMenu.getCreateShopMenu());
                        } else {
                            p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "The chest must be empty to create a shop.");
                            Utils.playFailSound(p);
                        }
                    }
                } else if (e.getInventory().getHolder() instanceof DoubleChest) {
                    e.getView().getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't create a shop on double chests.");
                    Utils.playFailSound(p);
                }
            }
            if (shopLocationFromStock != null) {
                if (!ShopManager.getOwner(shopLocationFromStock).equals(p.getName())) {
                    if (p.isOp()) {
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You accessed someone else's stockpile.");
                    } else {
                        e.setCancelled(true);
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't access someone else's stockpile.");
                    }
                }
            }
//            if (location != null) {
//                if (ShopManager.isShop(location)) {
//                    e.setCancelled(true);
//                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You must click without a block to open the shop.");
//                    Utils.playFailSound((Player) e.getPlayer());
//                }
//            }
        }
    }

    @EventHandler
    private void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            if (ShopManager.isShop(getLastClickedChestPosition(p))) {
                if (e.getView().getTitle().equals(ShopMenu.getSelfStockTitle())) {
                    ShopManager.saveInventoryToConfig(getLastClickedChestPosition(p), e.getView().getTopInventory());
                }
            }
        } catch (Exception ignored) { }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (ShopManager.isPlayerConnectingTethers(e.getPlayer())) {
            ShopManager.clearTethers(e.getPlayer());
            e.getPlayer().resetTitle();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!ShopManager.getOutOfStockLocations(p.getName()).isEmpty()) {
            Bukkit.getScheduler().runTaskLater(plugin, task -> {
                List<Location> list = ShopManager.getOutOfStockLocations(p.getName());
                ShopManager.getOutOfStockLocations(p.getName()).forEach(location -> {
                    if (ShopManager.hasStock(location)) {
                        list.remove(location);
                        if (ShopManager.warnedPlayerLocations.containsKey(p)) {
                            ShopManager.warnedPlayerLocations.get(p).remove(location);
                        }
                    } else {
                        p.sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Your shop at " + ChatColor.BOLD + (int) location.getX() + " " + (int) location.getY() + " " + (int) location.getZ() + ChatColor.RED + " ran out of stock.");
                    }
                });
                ShopManager.setOutOfStockLocations(p.getName(), list);
                Utils.playFailSound(p);
            },20L);
        }
    }
    public static void checkGlassDisplay(Location location, Material oldMat, Material newMat) {
        if (ShopManager.getDisplay(location).equals("glass") || ShopManager.getDisplay(location).equals("glass_glowing")) {
            if (oldMat == Material.CHEST && newMat != Material.CHEST) {
                ShopManager.getEntitiesList(location).forEach(uuidString -> {
                    UUID uuid = UUID.fromString(uuidString);
                    if (Bukkit.getEntity(uuid) != null) {
                        Display display = (Display) Bukkit.getEntity(uuid);
                        Transformation old = display.getTransformation();
                        display.setTransformation(new Transformation(old.getTranslation().add(0,.125f, 0), old.getLeftRotation(), old.getScale(), old.getRightRotation()));
                    }
                });
                Display display = (Display) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(1)));
                Transformation old = display.getTransformation();
                display.setTransformation(new Transformation(new Vector3f(-.4f,-.025f,-.4f), old.getLeftRotation(), new Vector3f(.8f,.1f,.8f), old.getRightRotation()));
            } else if (oldMat != Material.CHEST && newMat == Material.CHEST) {
                ShopManager.getEntitiesList(location).forEach(uuidString -> {
                    UUID uuid = UUID.fromString(uuidString);
                    if (Bukkit.getEntity(uuid) != null) {
                        Display display = (Display) Bukkit.getEntity(uuid);
                        Transformation old = display.getTransformation();
                        display.setTransformation(new Transformation(old.getTranslation().add(0,-.125f, 0), old.getLeftRotation(), old.getScale(), old.getRightRotation()));
                    }
                });
                Display display = (Display) Bukkit.getEntity(UUID.fromString(ShopManager.getEntitiesList(location).get(1)));
                display.setTransformation(new Transformation(new Vector3f(-.35f, -.15f, -.35f), new AxisAngle4f(0f, 0f, 0f, 0f), new Vector3f(0.7f, 0.1f, 0.7f), new AxisAngle4f(0f, 0f, 0f, 0f)));
            }
        }
    }
}
