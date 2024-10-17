package me.clickism.clickshop.menu;

import me.clickism.clickshop.Main;
import me.clickism.clickshop.data.Message;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class Menu {

    public static final List<Integer> PRODUCT_SLOTS = Arrays.asList(14, 15, 16, 23, 24, 25, 32, 33, 34);
    public static final List<Integer> EDITABLE_SLOTS = Arrays.asList(19, 14, 15, 16, 23, 24, 25, 32, 33, 34);
    public static final int PRICE_SLOT = 19;

    public static final int MAX_SIZE = 45;

    private final Player player;
    private final Location location;
    private final int size;
    private final ClickHandler clickHandler;
    private final MenuColor color;
    private final String title;

    private Inventory inventory;

    public Menu(Player player, Location location, int size, ClickHandler clickHandler,
                MenuColor color, Message title) {
        this(player, location, size, clickHandler, color, title.toString());
    }

    public Menu(Player player, Location location, int size, ClickHandler clickHandler,
                MenuColor color, String title) {
        if (size > MAX_SIZE) throw new IllegalArgumentException("Menu size must not be higher than " + MAX_SIZE);
        this.player = player;
        this.location = location;

        this.size = size;
        this.clickHandler = clickHandler;
        this.color = color;
        this.title = title;
    }

    public void open() {
        inventory = Bukkit.createInventory(null, size, title);
        decorate();
        Main.getMain().getMenuListener().registerActiveMenu(inventory, this);
        player.openInventory(inventory);
    }

    protected abstract void setupButtons();

    private final Map<Integer, Button> buttonMap = new HashMap<>();

    protected void addButton(Button button) {
        buttonMap.put(button.getSlot(), button);
    }

    protected void decorate() {
        for (int i = 0; i < size; i++) {
            addButton(color.getBackgroundButton(i));
        }
        setupButtons();
        placeButtons();
    }

    private void placeButtons() {
        buttonMap.forEach((slot, button) -> inventory.setItem(slot, button.getItem()));
    }

    protected void onClick(InventoryClickEvent event) {
        if (!clickHandler.isValidClick(event)) {
            event.setCancelled(true);
            return;
        }
        Button button = buttonMap.get(event.getRawSlot());
        if (button == null) return;
        if (!clickHandler.handleClick(event)) {
            return;
        }
        button.handleClick(event);
    }

    protected void onDrag(InventoryDragEvent event) {
        clickHandler.handleDrag(event);
    }

    protected void onClose(InventoryCloseEvent event) {
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

}
