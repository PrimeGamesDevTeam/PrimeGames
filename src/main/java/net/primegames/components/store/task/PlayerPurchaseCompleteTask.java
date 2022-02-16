package net.primegames.components.store.task;

import lombok.NonNull;
import net.primegames.components.store.StoreComponent;
import net.primegames.providor.task.MySqlPostQueryTask;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlayerPurchaseCompleteTask extends MySqlPostQueryTask {

    private final String transactionId;
    private final String executedCommand;
    private final String claimed_ip;

    public PlayerPurchaseCompleteTask(String transactionId, String executedCommand, String claim_ip) throws SQLException {
        super(StoreComponent.getInstance().getConnection(), StoreComponent.getInstance().getPlugin());
        this.transactionId = transactionId;
        this.executedCommand = executedCommand;
        this.claimed_ip = claim_ip;
    }

    @Override
    protected @NonNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `payments` SET `claim_ip` = ?, `command` = ?, claimed = 1 WHERE `transaction_id` = ?");
        preparedStatement.setString(1, claimed_ip);
        preparedStatement.setString(2, executedCommand);
        preparedStatement.setString(3, transactionId);
        return preparedStatement;
    }
}
