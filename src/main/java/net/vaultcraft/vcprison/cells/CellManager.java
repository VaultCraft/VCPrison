package net.vaultcraft.vcprison.cells;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.string.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CellManager {

    private World plotWorld;
    private HashMap<PrisonUser, List<Cell>> allTheCells = new HashMap<>();

    public CellManager(World plotWorld) {
        this.plotWorld = plotWorld;
    }

    public List<Cell> getCellsFromPlayer(Player player) {
        PrisonUser user = PrisonUser.fromPlayer(player);
        if(!allTheCells.containsKey(user)) {
            List<DBObject> objects = VCUtils.getInstance().getMongoDB().queryMutiple(VCUtils.mongoDBName, "Cells", "OwnerUUID", player.getUniqueId().toString());
            ArrayList<Cell> cells = new ArrayList<>();
            for (DBObject o : objects) {
                Cell cell = new Cell();
                cell.ownerUUID = UUID.fromString((String) o.get("OwnerUUID"));
                String[] chunkString = ((String) o.get("Chunk")).split(",");
                cell.chunkX = Integer.parseInt(chunkString[0]);
                cell.chunkZ = Integer.parseInt(chunkString[1]);
                for (String s : ((String) o.get("Members")).split(",")) {
                    cell.additionalUUIDs.add(UUID.fromString(s));
                }
                cell.name = (String) o.get("Name");
                cell.cellSpawn = stringToLocation((String) o.get("SpawnPoint"));
            }
            allTheCells.put(user, cells);
        }

        return allTheCells.get(user);
    }

    public Cell getCellFromLocation(Location location) {
        if(location.getWorld() != this.plotWorld) {
            throw new IllegalArgumentException("World must be ChunkWorld!");
        }
        DBObject o = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", location.getChunk().getX() + "," + location.getChunk().getZ());

        Cell cell = new Cell();
        cell.ownerUUID = UUID.fromString((String) o.get("OwnerUUID"));
        String[] chunkString = ((String) o.get("Chunk")).split(",");
        cell.chunkX = Integer.parseInt(chunkString[0]);
        cell.chunkZ = Integer.parseInt(chunkString[1]);
        for(String s : ((String)o.get("Members")).split(",")) {
            cell.additionalUUIDs.add(UUID.fromString(s));
        }
        cell.name = (String) o.get("Name");
        cell.cellSpawn = stringToLocation((String) o.get("SpawnPoint"));


        return cell;
    }

    private Location stringToLocation(String s) {
        String[] strings = s.split(" ");
        return new Location(plotWorld, Double.parseDouble(strings[0]), Double.parseDouble(strings[1]),
                Double.parseDouble(strings[2]), Float.parseFloat(strings[3]), Float.parseFloat(strings[4]));
    }




}