package net.primegames.components.store.task;

import lombok.NonNull;
import net.primegames.components.store.StoreComponent;
import net.primegames.components.store.payment.Payment;
import net.primegames.player.BedrockPlayer;
import net.primegames.providor.task.MySqlFetchQueryTask;
import net.primegames.utils.LoggerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PlayerPurchaseCheckTask extends MySqlFetchQueryTask {

    private final String xuid;
    private final UUID uuid;
    private boolean sendForm = false;
    private String  username;

    public PlayerPurchaseCheckTask(BedrockPlayer player) throws SQLException {
        super(StoreComponent.getInstance().getPlugin(), StoreComponent.getInstance().getConnection());
        this.xuid = player.getFloodgatePlayer().getXuid();
        this.uuid = player.getServerUUID();
        this.username = player.getUsername();
        LoggerUtils.info("Fetching purchases for " + player.getUsername() + " (" + player.getFloodgatePlayer().getXuid() + ")");
    }

    public PlayerPurchaseCheckTask(FloodgatePlayer player, boolean sendForm) throws SQLException {
        super(StoreComponent.getInstance().getPlugin(), StoreComponent.getInstance().getConnection());
        this.xuid = player.getXuid();
        this.uuid = player.getJavaUniqueId();
        this.sendForm = sendForm;
    }

    @Override
    protected @NonNull PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `payments` WHERE `user_id` = ? AND `claimed` = 0");
        preparedStatement.setString(1, xuid);
        return preparedStatement;
    }

    @Override
    protected void handleResult(ResultSet resultSet) throws SQLException {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            boolean hasPurchased = false;
            while(resultSet.next()) {
                hasPurchased = true;
                Payment payment = Payment.Builder.create()
                        .id(resultSet.getInt("id"))
                        .transaction_id(resultSet.getString("transaction_id"))
                        .xuid(resultSet.getString("user_id"))
                        .username(resultSet.getString("username"))
                        .email(resultSet.getString("email"))
                        .date(resultSet.getString("date"))
                        .time(resultSet.getString("time"))
                        .ip(resultSet.getString("ip"))
                        .package_name(resultSet.getString("package_name"))
                        .package_price(resultSet.getString("package_price"))
                        .command(resultSet.getString("command"))
                        .inventory(resultSet.getInt("inventory"))
                        .status(resultSet.getString("status"))
                        .build();
                StoreComponent.getInstance().getStore().addPayment(payment);
            }
            if (hasPurchased){
                if (sendForm) {
                    StoreComponent.getInstance().sendMainForm(player);
                } else {
                    player.sendMessage("§5§l[STORE]§r§aYou have purchased one or more packages from store! use §e/store §ato see and claim them!");
                }
            } else {
                if (sendForm){
                    player.sendMessage("No purchases were found for you! Use §e/store §afor more info");
                }
            }
        } else {
            LoggerUtils.info(username  + " went offline before we could check for purchases!");
        }
    }
}
