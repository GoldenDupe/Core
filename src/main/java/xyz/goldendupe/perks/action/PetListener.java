package xyz.goldendupe.perks.action;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.perks.PetOwner;

import java.util.Objects;
import java.util.UUID;

import static xyz.goldendupe.perks.action.actions.pets.PetAction.OWNER;

public class PetListener implements Listener {
    private final ActionManager actionManager;

    public PetListener(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    @EventHandler
    public void onRemoveFromWorld(@NotNull EntityRemoveFromWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Zombie) {
            if (entity.getPersistentDataContainer().has(OWNER, PersistentDataType.STRING)) {
                ((Zombie) entity).setHealth(0);
                entity.remove();
            }
        }
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        player.getWorld().getNearbyEntities(player.getLocation(), 50, 50, 50)
                .forEach(entity -> {
                    if (entity.getPersistentDataContainer().has(OWNER, PersistentDataType.STRING)) {
                        if (entity.getPersistentDataContainer().get(OWNER, PersistentDataType.STRING).equals(player.getUniqueId().toString())) {
                            entity.remove();
                        }
                    }
                });
    }

    @EventHandler
    public void onEntityTargetChange(@NotNull EntityTargetLivingEntityEvent e) {
        if ((e.getEntity() instanceof Creature creature) && (!(e.getEntity() instanceof Player)) && (!(e.getEntity() instanceof Wolf))) {
            if (!(e.getTarget() instanceof Player target)) {
                return;
            }
            if (!creature.getPersistentDataContainer().has(OWNER, PersistentDataType.STRING)) {
                return;
            }
            e.setCancelled(true);
            UUID ownerId = UUID.fromString(
                    Objects.requireNonNull(
                            creature.getPersistentDataContainer().get(OWNER, PersistentDataType.STRING)));
            PetOwner petOwner = actionManager.getPetOwner(ownerId);
            if (petOwner == null) {
                e.getEntity().remove();
                return;
            }
            petOwner.findTarget(creature);
        }
    }
}