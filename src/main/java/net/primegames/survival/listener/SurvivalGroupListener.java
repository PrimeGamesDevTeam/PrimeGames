package net.primegames.survival.listener;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.primegames.core.PrimesCore;
import net.primegames.core.event.player.CoreGroupsLoadedEvent;
import net.primegames.core.utils.LoggerUtils;
import net.primegames.survival.group.SurvivalGroups;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


public class SurvivalGroupListener implements Listener {

    @EventHandler
    public void onGroupLoaded(CoreGroupsLoadedEvent event) {
        List<Integer> groupIds = event.getGroups();
        ArrayList<SurvivalGroups> survivalGroups = new ArrayList<>();
        Player player = event.getPlayer();
        PrimesCore.getInstance().getLogger().log(Level.INFO, ChatColor.YELLOW + "Loading survival groups for " + player.getName());
        for (Integer groupId : groupIds) {
            SurvivalGroups survivalGroup = SurvivalGroups.fromTierId(groupId);
            if (survivalGroup != null) {
                survivalGroups.add(survivalGroup);
            }else {
                PrimesCore.getInstance().getLogger().warning("Group " + groupId + " is not a survival group!");
            }
        }
        SurvivalGroups highestPriority = null;
        for (SurvivalGroups survivalGroup : survivalGroups) {
            if (highestPriority == null) {
                highestPriority = survivalGroup;
            }else {
                if (highestPriority.getTier().getPriority() < survivalGroup.getTier().getPriority()) {
                    highestPriority = survivalGroup;
                }
            }
        }
        if (highestPriority != null) {
            LuckPerms luckPerms = PrimesCore.getInstance().getLuckPerms();
            Group finalGroup = luckPerms.getGroupManager().getGroup(highestPriority.getName());
            if (finalGroup != null) {
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                if (user != null) {
                    user.data().clear();
                    InheritanceNode node = InheritanceNode.builder(finalGroup.getName()).value(true).build();
                    if (user.data().add(node) == DataMutateResult.SUCCESS) {
                        user.setPrimaryGroup(finalGroup.getName());
                        luckPerms.getUserManager().saveUser(user);
                        PrimesCore.getInstance().getLogger().log(Level.INFO, "Set " + player.getName() + "'s group to " + finalGroup.getName());
                    }else {
                        LoggerUtils.error("Failed to set " + player.getName() + "'s group to " + finalGroup.getName());
                    }
                }
            }
        }
    }
}
