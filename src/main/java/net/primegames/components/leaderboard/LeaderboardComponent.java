package net.primegames.components.leaderboard;

import lombok.Getter;
import lombok.NonNull;
import net.primegames.PrimeGames;
import net.primegames.components.Component;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardComponent implements Component {
    @Getter
    public static LeaderboardComponent instance;
    @Getter
    private final Map<String, Leaderboard> leaderboards = new HashMap<>();

    public LeaderboardComponent(@NonNull PrimeGames plugin){
        if (!Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            plugin.getLogger().severe("*** HolographicDisplays is not installed or not enabled. ***");
            plugin.getLogger().severe("*** Disabling Leaderboard Component. ***");
            return;
        }
        instance = this;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(PrimeGames.getInstance(), () -> leaderboards.forEach((name, lb) -> lb.scheduleUpdate()), 100, 1200);
    }
    @Override
    public @NonNull String getIdentifier() {
        return "LeaderboardComponent";
    }

    public void registerLeaderboard(Leaderboard leaderboard) {
        leaderboards.put(leaderboard.getName(), leaderboard);
    }

    public void getLeaderboard(String name) {
        leaderboards.get(name);
    }
}
