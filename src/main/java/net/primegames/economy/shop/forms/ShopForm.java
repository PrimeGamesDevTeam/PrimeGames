package net.primegames.economy.shop.forms;

import lombok.NonNull;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.shop.Shop;
import net.brcdev.shopgui.shop.ShopItem;
import net.ess3.api.IUser;
import net.kyori.adventure.text.format.TextFormat;
import net.milkbowl.vault.economy.Economy;
import net.primegames.PrimeGames;
import net.primegames.utils.LoggerUtils;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.geysermc.cumulus.CustomForm;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.response.CustomFormResponse;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import scala.Int;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ShopForm {

    public static void init(Player player, FloodgatePlayer fPlayer) {
        new ShopForm(player, fPlayer);
    }

    private final Player player;
    private final HashMap<String, Shop> shops = new HashMap<>();
    String[] shopIds = {"armor", "blocks", "drops", "dyes", "dyes", "farming", "food", "miscellaneous", "ores", "tools"};
    private final FloodgateApi api = FloodgateApi.getInstance();
    private final Economy economy;

    public ShopForm(Player player, FloodgatePlayer fPlayer){
        if (!player.getUniqueId().equals(fPlayer.getJavaUniqueId())) {
            throw new IllegalArgumentException("Player is not the same as FloodgatePlayer");
        }
        this.player = player;
        for (String id : shopIds) {
            Shop shop = ShopGuiPlusApi.getShop(id);
            if (shop != null) {
                shops.put(id, shop);
            } else {
                throw new IllegalArgumentException("Shop with id " + id + " does not exist");
            }
        }
        economy = PrimeGames.getInstance().getEconomy();
        openMainMenu();
    }

    private void openMainMenu(){
        SimpleForm.Builder form = SimpleForm.builder().title("Shop GUI");
        form.content("Welcome to the Shop GUI!\nYour Balance: " + ChatColor.RED + economy.getBalance(player));
        shops.forEach((id, shop) -> form.button(id));
        form.responseHandler((simpleForm, response) -> {
            SimpleFormResponse formResponse = simpleForm.parseResponse(response);
            ButtonComponent clickedButton = formResponse.getClickedButton();
            if (clickedButton == null) {
                return;
            }
            openShop(shops.get(clickedButton.getText()));
        });
        api.sendForm(player.getUniqueId(), form.build());
    }

    private void openShop(Shop shop){
        HashMap<String, Integer> nameIdMap = new HashMap<>();
        final int[] count = {1};
        SimpleForm.Builder form = SimpleForm.builder().title(shop.getId());
        form.content("Your Balance: " + ChatColor.RED + economy.getBalance(player));
        shop.getShopItems().forEach(item -> {
            String itemName = niceName(item.getItem());
            itemName = itemName + "\n" + ChatColor.RED + "Price: " + ChatColor.GREEN + item.getSellPrice() + " " + economy.currencyNamePlural();
            form.button(itemName);
            nameIdMap.put(itemName, count[0]);
            count[0]++;
        });
        form.responseHandler((simpleForm, response) -> {
            SimpleFormResponse formResponse = simpleForm.parseResponse(response);
            ButtonComponent clickedButton = formResponse.getClickedButton();
            if (clickedButton == null) {
                return;
            }
            ShopItem shopItem = shop.getShopItem(String.valueOf(nameIdMap.get(clickedButton.getText())));
            assert shopItem != null;
            openBuyForm(shopItem);
        });
        api.sendForm(player.getUniqueId(), form.build());
    }

    private void openBuyForm(@NonNull ShopItem item){
        CustomForm.Builder form = CustomForm.builder().title("Buy Item");
        form.slider(item.getSellPriceForAmount(1) + economy.currencyNamePlural() + "/" + niceName(item.getItem()), 1, item.getItem().getMaxStackSize(), 1);
        form.responseHandler((customForm, response) -> {
            CustomFormResponse formResponse = customForm.parseResponse(response);
            int amount = (int) formResponse.getSlider(0);
            purchase(item, amount);
        });
        api.sendForm(player.getUniqueId(), form.build());
    }


    private String niceName(ItemStack item){
        return item.getType().name().toLowerCase(Locale.ROOT).replace("_", " ");
    }

    private void purchase(@NonNull ShopItem item, int amount){
        double cost = item.getBuyPriceForAmount(amount);
        if (economy.getBalance(player) < cost) {
            player.sendMessage(ChatColor.GRAY + "You don't have enough money, you need " + ChatColor.RED + cost + ChatColor.GRAY + " to buy " + amount + " " + niceName(item.getItem()));
            return;
        }
        ItemStack itemStack = item.getItem().clone();
        itemStack.setAmount(amount);
        HashMap<Integer, ItemStack> remaining = player.getInventory().addItem(itemStack);
        if (!remaining.isEmpty()) {
            int count = 0;
            for (ItemStack stack : remaining.values()) {
                count += stack.getAmount();
            }
            if (count == itemStack.getAmount()){
                player.sendMessage(ChatColor.RED + "You don't have enough space in your inventory");
            } else {
                int addedCount = itemStack.getAmount() - count;
                cost = item.getBuyPriceForAmount(addedCount);
                economy.withdrawPlayer(player, cost);
                player.sendMessage(ChatColor.YELLOW + "You bought " + addedCount + " " + niceName(item.getItem()) + " for " + cost + economy.currencyNamePlural());
                LoggerUtils.info("Player " + player.getName() + " bought " + addedCount + " " + niceName(item.getItem()) + " for " + cost + economy.currencyNamePlural());
            }
        }else {
            economy.withdrawPlayer(player, item.getBuyPriceForAmount(amount));
            player.sendMessage(ChatColor.GREEN + "You bought " + amount + " " + niceName(item.getItem()) + " for " + cost);
            LoggerUtils.info(player.getName() + " bought " + amount + " " + niceName(item.getItem()) + " for " + cost + economy.currencyNamePlural());
        }
    }

}
