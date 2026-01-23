package xyz.goldendupe.perks.particles;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.fusionflare.models.FFModel;
import bet.astral.fusionflare.particles.FFParticle;
import org.bukkit.Location;

// @TODO
public class DeathEffect extends FFModel {
    public static FusionFlare globalFlare = null;
    public DeathEffect(FFParticle<?> particle, Location location, int timeBetweenTicks, int timeBetweenDraws) {
        super(globalFlare, particle, location, timeBetweenTicks, timeBetweenDraws);
    }

    @Override
    public void tick() {

    }

    @Override
    public void draw() {

    }
}
