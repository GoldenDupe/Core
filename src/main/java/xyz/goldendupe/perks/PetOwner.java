package xyz.goldendupe.perks;

import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PetOwner {
    UUID getPetOwner();
    boolean isOwnerFriend(Player player);

    void findTarget(@NotNull Mob entity);
}
