package net.primegames.leaderboard;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import lombok.NonNull;
import net.primegames.PrimeGames;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public abstract class Leaderboard {

    private final Hologram hologram;
    private final int rows;
    @Getter
    private String name;

    public Leaderboard(String name, Location location, int rows){
        hologram = HologramsAPI.createHologram(PrimeGames.getInstance().getPlugin(), location);
        hologram.insertTextLine(1, name);
        this.rows = rows;
    }

    public Leaderboard(String name, Location location){
        hologram = HologramsAPI.createHologram(PrimeGames.getInstance().getPlugin(), location);
        hologram.insertTextLine(1, name);
        this.rows = 10;
    }


    public void updateValues(Map<@NonNull Integer, @NonNull String> values, @Nullable String suffix){
        int maxRows = rows;
        int ranking = 1;
        Map<Integer, String> shortedValues = shortScores(values);
        shortedValues.forEach((value, name) -> {
            if (maxRows <= 0) return;
            hologram.insertTextLine(ranking + 1, getLbText(ranking, name, value) + (suffix == null ? "" : suffix));
        });
    }

    private String getLbText(int ranking, String name, int value){
        return "§7" + ranking + ". §e" + name + " §7- §a" + value;
    }

    protected Map<Integer, String> shortScores(Map<Integer, String> scores){
        return new TreeMap<>(scores);
    }


    public abstract void scheduleUpdate();

}
