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

import java.sql.Connection;

public class MySqlProvider {

    private final Connection connection;

    public MySqlProvider(){
        connection = (new MySqlConnectionBuilder()).getConnection();
    }

    public void scheduleTask(BukkitRunnable mySqlTask) {
        mySqlTask.runTaskAsynchronously(PrimesCore.getInstance());
    }

    public Connection getConnection(){
        return connection;
    }
}
