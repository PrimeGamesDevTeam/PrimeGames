package net.primegames;

import com.earth2me.essentials.Essentials;
import lombok.Getter;

import net.milkbowl.vault.economy.Economy;
import net.primegames.listener.BedrockPlayerCommandListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public final class PrimeGames extends JavaPlugin {

    @Getter
    private static PrimeGames instance;
    @Getter
    private Economy economy;
    @Getter
    private Essentials essentials;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!setupEssentials()) {
            getLogger().severe("Essentials not found! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        registerCoreListeners();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    private boolean setupEssentials(){
        if (getServer().getPluginManager().getPlugin("Essentials") == null) {
            return false;
        }
        essentials = (Essentials) getServer().getPluginManager().getPlugin("Essentials");
        return true;
    }

    private void registerCoreListeners() {
        @NotNull PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BedrockPlayerCommandListener(), this);
    }

}
