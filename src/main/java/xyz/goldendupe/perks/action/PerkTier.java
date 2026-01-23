package xyz.goldendupe.perks.action;

public @interface PerkTier {
    int tier() default 1;
    float chance() default 1.0f;
    float cost() default 0.0f;
    float length() default 0.0f;
    float strength() default 0.0f;
    float extraValue() default 1;
    String value() default "";
}
