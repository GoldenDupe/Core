package xyz.goldendupe.perks.action;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoRegister {
    ActionType value();
    PerkTier[] tiers() default {};
}
