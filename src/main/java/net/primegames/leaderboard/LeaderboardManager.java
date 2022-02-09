package net.primegames.leaderboard;

import lombok.Getter;
import net.primegames.PrimeGames;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardManager {

    @Getter
    private static LeaderboardManager instance;
    @Getter
    private final Map<String, Leaderboard> leaderboards = new HashMap<>();
    private LeaderboardManager() {
        instance = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PrimeGames.getInstance().getPlugin(), () -> leaderboards.forEach((name, lb) -> lb.scheduleUpdate()), 100, 1200);
    }

    public static void init() {
        new LeaderboardManager();
    }

    public void registerLeaderboard(Leaderboard leaderboard) {
        leaderboards.put(leaderboard.getName(), leaderboard);
    }

    public void getLeaderboard(String name) {
        leaderboards.get(name);
    }

}
