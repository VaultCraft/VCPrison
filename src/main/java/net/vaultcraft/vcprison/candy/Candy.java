package net.vaultcraft.vcprison.candy;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public interface Candy extends Listener {

    public Recipe getRecipe();

    public ItemStack getCandyItem();

    public int getCooldown();

    public int getHarmfulAfter();

    public ItemStack onCandyConsume(Player player, boolean harmful);
}
