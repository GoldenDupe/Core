package xyz.goldendupe.perks.action;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.perks.PetOwner;
import xyz.goldendupe.perks.gui.ActionsMenu;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface ActionManager {
    double random();

    void applyDamage(Player killer, Entity victim);
    void applyDeath(Player killer, Entity victim);

    int getActionTier(Player player, Action action);

    JavaPlugin getPlugin();

    PetOwner getPetOwner(UUID player);

    Set<Action> getKillActions();
    Set<Action> getDeathActions();
    Set<Action> getDamageActions();
    Set<Action> getMovementActions();

    ActionsMenu getMenu();

    @NotNull Messenger getMessenger();

    @Nullable
    PerkTier getPerkTier(Action action, int tier);

    int getPlayerPerkTier(Player player, Action action);
    void setPlayerPerkTier(@NotNull Player player, Action action, int i);

    Action getAction(@NotNull TranslationKey id);

    Collection<? extends Action> getActions();

    void loadPlayer(Player player);
    void unloadPlayer(Player player);

    boolean isMaxTier(Action action, int currentTier);

    int getPerkSlot(Player player, Action action);
    void setPerkSlot(Player player, Action action, int slot);

    ActionType getType(Action action);

    int getMaxTier(Action action);

    int getPlayerUsedPerkTier(Player player, Action action);

    Action getPlayerPerkSlot(Player player, ActionType actionType, int slot);
}
