/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;

import net.primegames.PrimesCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

public abstract class MySqlPostQueryTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            PreparedStatement statement = preparedStatement(PrimesCore.getInstance().getMySQLProvider().getConnection());
            int resultSet = statement.executeUpdate();
            if(resultSet == 0){
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try(ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if(generatedKeys.next()){
                    onInsert(generatedKeys.getInt(1));
                }
                else{
                    throw new SQLException("User registration failed because ID was not obtained");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    protected abstract PreparedStatement preparedStatement(Connection connection) throws SQLException;

    protected void onSuccess(int effectedRows){

    }

    protected void onInsert(int Id){}

}
