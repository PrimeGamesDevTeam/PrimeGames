package net.primegames.components.store.task;

import net.primegames.providor.task.MySqlTask;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StoreTableInitialTask extends MySqlTask {

    public StoreTableInitialTask(Connection connection) {
        super(connection);
    }

    @Override
    protected void doOperations(Statement statement) throws SQLException {
        statement.execute("CREATE TABLE IF NOT EXISTS `payments` ( `id` INT NOT NULL AUTO_INCREMENT, `user_id` VARCHAR(20), `transection_id` VARCHAR(25), `username` VARCHAR(20), `email` VARCHAR(50), `date` VARCHAR(50), `time` VARCHAR(50), `package_name` VARCHAR(60), `package_id` VARCHAR(20), `package_price` VARCHAR(20), `ip` VARCHAR(20),\n" +
                "`claim_ip` INT(20) DEFAULT NULL, `claimed` BOOLEAN DEFAULT FALSE, `command` TEXT, `inventory` TINYINT unsigned DEFAULT 0, `status` VARCHAR(20) DEFAULT 'complete', PRIMARY KEY (`id`) ) ENGINE=InnoDB;");
    }
}
