package net.primegames.components.vote.data;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import net.primegames.PrimeGames;
import net.primegames.components.vote.VoteComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class VoteParty {

    @Getter
    private static VoteParty instance;

    public static void init() {
        if (instance != null){
            return;
        }
        Location location = PrimeGames.plugin().getServerSettings().votePartHoloLocation();
        int partyIn = PrimeGames.plugin().getServerSettings().votePartyCount();
        instance = new VoteParty(location, partyIn);
    }

    private final Location location;
    @Getter
    private final int parytIn;
    private int currentVotes = 0;
    private Hologram hologram;

    private VoteParty(Location holoLocation, int partyIn){
        this.location = holoLocation;
        this.parytIn = partyIn;
        resetHoloGram();
    }

    private void resetHoloGram(){
        if (hologram != null){
            hologram.delete();
            hologram = null;
        }
        hologram = HologramsAPI.createHologram(PrimeGames.plugin(), location);
        hologram.appendTextLine("&e&lVote party!");
        hologram.appendTextLine(" ");
        hologram.appendTextLine("&fVoting for the server helps");
        hologram.appendTextLine("&fus grow. Plus, you get rewarded");
        hologram.appendTextLine("&fwith awesome items!");
        hologram.appendTextLine(" ");
        hologram.appendTextLine("&fCurrent votes: &e"+ currentVotes +"/"+ parytIn);
        //show progress bar
        StringBuilder progressChat = new StringBuilder();
        for (int i = 0; i < parytIn; i++){
            if (i < currentVotes){
                progressChat.append("&a∎∎");
            } else {
                progressChat.append("&7∎∎");
            }
        }
        hologram.appendTextLine(String.valueOf(progressChat));
        hologram.appendTextLine(" ");
        hologram.appendTextLine("&6Vote now!");
    }


    public void onVote(){
        currentVotes++;
        if (currentVotes >= parytIn){
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(" §c♫");
            Bukkit.broadcastMessage("  §b♫  §e♫  §fThe votes for the §6Vote Party §fhave been obtained!");
            Bukkit.broadcastMessage("§a♫   &c♫          §fThis will be started in §e10 seconds§f!");
            Bukkit.broadcastMessage("  §e♫ ");
            Bukkit.broadcastMessage(" ");
            Bukkit.getScheduler().scheduleSyncDelayedTask(PrimeGames.plugin(), () -> {
                PrimeGames.plugin().getServerSettings().onVoteParty();
                currentVotes = 0;
                Bukkit.broadcastMessage("'§6§lVote§e§lParty§r §8➠ §eYour §fVote Party §erewards have been applied!'");
            }, 200);
        }
        resetHoloGram();
    }


}
