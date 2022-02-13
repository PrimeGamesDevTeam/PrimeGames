package net.primegames.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.primegames.PrimeGames;
import net.primegames.event.player.BedrockPlayerChatEvent;
import net.primegames.player.BedrockPlayer;
import net.primegames.player.BedrockPlayerManager;
import net.primegames.player.sentence.MuteSentence;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BedrockPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLogin(PlayerJoinEvent event) {
        PrimeGames.getInstance().getCorePlayerManager().initPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        BedrockPlayerManager.getInstance().remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncChatEvent event) {
        BedrockPlayer bedrockPlayer = BedrockPlayerManager.getInstance().getPlayer(event.getPlayer());
        Player player = event.getPlayer();
        if (bedrockPlayer != null) {
            //check if bedrock player is muted
            MuteSentence sentence = bedrockPlayer.isMuted();
            if (sentence != null){
                player.sendMessage("§cYou are muted for §e" + sentence.getTimeLeft());
                event.setCancelled(true);
                return;
            }
            BedrockPlayerChatEvent ev = new BedrockPlayerChatEvent(event.getPlayer(), bedrockPlayer, event.message().toString());
            ev.callEvent();
            if (ev.isCancelled()) {
                event.setCancelled(true);
            }
        }
    }
}
