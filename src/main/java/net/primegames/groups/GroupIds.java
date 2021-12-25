package net.primegames.groups;

import lombok.Getter;

public enum GroupIds {

    OWNER(0, 20),
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

    GroupIds(int id, int priority){
        this.id = id;
        this.priority = priority;
    }
}
