package xyz.goldendupe.perks.action.actions.pets;

import bet.astral.messenger.v2.translation.TranslationKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.goldendupe.perks.action.ActionManager;

import java.util.concurrent.atomic.AtomicInteger;

//@AutoRegister(ActionType.KILL)
public class ZombieAction extends PetAction implements Listener {
    protected ZombieAction(ActionManager actionManager) {
        super(actionManager, TranslationKey.of("zombie"),
                TranslationKey.of("zombie"),
                TranslationKey.of("zombie"),
                TranslationKey.of("zombie"),
                ItemStack.of(Material.ZOMBIE_HEAD));
    }

    @Override
    public void applyDeath(Player killer, Entity victim) {
        int amount = (
                actionManager.random() > 0.7 ? 3 :
                        (actionManager.random() > 0.5 ? 2 : 1)
        );
        for (int i = 0; i < amount; i++) {
            Location location = victim.getLocation();
            location.add(actionManager.random(), 0.5, actionManager.random());
            victim.getLocation().getWorld()
                    .spawn(victim.getLocation(), Zombie.class,
                            CreatureSpawnEvent.SpawnReason.CUSTOM,
                            false,
                            zombie -> {
                                zombie.customName(
                                        killer.name().color(NamedTextColor.RED)
                                                .append(Component.text("'s zombie", NamedTextColor.YELLOW))
                                );
                                zombie.setHealth(
                                        zombie.getHealth() / 2
                                );
                                zombie.setCanBreakDoors(false);
                                zombie.setCanPickupItems(false);
                                zombie.getPersistentDataContainer().set(OWNER, PersistentDataType.STRING, killer.getName());
                                zombie.getPersistentDataContainer().set(SPAWNED, PersistentDataType.LONG, System.currentTimeMillis());
                                zombie.setPersistent(false);

                                AtomicInteger ticks = new AtomicInteger(0);

                                zombie.getScheduler().runAtFixedRate(actionManager.getPlugin(),
                                        t -> {
                                            ticks.addAndGet(5);
                                            if (ticks.get() > 600) {
                                                zombie.remove();
                                                return;
                                            }
                                            zombie.remove();
                                            if (killer.isValid()) {
                                                Location locKiller = killer.getLocation();
                                                Location locZombie = zombie.getLocation();
                                                if (locKiller.getWorld() != locZombie.getWorld()) {
                                                    zombie.remove();
                                                    return;
                                                }
                                                if (locKiller.distanceSquared(locZombie) > 20) {
                                                    zombie.teleport(locKiller);
                                                }
                                            }
                                        },
                                        null,
                                        5,
                                        5
                                );
                            }
                    );
        }
    }

    @Override
    public void applyDamage(Player killer, Entity victim) {

    }

    @Override
    public boolean shouldApplyDeath(Player killer, Entity victim) {
        if (actionManager.getActionTier(killer, this) <= 0) {
            return false;
        }
        int tier = actionManager.getActionTier(killer, this);
        double chance = 1.0 - (tier * 0.07);
        double rolled = actionManager.random();
        return chance > rolled;
    }

    @Override
    public boolean shouldApplyDamage(Player killer, Entity victim) {
        return false;
    }

    @Override
    public void applyPassive(Player player) {

    }

    @Override
    public boolean shouldApplyPassive(Player player) {
        return false;
    }
}