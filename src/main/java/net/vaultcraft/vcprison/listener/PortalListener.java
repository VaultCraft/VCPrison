package net.vaultcraft.vcprison.listener;

import net.vaultcraft.vcessentials.VCEssentials;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PortalListener implements Listener {
    private List<Player> using = new ArrayList<>();

    public PortalListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getTo().getBlock().getType().equals(Material.PORTAL)) {
            if(using.contains(event.getPlayer())) {
                return;
            }

            for (String key : ProtectionManager.getInstance().getRegions().keySet()) {
                if (!(key.contains("ffa_portal")))
                    continue;

                ProtectedArea area = ProtectionManager.getInstance().getRegions().get(key);
                if (area.getArea().isInArea(event.getTo())) {
                    event.getPlayer().performCommand("redir ffa");
                    using.add(event.getPlayer());
                    Bukkit.getScheduler().runTaskLater(VCPrison.getInstance(), () -> using.remove(event.getPlayer()), 5);
                }
            }

        }
    }
}
