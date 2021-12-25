package net.primegames;

import net.primegames.listener.CorePlayerListener;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.task.MySQLInitialCoreTask;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public final class PrimesCore extends JavaPlugin {

    private MySqlProvider mySQLprovider;
    private static PrimesCore instance;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        mySQLprovider = new MySqlProvider();
    }

    @Override
    public void onEnable() {
        mySQLprovider.scheduleTask(new MySQLInitialCoreTask());
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
}
