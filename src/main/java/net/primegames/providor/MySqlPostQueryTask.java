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

public abstract class MySqlPostQueryTask extends ProviderRunnable {

    @Override
    public void run() {
        try {
            PreparedStatement statement = preparedStatement(PrimeGames.getInstance().getMySQLprovider().getConnection());
            int resultSet = statement.executeUpdate();
            if (resultSet == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            Bukkit.getScheduler().getMainThreadExecutor(PrimeGames.plugin()).execute(() -> {
                int effectRows = 0;
                while (true) {
                    try {
                        if (!generatedKeys.next()) break;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        effectRows++;
                        onInsert(generatedKeys.getInt(1));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                onSuccess(effectRows);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    protected abstract @NonNull PreparedStatement preparedStatement(Connection connection) throws SQLException;

    protected void onSuccess(int effectedRows) {

    }

    protected void onInsert(int Id) {
    }

}
