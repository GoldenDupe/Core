package xyz.goldendupe.perks.action.actions.kill;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.perks.PerkTranslations;
import xyz.goldendupe.perks.action.*;

@AutoRegister(value = ActionType.KILL,
        tiers = {
                @PerkTier(
                        tier = 1, // Tier
                        chance = 1f, // Always = 1.0 (100%)
                        strength = 1, // Tier
                        extraValue = 4,
                        cost = 250
                ),
                @PerkTier(
                        tier = 2,
                        chance = 1f,
                        strength = 3,
                        extraValue = 7,
                        cost = 500
                ),
                @PerkTier(
                        tier = 3,
                        chance = 1,
                        strength = 7,
                        extraValue = 8,
                        cost = 750
                ),
        }
)
public class RandomItemAction extends Action {
    protected RandomItemAction(ActionManager actionManager) {
        super(actionManager, PerkTranslations.PERK_KILL_RANDOM_ITEMS_TITLE,
                PerkTranslations.PERK_KILL_RANDOM_ITEMS_DESCRIPTION,
                PerkTranslations.PERK_KILL_RANDOM_ITEMS_UPGRADABLE_DESCRIPTION,
                PerkTranslations.PERK_KILL_RANDOM_ITEMS_MAX_DESCRIPTION,
                ItemStack.of(Material.GRASS_BLOCK));
    }

    @Override
    public void applyDeath(@NotNull Player killer, Entity victim) {
        int playerPerkTier = actionManager.getPlayerPerkTier(killer, this);
        PerkTier tier = actionManager.getPerkTier(this, playerPerkTier);
        Location location = victim.getLocation();

        int min = (int) tier.strength();
        int max = (int) tier.extraValue();
        int times = GoldenDupe.RANDOM.nextInt(min, max);
        for (int i = 0; i < times; i++) {
            ItemStack itemStack =GoldenDupe.instance().generateRandomItem(killer);
            double x = actionManager.random();
            double y = actionManager.random();
            double z = actionManager.random();
            location.add(
                    x,
                    y,
                    z
            );
            location.getWorld().dropItemNaturally(location, itemStack);
            location.add(
                    -x,
                    -y,
                    -z
            );
        }
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
