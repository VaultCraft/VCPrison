package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class Plot {

    private static Gson gson = new Gson();
    private String plotUUID = "";
    private String ownerUUID = "";
    private List<String> canBuildUUIDs = new ArrayList<>();
    private String plotSpawn;
    private String plotArea;
    private int chunkX;
    private int chunkZ;

    public Plot(CuboidSelection plotArea, int chunkX, int chunkZ) {
        Location minPoint = PlotWorld.getPlotWorld().getChunkAt(chunkX, chunkZ).getBlock(plotArea.getMinimumPoint().getBlockX(),
                plotArea.getMinimumPoint().getBlockY(), plotArea.getMinimumPoint().getBlockZ()).getLocation();
        Location maxPoint = PlotWorld.getPlotWorld().getChunkAt(chunkX, chunkZ).getBlock(plotArea.getMaximumPoint().getBlockX(),
                plotArea.getMaximumPoint().getBlockY(), plotArea.getMaximumPoint().getBlockZ()).getLocation();
        this.plotUUID = UUID.randomUUID().toString();
        this.plotArea = locationToString(minPoint) + "," + locationToString(maxPoint);
        this.plotSpawn = locationToString(minPoint);
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        Bukkit.getScheduler().runTaskTimerAsynchronously(VCPrison.getInstance(),
                () -> VCPrison.getInstance().getConfig().set("Plots.GenPlots." + this.plotUUID, gson.toJson(this)), 1200l, 1200l);
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void addBuildUUID(String uuid) {
        canBuildUUIDs.add(uuid);
    }

    public void removeBuildUUID(String uuid) {
        canBuildUUIDs.remove(uuid);
    }

    public boolean hasOwner() {
        return !ownerUUID.isEmpty();
    }

    public boolean canBuild(Player player) {
        String uuid = player.getUniqueId().toString();
        return uuid.equals(ownerUUID) || canBuildUUIDs.contains(uuid);
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public Location getPlotSpawn() {
        return locationFromString(plotSpawn);
    }

    public CuboidSelection getPlotArea() {
        String[] parts = plotArea.split(",");
        return new CuboidSelection(PlotWorld.getPlotWorld(), locationFromString(parts[0]), locationFromString(parts[1]));
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setPlotSpawn(Location plotSpawn) {
        this.plotSpawn = locationToString(plotSpawn);
    }

    public List<String> getCanBuildUUIDs() {
        return canBuildUUIDs;
    }

    public boolean delete() {
        ownerUUID = "";
        canBuildUUIDs.clear();
        plotSpawn = locationToString(getPlotArea().getMinimumPoint());
        EditSession editSession = new EditSession(BukkitUtil.getLocalWorld(PlotWorld.getPlotWorld()), -1);
        try {
            editSession.setBlocks(getPlotArea().getRegionSelector().getRegion(), new BaseBlock(0));
        } catch (MaxChangedBlocksException | IncompleteRegionException e) {
            Logger.error(VCPrison.getInstance(), e);
            return false;
        }
        return true;
    }

    private Location locationFromString(String s) {
        String[] parts = s.split(" ");
        return new Location(PlotWorld.getPlotWorld(), Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2]));
    }

    private String locationToString(Location l) {
        return l.getBlockX() + " " + l.getBlockY() + " " + l.getBlockZ();
    }

    public String getPlotUUID() {
        return plotUUID;
    }
}
