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
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Leaderboard {

    private final Hologram hologram;
    private final int rows;
    @Getter
    private String name;

    public Leaderboard(String name, Location location, int rows){
        hologram = HologramsAPI.createHologram(PrimeGames.getInstance().getPlugin(), location);
        hologram.insertTextLine(1, "[&b&l" + name + " &cLeaderboard$r]");
        this.rows = rows;
    }

    public Leaderboard(String name, Location location){
        hologram = HologramsAPI.createHologram(PrimeGames.getInstance().getPlugin(), location);
        hologram.insertTextLine(1, name);
        this.rows = 10;
    }

    public void updateValues(Map<@NonNull Integer, @NonNull String> values){
        updateValues(values, null);
    }

    public void updateValues(Map<@NonNull Integer, @NonNull String> values, @Nullable String suffix){
        AtomicInteger maxRows = new AtomicInteger(rows);
        AtomicInteger ranking = new AtomicInteger(1);
        Map<Integer, String> shortedValues = shortScores(values);
        shortedValues.forEach((value, name) -> {
            if (maxRows.get() <= 0) return;
            hologram.insertTextLine(ranking.get() + 1, getLbText(ranking.get(), name, value) + (suffix == null ? "" : suffix));
            ranking.getAndIncrement();
            maxRows.getAndDecrement();
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
