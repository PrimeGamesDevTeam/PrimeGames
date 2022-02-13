/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;

import lombok.NonNull;
import net.primegames.PrimeGames;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MySqlFetchQueryTask extends ProviderRunnable {

    @Override
    public void run() {
        ResultSet rs = null;
        try {
            rs = preparedStatement(PrimeGames.getInstance().getMySQLprovider().getConnection()).executeQuery();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        if (rs != null) {
            ResultSet finalRs = rs;
            Bukkit.getScheduler().getMainThreadExecutor(PrimeGames.plugin()).execute(() -> {
                try {
                    handleResult(finalRs);
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
