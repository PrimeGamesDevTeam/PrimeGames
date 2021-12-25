package net.primegames.survival;

import net.primegames.core.PrimesCore;
import net.primegames.core.gamemode.GameMode;
import net.primegames.core.gamemode.GameModeId;
import net.primegames.survival.listener.SurvivalGroupListener;
import org.bukkit.plugin.PluginManager;

public class PrimesSurvival extends GameMode {

    public PrimesSurvival() {
        super(GameModeId.SURVIVAL);
    }

    @Override
    public void enable() {
        registerEvents();
    }

    @Override
    public void disable() {

    }

    public void registerEvents(){
        PluginManager manager = getPlugin().getServer().getPluginManager();
        manager.registerEvents(new SurvivalGroupListener(), PrimesCore.getInstance());
    }
}
