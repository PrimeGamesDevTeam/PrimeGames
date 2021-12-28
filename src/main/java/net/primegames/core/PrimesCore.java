package net.primegames.core;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.primegames.core.gamemode.GameMode;
import net.primegames.core.gamemode.GameModeId;
import net.primegames.core.listener.CorePlayerListener;
import net.primegames.core.player.CorePlayerManager;
import net.primegames.core.providor.task.MySQLInitialCoreTask;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import net.primegames.core.providor.MySqlProvider;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

public final class PrimesCore extends JavaPlugin {

    private MySqlProvider mySQLprovider;
    private static PrimesCore instance;
    @Getter
    private GameMode gameMode;
    @Getter
    private LuckPerms luckPerms;
    @Getter
    private CorePlayerManager corePlayerManager;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        this.mySQLprovider = new MySqlProvider();
        this.corePlayerManager = new CorePlayerManager();
    }

    @Override
    public void onEnable() {

        //initialize Core Table
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask());

        //register core listeners
        registerListeners();

        //Load GameMode
        try {
            loadGameMode();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (gameMode != null) {
                getLogger().log(Level.INFO, ChatColor.BOLD + "" + ChatColor.GREEN + "GameMode loaded: " + ChatColor.YELLOW + gameMode.getId().getIdentifier());
                gameMode.enable();
            } else {
                getLogger().log(Level.SEVERE, "Failed to load game mode. Shutting down server.");
                getServer().shutdown();
            }
        }

        //Load LuckPerms
        this.luckPerms = LuckPermsProvider.get();
    }

    @Override
    public void onDisable() {

        try {
            gameMode.disable();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //close mysql connection
        try {
            getMySQLProvider().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MySqlProvider getMySQLProvider(){
        return mySQLprovider;
    }

    public static PrimesCore getInstance() {
        return instance;
    }

    private void registerListeners(){
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new CorePlayerListener(), this);
    }

    private void loadGameMode() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String gamemodeName = getConfig().getString("GameMode");
        GameModeId gamemodeId = null;
        try {
            gamemodeId = GameModeId.getGameModeId(gamemodeName);
        } catch (IllegalArgumentException e) {
            getLogger().log(Level.SEVERE, "Invalid gamemode name provided in config file: " + gamemodeName + ". Possible values: " + Arrays.toString(GameModeId.values()));
            e.printStackTrace();
        }
        assert gamemodeId != null;
        this.gameMode = gamemodeId.getGameModeClass().getDeclaredConstructor().newInstance();
    }
}
