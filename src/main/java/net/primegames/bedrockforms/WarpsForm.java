package net.primegames.bedrockforms;

import com.earth2me.essentials.Warps;
import com.earth2me.essentials.commands.WarpNotFoundException;
import net.ess3.api.InvalidWorldException;
import net.primegames.PrimeGames;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.SimpleForm;
import org.geysermc.cumulus.component.ButtonComponent;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;

public class WarpsForm {

    private final Player player;

    public static void init(Player player){
        new WarpsForm(player);
    }

    private WarpsForm(Player player){
        if (!FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())){
            throw new IllegalArgumentException("Player is not a floodgate player");
        }
        this.player = player;
        send();
    }

    private void send(){
        Warps warps = PrimeGames.getInstance().getEssentials().getWarps();
        SimpleForm.Builder form = SimpleForm.builder().title("Warps");
        for (String warp : warps.getList()){
            form.button(warp);
        }
        form.responseHandler((simpleForm, s) -> {
            SimpleFormResponse response = simpleForm.parseResponse(s);
            ButtonComponent clicked = response.getClickedButton();
            if (clicked != null){
                try {
                    Location location = warps.getWarp(clicked.getText());
                    player.teleport(location);
                } catch (WarpNotFoundException | InvalidWorldException e) {
                    e.printStackTrace();
                }
            } else {
                player.sendMessage("&cSelected warp is invalid, report this to a staff member");
            }
        });
        FloodgateApi.getInstance().sendForm(player.getUniqueId(), form.build());
    }

}
