package me.clickism.clickshop.events;

import me.clickism.clickshop.ShopManager;
import me.clickism.clickshop.Utils;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class BlockEvent implements Listener {

    private HashMap<Player, Location> fromList = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (ShopManager.isPlayerConnectingTethers(e.getPlayer())) {
            if (e.getTo() != null) {
                if (!fromList.containsKey(e.getPlayer())) {
                    fromList.put(e.getPlayer(), e.getTo());
                }
                if (fromList.get(e.getPlayer()).distance(e.getTo()) > 0.5) {
                        Location anchor = ShopManager.getAnchorTetherLocationFromPlayer(e.getPlayer());
                        if (anchor != null) {
                            double distance = anchor.distance(e.getPlayer().getLocation());
                            float pitch = (float) (distance / 15 + .5f);
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_FRAME_ADD_ITEM, 1f, pitch);
                            fromList.put(e.getPlayer(), e.getTo());
                        }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (ShopManager.isShop(e.getBlock().getLocation().clone().add(0,-1,0)) && (ShopManager.getDisplay(e.getBlock().getLocation().clone().add(0,-1,0)).equals("glass") || ShopManager.getDisplay(e.getBlock().getLocation().clone().add(0,-1,0)).equals("glass_glowing"))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (ShopManager.isShop(e.getBlock().getLocation())) {
            e.setCancelled(true);
            if (!e.getPlayer().getName().equals(ShopManager.getOwner(e.getBlock().getLocation()))) {
                if (e.getPlayer().isOp()) {
                    if (Tag.ALL_SIGNS.getValues().contains(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You broke someone else's shop.");
                        ShopManager.deleteShop(e.getBlock().getLocation());
                    } else {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Have a sign in your hand in order to break someone else's shop.");
                    }
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't break someone else's shop.");
                    Utils.playFailSound(e.getPlayer());
                }
            } else {
                ShopManager.deleteShop(e.getBlock().getLocation());
                e.getPlayer().closeInventory();
                e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Shop removed.");
                Utils.playFailSound(e.getPlayer());
            }
        } if (e.getBlock().getType() == Material.CHEST || e.getBlock().getType() == Material.BARREL) {
            Location location = e.getBlock().getLocation();
            if (((InventoryHolder) e.getBlock().getState()).getInventory().getHolder() instanceof DoubleChest) {
                DoubleChest doubleChest = (DoubleChest) ((Chest) e.getBlock().getState()).getInventory().getHolder();
                location = doubleChest.getInventory().getLocation();
            }
            Location shopLocation = ShopManager.getShopLocationFromStockPile(location);
            if (shopLocation != null) {
                if (ShopManager.getOwner(shopLocation).equals(e.getPlayer().getName())) {
                    if (e.getBlock().getLocation() != shopLocation) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't break a stockpile before disconnecting it.");
                        Utils.playFailSound(e.getPlayer());
                    } else {
                        ShopManager.deleteShop(shopLocation);
                        e.getPlayer().closeInventory();
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Shop removed.");
                        Utils.playFailSound(e.getPlayer());
                    }
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't break someone else's stockpile.");
                    Utils.playFailSound(e.getPlayer());
                }
            }
        } else if (!ShopManager.getConnectedShopsToEarningPile(e.getBlock().getLocation()).isEmpty()) {
            if (ShopManager.getOwner(ShopManager.getConnectedShopsToEarningPile(e.getBlock().getLocation()).get(0)).equals(e.getPlayer().getName())) {
                e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You broke your earnings pile.");
                Utils.playFailSound(e.getPlayer());
                ShopManager.getConnectedShopsToEarningPile(e.getBlock().getLocation()).forEach(shop -> {
                    ShopManager.setEarningsPile(shop, null);
                });
            } else {
                if (e.getPlayer().isOp()) {
                    if (Tag.ALL_SIGNS.getValues().contains(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You broke someone else's earnings pile.");
                        ShopManager.getConnectedShopsToEarningPile(e.getBlock().getLocation()).forEach(shop -> {
                            ShopManager.setEarningsPile(shop, null);
                        });
                    } else {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "Have a sign in your hand in order to break someone else's earnings pile.");
                    }
                } else {
                    e.getPlayer().sendMessage(ChatColor.GOLD + ">> " + ChatColor.RED + "You can't break someone else's earnings pile.");
                }
            }
        }
    }
}
