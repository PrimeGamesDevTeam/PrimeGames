package net.primegames.utils;

import org.bukkit.entity.Player;
import org.geysermc.floodgate.util.DeviceOs;

import java.util.ArrayList;

public abstract class BedrockPlayerCallback {

    private final ArrayList<DeviceOs> ignoreList = new ArrayList<>();

    public abstract void call(Player player);


    public void ignore(DeviceOs deviceOs) {
        ignoreList.add(deviceOs);
    }

    public boolean shouldIgnore(DeviceOs deviceOs) {
        return ignoreList.contains(deviceOs);
    }
}
