package xyz.goldendupe.perks.action.actions.kill;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.perks.PerkTranslations;
import xyz.goldendupe.perks.action.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@AutoRegister(value = ActionType.KILL,
        tiers = {
                @PerkTier(
                        tier = 1, // Tier
                        chance = 1f, // Always = 1.0 (100%)
                        cost = 100
                )
}
)
public class HeadAction extends Action {
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyyy HH:mm:ss");
    protected HeadAction(ActionManager actionManager) {
        super(actionManager, PerkTranslations.PERK_KILL_HEAD_COLLECTOR_TITLE,
                PerkTranslations.PERK_KILL_HEAD_COLLECTOR_DESCRIPTION,
                PerkTranslations.PERK_KILL_HEAD_COLLECTOR_UPGRADABLE_DESCRIPTION,
                PerkTranslations.PERK_KILL_HEAD_COLLECTOR_MAX_DESCRIPTION,
                ItemStack.of(Material.PLAYER_HEAD));
    }

    @Override
    public void applyDeath(@NotNull Player killer, Entity victim) {
        ItemStack itemStack = null;
        if (!(victim instanceof Player player)) {
            if (victim instanceof Zombie) {
                itemStack = ItemStack.of(Material.ZOMBIE_HEAD);
            } else if (victim instanceof Skeleton) {
                itemStack = ItemStack.of(Material.SKELETON_SKULL);
            } else if (victim instanceof WitherSkeleton) {
                itemStack = ItemStack.of(Material.WITHER_SKELETON_SKULL);
            } else if (victim instanceof Piglin) {
                itemStack = ItemStack.of(Material.PIGLIN_HEAD);
            } else if (victim instanceof EnderDragon) {
                itemStack = ItemStack.of(Material.DRAGON_HEAD);
            }
        } else {
            itemStack = new ItemStack(Material.PLAYER_HEAD);
            itemStack.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(player.getPlayerProfile()));
            itemStack.setData(DataComponentTypes.CUSTOM_NAME, victim.name().color(NamedTextColor.WHITE).append(Component.text("'s head").color(NamedTextColor.YELLOW)));
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("Killed by ").color(NamedTextColor.GRAY)
                    .append(killer.name().color(NamedTextColor.YELLOW)).append(Component.text(" at ", NamedTextColor.GRAY))
                    .append(Component.text(dateFormat.format(Date.from(Instant.now()))).color(NamedTextColor.YELLOW))));
        }

        if (itemStack == null){
            return;
        }
        Location location = victim.getLocation();

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
