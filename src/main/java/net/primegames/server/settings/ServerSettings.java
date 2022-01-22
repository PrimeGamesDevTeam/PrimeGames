package net.primegames.server.settings;

import lombok.Getter;
import net.primegames.plugin.PrimePlugin;
import net.primegames.server.GameServerSettings;
import org.bukkit.*;
import org.bukkit.util.Vector;

import java.io.File;

public abstract class ServerSettings {

    @Getter
    private final World lobby;
    @Getter
    private final PrimePlugin plugin;


    public ServerSettings(PrimePlugin plugin){
        this.plugin = plugin;
        this.lobby = loadAndGet(getLobbyWorldName(), getLobbySpawn());
    }

    protected World loadAndGet(String worldName, Vector defaultSpawn) {
        World world = loadAndGet(worldName);
        world.setSpawnLocation(new Location(world, defaultSpawn.getX(), defaultSpawn.getY(), defaultSpawn.getZ()));
        return world;
    }

    protected World loadAndGet(String worldName){
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            String worldPath = Bukkit.getWorldContainer().getPath() + "/" + worldName;
            File worldFile = new File(worldPath);
            if (!worldFile.isDirectory()){
                Bukkit.shutdown();
                throw new IllegalStateException("World file not found: " + worldPath);
            }else {
                world = new WorldCreator(worldName).createWorld();
            }
        }
        return world;
    }

    public abstract GameServerSettings getServerSettings();
    public abstract String getLobbyWorldName();
    public abstract Vector getLobbySpawn();

}
