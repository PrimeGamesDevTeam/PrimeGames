/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.providor;

import net.primegames.JavaCore;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;

public class MySqlProvider {

    private final Connection connection;

    public MySqlProvider() {
        connection = MySqlConnectionBuilder.build(JavaCore.getInstance()).getConnection();
    }

    public void scheduleTask(BukkitRunnable mySqlTask) {
        mySqlTask.runTaskAsynchronously(JavaCore.getInstance().getPlugin());
    }

    public Connection getConnection() {
        return connection;
    }
}
