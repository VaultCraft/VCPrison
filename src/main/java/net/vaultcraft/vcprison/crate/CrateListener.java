package net.vaultcraft.vcprison.crate;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Connor Hollasch on 9/26/14.
 */

public class CrateListener implements Listener {

    public CrateListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (MineCrateInjector.getInjected().contains(block.getLocation())) {
            MineCrateInjector.getInjected().remove(block.getLocation());
            Player player = event.getPlayer();

            player.playSound(block.getLocation(), Sound.ITEM_BREAK, 1, 0);
            Chest chest = (Chest)event.getBlock().getState();
            for (ItemStack stack : chest.getInventory().getContents()) {
                if (stack == null)
                    continue;

                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(chest.getLocation(), stack);
                } else {
                    player.getInventory().addItem(stack);
                }
            }
            player.updateInventory();
            chest.getInventory().clear();
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest)event.getInventory().getHolder();
            onBlockBreak(new BlockBreakEvent(chest.getBlock(), (Player) event.getPlayer()));
            ((Player) event.getPlayer()).playEffect(chest.getLocation(), Effect.STEP_SOUND, Material.CHEST.getId());
            chest.getBlock().setType(Material.AIR);
        }
    }
}