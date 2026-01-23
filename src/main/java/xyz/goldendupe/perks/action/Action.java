package xyz.goldendupe.perks.action;

import bet.astral.messenger.v2.translation.TranslationKey;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public abstract class Action {
    protected final ActionManager actionManager;
    private final TranslationKey name;
    private final TranslationKey description;
    private final TranslationKey descriptionUpgradable;
    private final TranslationKey descriptionMax;
    private final ItemStack icon;
    private final TranslationKey[] incompatibleEffects;
    protected boolean isDeathEffect = true;

    protected Action(ActionManager actionManager, TranslationKey name, TranslationKey description, TranslationKey descriptionUpgradable, TranslationKey descriptionMax, ItemStack icon, TranslationKey... incompatibleEffects) {
        this.actionManager = actionManager;
        this.name = name;
        this.description = description;
        this.descriptionUpgradable = descriptionUpgradable;
        this.descriptionMax = descriptionMax;
        this.icon = icon;
        this.incompatibleEffects = incompatibleEffects != null ? incompatibleEffects : new TranslationKey[0];
    }

    public abstract void applyDeath(Player killer, Entity victim);
    public abstract void applyDamage(Player killer, Entity victim);
    public abstract boolean shouldApplyDeath(Player killer, Entity victim);
    public abstract boolean shouldApplyDamage(Player killer, Entity victim);
    public abstract void applyPassive(Player player);
    public abstract boolean shouldApplyPassive(Player player);
}
