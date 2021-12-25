/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.core.player;

import lombok.Data;

import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

@Data
public class CorePlayerData {

    private Player player;
    private UUID uuid;
    private String username;
    private long lastSession;
    private int internalId;
    private String lastIp;
    private String currentIp;
    private String continentCode;
    private String countryCode;
    private int reputation;
    private int warnings;
    private long timePlayed;
    private long lastSessionDuration;
    private Date registeredAt;
    private int voteKeys;
    private int legendaryKeys;
    private int rareKeys;
    private int commonKeys;
    private PlayerStatus playerStatus = PlayerStatus.LOAD_PENDING;
    private String locale;

    public CorePlayerData(Player player, int internalId, UUID uuid, String lastIp, String countryCode, String continentCode, int reputation, int warnings, long timePlayed, long lastSessionDuration, Date registeredAt, int voteKeys, int commonKeys, int rareKeys, int legendaryKeys, String locale){
        this.player = player;
        this.username = player.getName();
        this.uuid = uuid;
        this.lastSession = System.currentTimeMillis();
        this.internalId = internalId;
        this.lastIp = lastIp;
        this.continentCode = continentCode;
        this.reputation = reputation;
        this.warnings = warnings;
        this.timePlayed = timePlayed;
        this.lastSessionDuration = lastSessionDuration;
        this.registeredAt = registeredAt;
        this.voteKeys = voteKeys;
        this.commonKeys = commonKeys;
        this.rareKeys = rareKeys;
        this.legendaryKeys = legendaryKeys;
        this.locale = locale;
        this.countryCode = countryCode;
        if (player.getAddress() != null){
            this.currentIp = player.getAddress().getHostString();
        }else {
            this.currentIp = lastIp;
        }
    }

    public boolean isOnline(){
        return player.isOnline();
    }
}
