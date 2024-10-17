package me.clickism.clickshop.events;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.menu.pile.EarningsMenu;
import me.clickism.clickshop.shop.ItemShop;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class PileInteractEvent extends InteractEvent {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        handleInteract(event);
    }

    @Override
    protected boolean onInteract(Player player, Block block) {
        if (!player.isSneaking()) return false;
        if (block.getType() != Material.ENDER_CHEST) return false;
        if (ItemShop.get(block.getLocation()) != null) return false;
        if (Permission.EARNINGS_PILE.lacksAndNotify(player)) {
            return true;
        }
        Location location = block.getLocation();
        Pile pile = Pile.get(location);
        if (pile == null) {
            if (Pile.isPileLimitReachedForAndWarn(player)) {
                return true;
            }
            pile = new Pile(location, player);
        } else if (!pile.isOwner(player)) {
            Message.PILE_NOT_OWNER.send(player);
            return true;
        }

        new EarningsMenu(player, pile).open();
        player.playSound(player, Sound.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, .5f, .8f);
        return true;
    }
}
