package net.vaultcraft.vcprison.cells;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class CellPop extends BlockPopulator {
    CuboidClipboard cells;
    CuboidClipboard hallway;

    public CellPop(CuboidClipboard cells, CuboidClipboard hallway) {
        this.cells = cells;
        this.hallway = hallway;
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int y = 80; y < 99; y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (chunk.getX() % 2 == 0) {
                        if (cells.getBlock(new Vector(x, y - 80, z)).getId() != 0) {
                            if (cells.getBlock(new Vector(x, y - 80, z)).getData() != 0) {
                                block.setType(Material.getMaterial(cells.getBlock(new Vector(x, y - 80, z)).getId()));
                                block.setData((byte) cells.getBlock(new Vector(x, y - 80, z)).getData());
                            }
                        }
                    } else {
                        if (hallway.getBlock(new Vector(x, y - 80, z)).getId() != 0) {
                            if (hallway.getBlock(new Vector(x, y - 80, z)).getData() != 0) {
                                block.setType(Material.getMaterial(hallway.getBlock(new Vector(x, y - 80, z)).getId()));
                                block.setData((byte) hallway.getBlock(new Vector(x, y - 80, z)).getData());
                            }
                        }
                    }
                }
            }
        }
    }
}
