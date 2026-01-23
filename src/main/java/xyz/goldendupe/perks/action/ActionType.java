package xyz.goldendupe.perks.action;

public enum ActionType {
    KILL,
    DEATH,
    @Deprecated
    MOVEMENT,
    DAMAGE,

    /**
     * Always granted for the player. Does not require player to do anything for the perk to be active
     */
    PASSIVE,

    TRAIL_WALK,
    TRAIL_FLIGHT,
    TRAIL_ELYTRA,
}
