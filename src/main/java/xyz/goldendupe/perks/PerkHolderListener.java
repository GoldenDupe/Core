package xyz.goldendupe.perks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.goldendupe.perks.action.ActionManager;

public class PerkHolderListener implements Listener {
    private final ActionManager actionManager;
    public PerkHolderListener(ActionManager actionManager) {
        this.actionManager = actionManager;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        actionManager.loadPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        actionManager.unloadPlayer(player);
    }
}
