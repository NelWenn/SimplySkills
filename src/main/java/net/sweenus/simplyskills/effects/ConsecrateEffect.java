package net.sweenus.simplyskills.effects;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Box;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.client.particle.SpellHitParticle;
import net.spell_engine.particle.Particles;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.SpellAttributes;
import net.sweenus.simplyskills.SimplySkills;
import net.sweenus.simplyskills.abilities.compat.SimplySwordsGemEffects;
import net.sweenus.simplyskills.registry.SoundRegistry;
import net.sweenus.simplyskills.util.HelperMethods;

public class ConsecrateEffect extends StatusEffect {
    public ConsecrateEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }


    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.getWorld().isClient()) {

            if (livingEntity.isOnGround() && (livingEntity instanceof PlayerEntity player)) {

                int radius = SimplySkills.berserkerConfig.signatureBerserkerBullrushRadius;
                double damageMultiplier = SimplySkills.berserkerConfig.signatureBerserkerBullrushDamageModifier;
                int hitFrequency = 18; //SimplySkills.berserkerConfig.signatureBerserkerBullrushHitFrequency;
                double damage = (player.getAttributeValue(SpellAttributes.POWER.get(MagicSchool.HEALING).attribute) * damageMultiplier);

                Box box = HelperMethods.createBox(player, radius * 2);
                if (player.age % hitFrequency == 0) {
                    for (Entity entities : livingEntity.getWorld().getOtherEntities(livingEntity, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, player)) {
                                //le.setVelocity((player.getX() - le.getX()) /4,  (player.getY() - le.getY()) /4, (player.getZ() - le.getZ()) /4);
                                le.damage(player.getDamageSources().magic(), (float) damage);
                                le.timeUntilRegen = 1;
                            }
                        }
                    }
                }
                if (player.age % hitFrequency == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_ascend.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.4, 0);
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.2, 0);
                    player.getWorld().playSoundFromEntity(null, player, SoundRegistry.SOUNDEFFECT25,
                            SoundCategory.PLAYERS, 0.05f, 0.8f);
                }
                if (player.age % hitFrequency-10 == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_spell.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.2, 0);
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.3, 0);
                }
                if (player.age % hitFrequency-5 == 0) {
                    HelperMethods.spawnParticlesPlane(
                            player.getWorld(),
                            Particles.holy_hit.particleType,
                            player.getBlockPos(),
                            radius, 0, 0.4, 0);
                }
            }
        }
        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
