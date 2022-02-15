package net.primegames.components.vote.listener;

import com.vexsoftware.votifier.model.VotifierEvent;
import net.primegames.components.vote.VoteComponent;
import net.primegames.utils.LoggerUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VoteListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        VoteComponent.getInstance().checkVotes(event.getPlayer(), false);
    }


    @EventHandler
    public void onVote(VotifierEvent event){
        LoggerUtils.info("VOTE EVENT CALLED");
    }


}
