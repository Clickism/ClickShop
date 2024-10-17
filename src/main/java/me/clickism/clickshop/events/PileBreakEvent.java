package me.clickism.clickshop.events;

import me.clickism.clickshop.data.Message;
import me.clickism.clickshop.data.Permission;
import me.clickism.clickshop.shop.Pile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PileBreakEvent implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Pile pile = Pile.get(event.getBlock().getLocation());
        if (pile == null) return;
        // Break by owner
        Player player = event.getPlayer();
        if (pile.isOwner(player)) {
            pile.delete();
            Message.PILE_BREAK_OWNER.send(player);
            return;
        }
        // No permission
        if (Permission.DELETE.lacks(player)) {
            event.setCancelled(true);
            Message.PILE_NO_BREAK.send(player);
            return;
        }
        // Break by operator
        pile.delete();
        Message.PILE_BREAK_OPERATOR.parameterizer()
                .put("owner", pile.getOwnerName())
                .send(player);
    }
}
