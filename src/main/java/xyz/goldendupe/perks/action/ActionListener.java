package xyz.goldendupe.perks.action;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class ActionListener implements Listener {
    private final ActionManager actionManager;
    public ActionListener(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent){
            if (damageEvent.getDamager() instanceof Player killer) {
                if (event.getEntity() == killer) {
                    // Cancel all self-inflicted kill actions
                    return;
                }
                actionManager.applyDeath(killer, event.getEntity());
            }
        }
    }

}
