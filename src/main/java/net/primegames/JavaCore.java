package net.primegames;

import lombok.Getter;
import net.primegames.listener.CorePlayerListener;
import net.primegames.player.CorePlayerManager;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.MySQLInitialCoreTask;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.sql.SQLException;

public final class JavaCore extends JavaPlugin {

    private static JavaCore instance;
    private MySqlProvider mySQLprovider;
    @Getter
    private CorePlayerManager corePlayerManager;

    public static JavaCore getInstance() {
        return instance;
    }

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
    }

    @Override
    public void onDisable() {

        //close mysql connection
        try {
            getMySQLProvider().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MySqlProvider getMySQLProvider() {
        return mySQLprovider;
    }

    private void registerListeners() {
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new CorePlayerListener(), this);
    }

}
