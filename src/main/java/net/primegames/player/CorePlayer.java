/*
 *
 *  * Copyright (C) PrimeGames - All Rights Reserved
 *  * Unauthorized copying of this file, via any medium is strictly prohibited
 *  * Proprietary and confidential
 *
 */

package net.primegames.player;

import lombok.Data;
import net.primegames.groups.GroupTier;
import net.primegames.utils.LoggerUtils;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class CorePlayer {

    private final UUID originalUUID;
    private final UUID serverUUID;
    private final String username;
    private final long lastSession;
    private final int internalId;
    private final String lastIp;
    private final String currentIp;
    private final String continentCode;
    private final String countryCode;
    private final int reputation;
    private final int warnings;
    private final long timePlayed;
    private final long lastSessionDuration;
    private final Date registeredAt;
    private final int voteKeys;
    private final int legendaryKeys;
    private final int rareKeys;
    private final int commonKeys;
    private final PlayerStatus playerStatus = PlayerStatus.LOAD_PENDING;
    private final String locale;
    private ArrayList<@NonNull GroupTier> groupTiers = new ArrayList<>();

    public CorePlayer(int internalId, UUID originalUuid, UUID serverUuid, String username, String lastIp, String currentIp, String countryCode, String continentCode, int reputation, int warnings, long timePlayed, long lastSessionDuration, Date registeredAt, int voteKeys, int commonKeys, int rareKeys, int legendaryKeys, String locale) {
        this.originalUUID = originalUuid;
        this.serverUUID = serverUuid;
        this.lastSession = System.currentTimeMillis();
        this.internalId = internalId;
        this.username = username;
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
        this.currentIp = currentIp;
    }

    public void addGroupTier(GroupTier groupTier) {
        groupTiers.add(groupTier);
    }

    public void addGroups(List<Integer> groups) {
        for (int group : groups) {
            AddGroupTierFromId(group);
        }
    }

    public void AddGroupTierFromId(int id) {
        GroupTier groupTier = GroupTier.fromId(id);
        if (groupTier != null) {
            groupTiers.add(groupTier);
        } else {
            LoggerUtils.error("GroupTier with id " + id + " not found!");
        }
    }

    public void removeGroupTier(GroupTier groupTier) {
        groupTiers.remove(groupTier);
    }

    public Player getPlayer() {
        return org.bukkit.Bukkit.getPlayer(serverUUID);
    }

    public boolean isOnline() {
        return getPlayer() != null && getPlayer().isOnline();
    }

}
