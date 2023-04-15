package net.splurpy.paranoiamod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class LithiumFlameParticles extends RisingParticle {
    protected LithiumFlameParticles(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xSpd, double ySpd, double zSpd) {
        super(level, xCoord, yCoord, zCoord, xSpd, ySpd, zSpd);

        this.friction = 0.8f;
        this.xd = xSpd;
        this.yd = ySpd;
        this.zd = zSpd;
        this.quadSize *= 1.0f;
        this.lifetime = 40;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;
    }

    @Override
    public void tick() {
        super.tick();
        this.quadSize = shrink();
    }

    private float shrink() {
        float f = ((float)this.age + 1) / (float)this.lifetime;
        return this.quadSize * (1.0F - f * f * 0.5F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z, double xSpd, double ySpd, double zSpd) {
            return new LithiumFlameParticles(level, x, y, z, this.sprites, xSpd, ySpd, zSpd);
        }
    }
}
