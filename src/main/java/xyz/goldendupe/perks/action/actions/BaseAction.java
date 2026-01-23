package xyz.goldendupe.perks.action.actions;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.perks.action.*;

/*
@AutoRegister(value = ActionType.KILL,
        tiers = {
                @PerkTier(
                        tier = 1, // Tier
                        chance = 1f, // Always = 1.0 (100%)
                        cost = 100
                )
}
)

 */
public class BaseAction extends Action {
    protected BaseAction(ActionManager actionManager) {
        super(actionManager, null,
                null,
                null,
                null,
                ItemStack.of(Material.DIRT));
    }

    @Override
    public void applyDeath(@NotNull Player killer, Entity victim) {
    }

    @Override
    public void applyDamage(Player killer, Entity victim) {

    }

    @Override
    public boolean shouldApplyDeath(Player killer, Entity victim) {
        int playerPerkTier = getActionManager().getPlayerPerkTier(killer, this);
        if (playerPerkTier <= 0) {
            return false;
        }
        PerkTier perkTier = actionManager.getPerkTier(this, playerPerkTier);
        return getActionManager().random() < perkTier.chance();
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
