package net.primegames.components.vote.task;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import net.primegames.PrimeGames;
import net.primegames.components.vote.data.VoteTaskResponse;
import net.primegames.components.vote.data.VoteSite;
import net.primegames.utils.LoggerUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class CheckAllVoteTask implements Runnable {

    private final ArrayList<VoteSite> sites = new ArrayList<>();
    private final UUID uuid;
    private final String name;
    private final boolean sendResponse;

    public CheckAllVoteTask(HashMap<String, VoteSite> sites, Player player, boolean sendResponse) {
        this.uuid = player.getUniqueId();
        sites.forEach((key, value) -> this.sites.add(value));
        this.name = player.getName();
        this.sendResponse = sendResponse;
        LoggerUtils.debug("Checking votes for " + this.name + " on " + this.sites.size() + " sites.");
    }

    @Override
    public void run() {
        HashMap<VoteSite, String> responses = new HashMap<>();
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            for (VoteSite site : sites) {
                String checkUrl = site.getCheckUrlFor(uuid);
                if (checkUrl == null) {
                    LoggerUtils.debug("Player " + this.name + " logged out while checking vote sites.");
                    continue;
                }
                HttpUriRequest request = new HttpGet(checkUrl);
                request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9) Gecko/2008052906 Firefox/3.0");
                CloseableHttpResponse response = httpClient.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                responses.put(site, reader.readLine());
                LoggerUtils.debug("Response from " + site.getVote() + ": " + responses.get(site));
            }
            LoggerUtils.debug("Finished checking votes for " + this.name);
            handleResponses(responses);
            httpClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleResponses(HashMap<VoteSite, String> responses) {
        Bukkit.getScheduler().runTask(PrimeGames.getInstance().getPlugin(), () -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) {
                LoggerUtils.debug("player: " + name + " logged out while checking vote sites.");
                return;
            }
            if (responses.isEmpty()) {
                player.sendMessage("§cNo vote sites found.");
            }
            responses.forEach((site, response) -> {
                Gson gson = new Gson();
                VoteTaskResponse responseObject = gson.fromJson(response, VoteTaskResponse.class);
                if (responseObject.voted) {
                    if (!responseObject.claimed) {
                        LoggerUtils.info("claiming vote for " + name + " on " + site.getVote());
                        site.claimVote(player);
                        LoggerUtils.info("claimed vote for " + name + " on " + site.getVote());
                    } else if (sendResponse) {
                        player.sendMessage("§rYou have already claimed your vote on " + site.getVote() + ". Thank you for your support!");
                    }
                } else {
                    player.sendMessage("§cYou have not voted on " + site.getVote() + ".");
                }
            });
        });
    }
}
