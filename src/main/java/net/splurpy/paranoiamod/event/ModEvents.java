package net.splurpy.paranoiamod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.data.worldgen.DimensionTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.splurpy.paranoiamod.ParanoiaMod;
import net.splurpy.paranoiamod.client.ClientSanityData;
import net.splurpy.paranoiamod.effect.InsaneEffect;
import net.splurpy.paranoiamod.effect.ModEffects;
import net.splurpy.paranoiamod.networking.ModMessages;
import net.splurpy.paranoiamod.networking.packet.SanityDataSyncS2CPacket;
import net.splurpy.paranoiamod.particle.ModParticles;
import net.splurpy.paranoiamod.particle.custom.LithiumFlameParticles;
import net.splurpy.paranoiamod.sanity.PlayerSanity;
import net.splurpy.paranoiamod.sanity.PlayerSanityProvider;
import net.splurpy.paranoiamod.sanity.SanityEvents;
import net.splurpy.paranoiamod.sanity.SanityUtil;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.List;

import static net.minecraft.world.entity.ai.targeting.TargetingConditions.forCombat;

@Mod.EventBusSubscriber(modid = ParanoiaMod.MOD_ID)
public class ModEvents {
    static int tickCount = 0;
    @SubscribeEvent
    public static void onAttachCapabilitesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerSanityProvider.PLAYER_SANITY).isPresent()) {
                event.addCapability(new ResourceLocation(ParanoiaMod.MOD_ID, "properties"), new PlayerSanityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(newStore -> {
                    newStore.from(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerSanity.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        event.player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
            if(event.side == LogicalSide.SERVER) {

                if(sanity.getSanity() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg

                    if (event.player.getLevel().dimensionTypeId().equals(BuiltinDimensionTypes.NETHER)) {
                        sanity.decSanity(1);
                        event.player.sendSystemMessage(Component.literal("Sanity has decreased by 1 - NETHER"));
                        ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.player);
                    }
                    else {
                        if (((event.player.level.getBrightness(LightLayer.SKY, event.player.blockPosition())) +
                                (event.player.level.getBrightness(LightLayer.BLOCK, event.player.blockPosition())) / 2) <= 2 && !event.player.hasEffect(MobEffects.NIGHT_VISION)) {
                            sanity.decSanity(1);
                            event.player.sendSystemMessage(Component.literal("Sanity has decreased by 1 - DARKNESS"));
                            ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.player);
                        }
                    }
                }

                if(sanity.getSanity() > 0 && event.player.getRandom().nextFloat() < 0.0005f) {
                    if (SanityUtil.isNearPurgingTorch(event.player)) {
                        sanity.incSanity(2);
                        event.player.sendSystemMessage(Component.literal("Sanity has increased by 2 - PURGING TORCH"));
                        ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.player);
                    }
                }

                if (SanityUtil.isInsane(ClientSanityData.getPlayerSanity())) {
                    event.player.addEffect(new MobEffectInstance(ModEffects.INSANE.get()));
                    event.player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 6));
                }
                else {
                    event.player.removeEffect(ModEffects.INSANE.get());
                    AABB BB = event.player.getBoundingBox().inflate(20);
                    List<Warden> entities = event.player.getLevel().getNearbyEntities(Warden.class, TargetingConditions.forCombat(), event.player, BB);
                    // System.out.println("[DEBUG] Entities: " + entities.toString());
                    if (!(entities.size() > 0))
                    {
                        event.player.removeEffect(MobEffects.DARKNESS);
                    }
                }
            }

            if (event.side == LogicalSide.CLIENT) {
                if (sanity.getSanity() <= 79 && event.player.getRandom().nextFloat() < SanityUtil.getSoundFreq(ClientSanityData.getPlayerSanity())) { // Playing sounds based on sanity level
                    SoundEvent soundToPlay = SanityEvents.chooseSoundEvent(event);
                    SanityEvents.playSoundFor(soundToPlay, SoundSource.HOSTILE, (Player) event.player, false, true);
                }

                if (event.player.hasEffect(ModEffects.INSANE.get()))
                {
                    tickCount++;
                    if (tickCount % 40 == 0)
                    {
                        Player entity = event.player;
                        event.player.getLevel().playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.AMBIENT, 1.0f, 1.0f, false);
                        // event.player.sendSystemMessage(Component.literal("[DEBUG] Inside tickCount if statement"));
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void LivingEntityUseItemEvent(LivingEntityUseItemEvent.Finish event) {
        if (event.getItem().getItem().equals(Items.ROTTEN_FLESH)) {
            if (event.getEntity() instanceof Player player) {
                player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.decSanity(2);
                    player.sendSystemMessage(Component.literal("Consumed 1 Rotten Flesh: Decreased Sanity by 2"));
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void DimensionChangeEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getTo().equals(BuiltinDimensionTypes.OVERWORLD)) {
            if (event.getEntity() instanceof Player) {
                event.getEntity().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.decSanity(5);
                    event.getEntity().sendSystemMessage(Component.literal("Decreased Sanity by 5 - ENTERED ALTERNATE DIMENSION"));
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.getEntity());
                });
            }
        }
    }

    @SubscribeEvent
    public static void LivingDeathEvent(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player) {
            if (event.getEntity() instanceof Villager || event.getEntity() instanceof Cat ||
            event.getEntity() instanceof Wolf || event.getEntity() instanceof Rabbit || event.getEntity() instanceof ZombieVillager) {
                event.getSource().getEntity().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.decSanity(3);
                    event.getSource().getEntity().sendSystemMessage(Component.literal("Decreased Sanity by 3 - KILLED FRIENDLY MOB"));
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.getSource().getEntity());
                });
            }
        }
    }

    @SubscribeEvent
    public static void LivingHurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().equals(DamageSource.ON_FIRE) || event.getSource().equals(DamageSource.DROWN) ||
                    event.getSource().equals(DamageSource.STARVE) || event.getSource().equals(DamageSource.WITHER) || event.getSource().equals(DamageSource.LAVA) ||
                    event.getSource().equals(DamageSource.LIGHTNING_BOLT) || event.getSource().equals(DamageSource.IN_FIRE)) {
                player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.decSanity(2);
                    player.sendSystemMessage(Component.literal("Sanity has decreased by 2 - DISTURBING DAMAGE"));
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) player);
                });
            }
            else {
                if (event.getSource().equals(DamageSource.FALL)) {
                    float remainingHealth = event.getEntity().getHealth() - event.getAmount();
                    if (remainingHealth <= 2.5f) {
                        player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                            sanity.decSanity(10);
                            player.sendSystemMessage(Component.literal("-10 Sanity - Significant Fall"));
                            ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) player);
                        });
                    }
                }
            }

            if (player.getLevel().isClientSide()) {
                if (player.getRandom().nextInt(1, 10) <= SanityUtil.getBreakProbability(ClientSanityData.getPlayerSanity()))
                    SanityEvents.playSoundFor(SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, player, true, false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJoinWorld(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide()) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), player);
                });
            }
        }
    }

    @SubscribeEvent
    public static void PlayerWakeUpEvent(PlayerWakeUpEvent event) {
        if (!event.getEntity().getLevel().isClientSide()) {
            Player entity = event.getEntity();
            if ((entity.getLevel().getDayTime() >= 0 && entity.getLevel().getDayTime() <= 10) || entity.getLevel().getDayTime() == 24000) {
                entity.getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                    sanity.incSanity(20);
                    entity.sendSystemMessage(Component.literal("Sanity increased by 20 - Slept"));
                    ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) entity);
                });
            }
        }
    }

    @SubscribeEvent
    public static void BreakBlockEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            if (!event.getLevel().isClientSide()) {
                if (event.getState().getBlock() == Blocks.DIAMOND_ORE || event.getState().getBlock() == Blocks.DEEPSLATE_DIAMOND_ORE
                        || event.getState().getBlock() == Blocks.GOLD_ORE || event.getState().getBlock() == Blocks.DEEPSLATE_GOLD_ORE
                        || event.getState().getBlock() == Blocks.NETHER_GOLD_ORE) {
                    event.getPlayer().getCapability(PlayerSanityProvider.PLAYER_SANITY).ifPresent(sanity -> {
                        sanity.incSanity(3);
                        event.getPlayer().sendSystemMessage(Component.literal("Sanity increased by 3 - Found precious ore"));
                        ModMessages.sendToPlayer(new SanityDataSyncS2CPacket(sanity.getSanity()), (ServerPlayer) event.getPlayer());
                    });
                }
            }
        }
    }

    /*===============EventBus Events================*/
    /*
    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.register(ModParticles.LITHIUM_FLAME_PARTICLES.get(),
                LithiumFlameParticles.Provider::new);
    }
     */
}
