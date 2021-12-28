package net.primegames.core.groups;

import lombok.Getter;

public enum GroupTier {

    OWNER(1, 20),
    CHIEF(19, 18),
    LOS(2, 17),
    DEV(3, 16),
    COMMUNITY_MANAGER(4, 15),
    ADMIN(5, 14),
    MOD(6, 13),
    TRAINEE(7, 12),
    HEAD_BUILDER(8, 11),
    BUILDER(9, 10),
    TIER_0(0, 0), //Mortal
    TIER_1(18, 1), //Voter
    TIER_2(16, 2), //Ares
    TIER_3(15, 3), //Iris
    TIER_4(13, 4), //Poseidon
    TIER_5(10, 5), //STREAMER
    TIER_6(14, 6), //Hades
    TIER_7(20, 7), //Artist
    TIER_8(12, 8), //Zeus
    TIER_9(11, 9); //Titan

    @Getter
    private final int id;
    @Getter
    private final int priority;
    @Getter
    private final boolean staff = false;

    GroupTier(int id, int priority) {
        this.id = id;
        this.priority = priority;
    }

    GroupTier(int id, int priority, boolean staff) {
        this.id = id;
        this.priority = priority;

    }

    public static GroupTier fromId(int id) {
        for (GroupTier tier : GroupTier.values()) {
            if (tier.getId() == id) {
                return tier;
            }
        }
        return null;
    }

    public static GroupTier getByPriority(int priority) {
        for (GroupTier tier : GroupTier.values()) {
            if (tier.getPriority() == priority) {
                return tier;
            }
        }
        return null;
    }

    public static GroupTier getHighestTierPlayer() {
        GroupTier highest = GroupTier.TIER_0;
        for (GroupTier tier : GroupTier.values()) {
            if (tier.isStaff()) continue;
            if (tier.getPriority() > highest.getPriority()) {
                highest = tier;
            }
        }
        return highest;
    }

}

