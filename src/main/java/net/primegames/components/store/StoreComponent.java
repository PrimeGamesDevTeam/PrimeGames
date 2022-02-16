package net.primegames.components.store;

import lombok.Getter;
import lombok.NonNull;
import net.primegames.PrimeGames;
import net.primegames.components.Component;
import net.primegames.components.store.command.StoreCommand;
import net.primegames.components.store.listener.StoreListener;
import net.primegames.components.store.payment.Payment;
import net.primegames.components.store.payment.PaymentStore;
import net.primegames.components.store.task.PlayerPurchaseCheckTask;
import net.primegames.components.store.task.PlayerPurchaseCompleteTask;
import net.primegames.components.store.task.StoreTableInitialTask;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.plugin.PrimePlugin;
import net.primegames.providor.MySqlProvider;
import net.primegames.providor.MysqlCredentials;
import net.primegames.providor.connection.ConnectionId;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class StoreComponent implements Component {

    @Getter
    private static StoreComponent instance;

    @Getter
    private final MySqlProvider mySqlProvider;
    @Getter
    private final PrimePlugin plugin;
    @Getter
    private final PaymentStore store;

    public StoreComponent(PrimePlugin plugin) throws SQLException {
        this.plugin = plugin;
        this.mySqlProvider = PrimeGames.getInstance().getMySQLprovider();
        mySqlProvider.createConnection(ConnectionId.TEBEX, MysqlCredentials.getCredentials(plugin, "tebex"));
        mySqlProvider.scheduleTask(new StoreTableInitialTask(getConnection()));
        plugin.getServer().getPluginManager().registerEvents(new StoreListener(), plugin);
        plugin.getServer().getCommandMap().register("store", new StoreCommand());
        store = new PaymentStore();
        instance = this;
    }

    @Override
    public @NonNull String getIdentifier() {
        return "store.component";
    }

    public Connection getConnection() throws SQLException {
        return mySqlProvider.getConnection(ConnectionId.TEBEX);
    }

    public void sendMainForm(Player player){
        if (!BedrockPlayerManager.getInstance().isFloodGatePlayer(player)){
            player.sendMessage("§cThis feature is only available to bedrock players!");
            return;
        }
        FloodgateApi fd = FloodgateApi.getInstance();
        FloodgatePlayer floodgatePlayer = fd.getPlayer(player.getUniqueId());
        SimpleForm.Builder form = SimpleForm.builder().title("Store");
        Map<String, Payment> payments = StoreComponent.getInstance().getStore().getPayments(floodgatePlayer.getXuid());
        if (payments.size() != 0){
            form.content("§aYou have §c" + payments.size() + " §apayments\n§aTo view/claim your payments, click on the button below");
            payments.forEach((key, payment) -> {
                String name = payment.getPackage_name() + "\n§a" + payment.getTransaction_id();
                form.button(name);
                form.responseHandler(((simpleForm, s) -> {
                    SimpleFormResponse response = simpleForm.parseResponse(s);
                    if (Objects.requireNonNull(response.getClickedButton()).getText().equals(name)){
                        sendConformationForm(player, payment);
                    }
                }));
            });
        } else {
            String fetchButton = "§aFetch Payments";
            form.content("§aVisit our store at: §cstore.primegames.net §ato purchase ranks and keys");
            form.button(fetchButton);
            form.responseHandler(((simpleForm, s) -> {
                SimpleFormResponse response = simpleForm.parseResponse(s);
                if (Objects.requireNonNull(response.getClickedButton()).getText().equals(fetchButton)){
                    try {
                        StoreComponent.getInstance().getMySqlProvider().scheduleTask(new PlayerPurchaseCheckTask(floodgatePlayer, true));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        fd.sendForm(player.getUniqueId(), form.build());
    }

    //send payment claim confirm form to player
    public void sendConformationForm(Player player, Payment payment){
        String claimButton = "§aClaim payment";
        SimpleForm.Builder form = SimpleForm.builder().title("Payment Claim");
        form.content("§aYou have a payment of §c" + payment.getPackage_price() + " §afor §c" + payment.getPackage_name() + "\n\n§aTo claim this payment, click on the button below." +
                "\n\n§cNote: §aif you purchase this item for another server. Please do not claim here." +
                "Inventory Space Required: " + payment.getInventory() + " §a\n\n§eIf you have any issues please contact us on discord.io/primegames or support@primegames.net");
        form.button(claimButton);
        form.responseHandler(((simpleForm, s) -> {
            SimpleFormResponse response = simpleForm.parseResponse(s);
            if (Objects.requireNonNull(response.getClickedButton()).getText().equals(claimButton)){
                if (getFreeSlots(player.getInventory()) >= payment.getInventory()){
                    String command = payment.getCommand();
                    command = command.replace("%playername%", player.getName());
                    if (Bukkit.getCommandMap().dispatch(Bukkit.getConsoleSender(), command)){
                        Bukkit.broadcastMessage("§a" + player.getName() + " §ahas claimed a payment of §c" + payment.getPackage_price() + " §afor §c" + payment.getPackage_name() + "§a from store.primegames.net");
                        try {
                            String ip = payment.getIp();
                            InetSocketAddress inetSocketAddress  = player.getAddress();
                            if (inetSocketAddress != null){
                                ip = inetSocketAddress.getAddress().getHostAddress();
                            }
                            if (ip.isEmpty()){
                                ip = null;
                            }
                            StoreComponent.getInstance().getMySqlProvider().scheduleTask(new PlayerPurchaseCompleteTask(payment.getTransaction_id(), command, ip));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    player.sendMessage("§cYou do not have enough inventory space to claim this payment. Please free up some inventory space and try again.");
                }
            }
        }));
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form.build());
    }

    //get inventory empty slots
    private int getFreeSlots(Inventory inventory){
        int i = 0;
        for (ItemStack itemStack : inventory.getContents()){
            if (itemStack == null || itemStack.getType() == Material.AIR){
                i++;
            }
        }
        return i;
    }

}
