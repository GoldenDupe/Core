package xyz.goldendupe.perks.action;

import lombok.Getter;

@Getter
public class PlayerPerkTier {
    private final Action action;
    private PerkTier perkTier;
    private int tier;

    public PlayerPerkTier(Action action, PerkTier tier) {
        this.action = action;
        this.perkTier = tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
        this.perkTier = action.getActionManager().getPerkTier(action, tier);
    }
}
