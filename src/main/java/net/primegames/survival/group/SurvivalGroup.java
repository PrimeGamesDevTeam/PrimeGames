package net.primegames.survival.group;

import lombok.Getter;
import net.luckperms.api.model.group.Group;
import net.primegames.core.PrimesCore;
import net.primegames.core.groups.GroupTier;

import java.util.ArrayList;

public enum SurvivalGroup {

    OWNER("Owner", GroupTier.OWNER),
    CHIEF("Chief", GroupTier.CHIEF),
    LOS("Leader", GroupTier.LOS),
    ADMIN("admin", GroupTier.ADMIN),
    MODERATOR("moderator", GroupTier.MOD),
    HELPER("Helper", GroupTier.TRAINEE),
    VOTER("Voter", GroupTier.TIER_1),
    VIP("VIP", GroupTier.TIER_2),
    VIP_P("VIP+", GroupTier.TIER_3),
    VIP_PP("VIP++", GroupTier.TIER_4),
    BUILDER("Builder", GroupTier.TIER_5),
    MVP("MVP", GroupTier.TIER_6),
    LEGEND("Legend", GroupTier.TIER_8),
    IMMORTAL("Immortal", GroupTier.TIER_9);

    @Getter
    private final String name;
    @Getter
    private final GroupTier tier;

    SurvivalGroup(String name, GroupTier groupTier){
        this.tier = groupTier;
        this.name = name;
    }

    public static SurvivalGroup fromTier(GroupTier groupTier){
        SurvivalGroup[] groups = SurvivalGroup.values();
        for(SurvivalGroup group : groups){
            if(group.getTier().equals(groupTier)){
                return group;
            }
        }
        return null;
    }

    public Group getGroup(){
        return PrimesCore.getInstance().getLuckPerms().getGroupManager().getGroup(this.getName());
    }

    public static SurvivalGroup fromTierId(int id){
        SurvivalGroup[] groups = SurvivalGroup.values();
        for(SurvivalGroup group : groups){
            if(group.getTier().getId() == id){
                return group;
            }
        }
        return null;
    }

    public static SurvivalGroup getHighestPriority(ArrayList<SurvivalGroup> groups){
        SurvivalGroup highestPriority = null;
        for (SurvivalGroup survivalGroup : groups) {
            if (highestPriority == null) {
                highestPriority = survivalGroup;
            }else {
                if (highestPriority.getTier().getPriority() < survivalGroup.getTier().getPriority()) {
                    highestPriority = survivalGroup;
                }
            }
        }
        return highestPriority;
    }

}
