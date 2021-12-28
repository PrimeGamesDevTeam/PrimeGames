package net.primegames.survival.listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.primegames.core.PrimesCore;
import net.primegames.core.event.player.CorePlayerLoadedEvent;
import net.primegames.core.groups.GroupTier;
import net.primegames.core.utils.LoggerUtils;
import net.primegames.survival.group.SurvivalGroup;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;


public class SurvivalGroupListener implements Listener {

    @EventHandler
    public void onGroupLoaded(CorePlayerLoadedEvent event) {
        List<GroupTier> groupTiers = event.getPlayerData().getGroupTiers();
        ArrayList<SurvivalGroup> survivalGroups = new ArrayList<>();
        Player player = event.getPlayer();
        LoggerUtils.info(ChatColor.YELLOW + "Loading survival groups for " + player.getName());
        for (GroupTier groupId : groupTiers) {
            SurvivalGroup survivalGroup = SurvivalGroup.fromTier(groupId);
            if (survivalGroup != null) {
                survivalGroups.add(survivalGroup);
            } else {
                LoggerUtils.error("Group " + groupId + " is not a survival group!");
            }
        }
        SurvivalGroup highestPriority = SurvivalGroup.getHighestPriority(survivalGroups);
        if (highestPriority != null) {
            LuckPerms luckPerms = PrimesCore.getInstance().getLuckPerms();
            Group finalGroup = highestPriority.getGroup();
            if (finalGroup != null) {
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                if (user != null) {
                    user.data().clear();
                    InheritanceNode node = InheritanceNode.builder(finalGroup.getName()).value(true).build();
                    if (user.data().add(node) == DataMutateResult.SUCCESS) {
                        user.setPrimaryGroup(finalGroup.getName());
                        luckPerms.getUserManager().saveUser(user);
                        LoggerUtils.info("Set " + player.getName() + "'s group to " + finalGroup.getName());
                    } else {
                        LoggerUtils.error("Failed to set " + player.getName() + "'s group to " + finalGroup.getName());
                    }
                }
            }
        }
    }
}
