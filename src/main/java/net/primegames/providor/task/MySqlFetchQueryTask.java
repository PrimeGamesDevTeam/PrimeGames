/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor.task;

import lombok.NonNull;
import net.primegames.PrimeGames;
import net.primegames.plugin.PrimePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class MySqlFetchQueryTask extends ProviderRunnable {

    private final String pluginName;
    private final Connection connection;

    public MySqlFetchQueryTask(Plugin plugin, Connection connection) {
        this.pluginName = plugin.getName();
        this.connection = connection;
    }

    @Override
    public void run() {
        ResultSet rs = null;
        try {
            rs = preparedStatement(connection).executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        if (rs != null) {
            ResultSet finalRs = rs;
            Bukkit.getScheduler().getMainThreadExecutor(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin(pluginName))).execute(() -> {
                try {
                    handleResult(finalRs);
                    Bukkit.getScheduler().cancelTask(getTaskId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } else {
            throw new RuntimeException("Could not load player data from Database since resultSet returned null");
        }
    }


    protected abstract @NonNull PreparedStatement preparedStatement(Connection connection) throws SQLException;

    protected abstract void handleResult(ResultSet resultSet) throws SQLException;

}
