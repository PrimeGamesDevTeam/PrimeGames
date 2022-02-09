package net.primegames.components.vote.listener;

import net.primegames.components.vote.VoteComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class VoteListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        VoteComponent.getInstance().checkVotes(event.getPlayer(), false);
    }

}
