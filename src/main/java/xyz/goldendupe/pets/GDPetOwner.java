package xyz.goldendupe.pets;

import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.perks.PetOwner;

import java.util.List;
import java.util.UUID;

public class GDPetOwner implements PetOwner {
    private final UUID ownerId;

    public GDPetOwner(UUID ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public UUID getPetOwner() {
        return ownerId;
    }

    @Override
    public boolean isOwnerFriend(Player player) {
        return false;
    }

    @Override
    public void findTarget(@NotNull Mob entity) {
        Location location = entity.getLocation();
        List<Player> entities =
                location
                        .getNearbyEntitiesByType(Player.class, 10.5, 10.5, 10.5)
                        .stream()
                        .filter(p -> p.getUniqueId() != getPetOwner())
                        .toList();
        if (entities.isEmpty()) {
            return;
        }
        Player target = entities.get(GoldenDupe.RANDOM.nextInt(entities.size()));
        entity.setTarget(target);
    }
}
