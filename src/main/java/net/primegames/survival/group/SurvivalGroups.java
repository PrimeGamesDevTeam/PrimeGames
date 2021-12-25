package net.primegames.survival.group;

import lombok.Getter;
import net.primegames.core.groups.GroupTier;

public enum SurvivalGroups {

    OWNER("Owner", GroupTier.OWNER),
    CHIEF("Chief", GroupTier.CHIEF),
    LOS("Leader", GroupTier.LOS),
    ADMIN("admin", GroupTier.ADMIN),
    MODERATOR("moderator", GroupTier.MOD),
    HELPER("Helper", GroupTier.TRAINEE),
    VIP("VIP", GroupTier.TIER_2),
    VIP_P("VIP+", GroupTier.TIER_3),
    VIP_PP("VIP++", GroupTier.TIER_4),
    MVP("MVP", GroupTier.TIER_6),
    LEGEND("Legend", GroupTier.TIER_8),
    IMMORTAL("Immortal", GroupTier.TIER_9),
    VOTER("Voter", GroupTier.TIER_1),
    BUILDER("Builder", GroupTier.TIER_5);

    @Getter
    private final String name;
    @Getter
    private final GroupTier tier;

    SurvivalGroups(String name, GroupTier groupTier){
        this.tier = groupTier;
        this.name = name;
    }

    public static SurvivalGroups fromTierId(int id){
        SurvivalGroups[] groups = SurvivalGroups.values();
        for(SurvivalGroups group : groups){
            if(group.getTier().getId() == id){
                return group;
            }
        }
        return null;
    }
}
