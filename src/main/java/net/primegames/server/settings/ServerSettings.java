package net.primegames.server.settings;

import lombok.Getter;
import lombok.NonNull;
import net.primegames.components.vote.settings.VoteSettings;
import net.primegames.plugin.PrimePlugin;
import net.primegames.server.GameServerSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.util.Vector;

import java.io.File;

public abstract class ServerSettings implements VoteSettings {

    @Getter
    private final PrimePlugin plugin;


    public ServerSettings(@NonNull PrimePlugin plugin) {
        this.plugin = plugin;
        //this.lobby = loadAndGet(getLobbyWorldName(), getLobbySpawn());
    }

    protected World loadAndGet(@NonNull String worldName, @NonNull Vector defaultSpawn) {
        World world = loadAndGet(worldName);
        world.setSpawnLocation(new Location(world, defaultSpawn.getX(), defaultSpawn.getY(), defaultSpawn.getZ()));
        return world;
    }

    @NonNull
    protected World loadAndGet(@NonNull String worldName) {
        World world = plugin.getServer().getWorld(worldName);
        if (world == null) {
            String worldPath = Bukkit.getWorldContainer().getPath() + "/" + worldName;
            File worldFile = new File(worldPath);
            if (!worldFile.isDirectory()) {
                Bukkit.shutdown();
                throw new IllegalStateException("World file not found: " + worldPath);
            } else {
                world = new WorldCreator(worldName).createWorld();
            }
        }
        assert world != null;
        return world;
    }

    @NonNull
    public abstract GameServerSettings getServerSettings();

    @NonNull
    public abstract String getLobbyWorldName();

    @NonNull
    public abstract Location getLobbySpawnLocation();

}
