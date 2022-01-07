package net.primegames.listener;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ChunkSpawnerListener implements Listener {


    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if(!event.getBlock().getType().equals(Material.SPAWNER)) {
            return;
        }
        Chunk chunk = event.getBlock().getChunk();
        int count = 0;
        for (BlockState block : chunk.getTileEntities()) {
            if(block.getType().equals(Material.SPAWNER)) {
                if (count >= 1) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage("You can't place more than 1 spawner in a chunk!");
                    break;
                }
                count++;
            }
        }
    }

}
