package xyz.goldendupe.perks.action.actions.pets;

import bet.astral.messenger.v2.translation.TranslationKey;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import xyz.goldendupe.perks.action.ActionManager;
import xyz.goldendupe.perks.action.Action;

public abstract class PetAction extends Action {
    public static final NamespacedKey OWNER = new NamespacedKey("combat-actions", "owner");
    public static final NamespacedKey SPAWNED = new NamespacedKey("combat-actions", "spawned");

    protected PetAction(ActionManager actionManager, TranslationKey name, TranslationKey description, TranslationKey descUpgr, TranslationKey descMax, ItemStack icon, TranslationKey... incompatibleEffects) {
        super(actionManager, name, description, descUpgr, descMax, icon, incompatibleEffects);
    }
}
