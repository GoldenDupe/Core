package xyz.goldendupe.perks.action.actions.kill;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.perks.PerkTranslations;
import xyz.goldendupe.perks.action.*;

@AutoRegister(value = ActionType.KILL,
        tiers = {
                @PerkTier(
                        tier = 1, // Tier
                        chance = 1, // Always = 1.0 (100%)
                        strength = 1, // Tier
                        length = 2.5f, // Length seconds
                        cost = 500
                ),
                @PerkTier(
                        tier = 2,
                        chance = 1,
                        strength = 2,
                        length = 3.5f,
                        cost = 1000
                ),
                @PerkTier(
                        tier = 3,
                        chance = 1,
                        strength = 3,
                        length = 3.5f,
                        cost = 1500
        ),
        }
)
public class SpeedAction extends Action {
    protected SpeedAction(ActionManager actionManager) {
        super(actionManager, PerkTranslations.PERK_KILL_SPEED_TITLE,
                PerkTranslations.PERK_KILL_SPEED_DESCRIPTION,
                PerkTranslations.PERK_KILL_SPEED_UPGRADABLE_DESCRIPTION,
                PerkTranslations.PERK_KILL_SPEED_MAX_DESCRIPTION,
                ItemStack.of(Material.SUGAR));
    }

    @Override
    public void applyDeath(@NotNull Player killer, Entity victim) {
        int playerPerkTier = actionManager.getPlayerPerkTier(killer, this);
        PerkTier tier = actionManager.getPerkTier(this, playerPerkTier);

        killer.addPotionEffect(new PotionEffect(
                PotionEffectType.SPEED,
                (int) (tier.length()*20),
                (int) (tier.strength()-1)
        ));
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
