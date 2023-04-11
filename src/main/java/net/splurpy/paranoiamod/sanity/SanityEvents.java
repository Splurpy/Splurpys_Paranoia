package net.splurpy.paranoiamod.sanity;

import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraftforge.event.TickEvent;
import net.splurpy.paranoiamod.sound.ModSounds;

import java.util.HashMap;


public class SanityEvents {

    public static SoundEvent chooseSoundEvent(TickEvent.PlayerTickEvent event) {
        SoundEvent resultSound = SoundEvents.CREEPER_PRIMED; // Default SoundEvent if execution goes wrong

        // Getting necessary info for determining the best sounds to play
        ResourceKey<DimensionType> dimension = event.player.getLevel().dimensionTypeId();
        boolean isNight = event.player.getLevel().getDayTime() >= 13000;
        Difficulty difficulty = event.player.getLevel().getDifficulty(); // We want anything but DIFFICULTY.PEACEFUL
        boolean isInCave = ((event.player.getY() <= 60) && event.player.getLevel().getBrightness(LightLayer.SKY, event.player.getOnPos()) == 0) && !event.player.isUnderWater();

        if (difficulty.equals(Difficulty.PEACEFUL)) {
            if (dimension.equals(BuiltinDimensionTypes.OVERWORLD)) {
                if (isInCave) resultSound = SoundEvents.AMBIENT_CAVE;
            }
            else resultSound = SoundEvents.ARROW_HIT;
        }
        else {
            HashMap<Integer, SoundEvent> soundMap = new HashMap<Integer, SoundEvent>();
            Integer selector;

            if (dimension.equals(BuiltinDimensionTypes.OVERWORLD)) soundMap = getOverworldSoundMap(isInCave, isNight); // Overworld
            else if (dimension.equals(BuiltinDimensionTypes.NETHER)) soundMap = getNetherSoundMap(); // Nether
            else soundMap = getEndSoundMap(); // End

            selector = event.player.getRandom().nextInt(0, soundMap.size() - 1);

            resultSound = soundMap.get(selector);
        }

        return resultSound;
    }

    public static void playSoundFor(SoundEvent soundEvent, SoundSource soundSource, Player player, boolean onPlayer, boolean randVolume) {
        if (player.getLevel().isClientSide()) {
            BlockPos soundPos;
            if (onPlayer) soundPos = player.getOnPos();
            else soundPos = SanityUtil.getSoundLocation(player);

            float volume;
            if (randVolume) volume = player.getRandom().nextFloat();
            else volume = 1.0f;

            player.getLevel().playLocalSound(soundPos.getX(), soundPos.getY(), soundPos.getZ(), soundEvent, soundSource, volume, 1.0f, true);
        }
    }

    private static HashMap<Integer, SoundEvent> getNetherSoundMap() {
        HashMap<Integer, SoundEvent> soundMap = new HashMap<Integer, SoundEvent>();

        soundMap.put(0, SoundEvents.GHAST_WARN);
        soundMap.put(1, SoundEvents.GHAST_SHOOT);
        soundMap.put(2, SoundEvents.PIGLIN_ANGRY);
        soundMap.put(3, SoundEvents.BLAZE_SHOOT);
        soundMap.put(4, SoundEvents.ANCIENT_DEBRIS_BREAK);
        soundMap.put(5, SoundEvents.NETHER_BRICKS_PLACE);
        soundMap.put(6, SoundEvents.WITHER_SKELETON_STEP);
        soundMap.put(7, SoundEvents.WITHER_SKELETON_AMBIENT);
        soundMap.put(8, SoundEvents.NETHERRACK_STEP);
        soundMap.put(9, SoundEvents.NETHERRACK_PLACE);
        soundMap.put(10, SoundEvents.NETHERRACK_BREAK);
        soundMap.put(11, ModSounds.KNOCKING_AMBIENT.get());

        return soundMap;
    }

    private static HashMap<Integer, SoundEvent> getEndSoundMap() {
        HashMap<Integer, SoundEvent> soundMap = new HashMap<Integer, SoundEvent>();

        soundMap.put(0, SoundEvents.ENDER_DRAGON_SHOOT);
        soundMap.put(1, SoundEvents.ENDERMAN_SCREAM);
        soundMap.put(2, SoundEvents.ENDERMAN_STARE);
        soundMap.put(3, SoundEvents.ENDER_DRAGON_SHOOT);
        soundMap.put(4, ModSounds.KNOCKING_AMBIENT.get());

        return soundMap;
    }

    private static HashMap<Integer, SoundEvent> getOverworldSoundMap(boolean inCave, boolean isNight) {
        HashMap<Integer, SoundEvent> soundMap = new HashMap<Integer, SoundEvent>();

        if (inCave) {
            soundMap.put(0, SoundEvents.AMBIENT_CAVE);
            soundMap.put(1, SoundEvents.CAVE_VINES_STEP);
            soundMap.put(2, SoundEvents.CAVE_VINES_STEP);
            soundMap.put(3, SoundEvents.STONE_BREAK);
            soundMap.put(4, SoundEvents.STONE_STEP);
            soundMap.put(5, SoundEvents.STONE_PLACE);
            soundMap.put(6, SoundEvents.ENDERMAN_AMBIENT);
            soundMap.put(7, SoundEvents.ENDERMAN_SCREAM);
            soundMap.put(8, SoundEvents.SKELETON_AMBIENT);
            soundMap.put(9, SoundEvents.ZOMBIE_AMBIENT);
            soundMap.put(10, SoundEvents.CREEPER_PRIMED);
            soundMap.put(11, SoundEvents.POINTED_DRIPSTONE_FALL);
            soundMap.put(12, SoundEvents.SCULK_CLICKING);
            soundMap.put(13, ModSounds.KNOCKING_AMBIENT.get());
            soundMap.put(14, SoundEvents.ARROW_HIT);
        }
        else {
            if (isNight) {
                soundMap.put(0, SoundEvents.ENDERMAN_AMBIENT);
                soundMap.put(1, SoundEvents.ENDERMAN_SCREAM);
                soundMap.put(2, SoundEvents.SKELETON_AMBIENT);
                soundMap.put(3, SoundEvents.ZOMBIE_AMBIENT);
                soundMap.put(4, SoundEvents.CREEPER_PRIMED);
                soundMap.put(5, SoundEvents.PHANTOM_FLAP);
                soundMap.put(6, SoundEvents.PHANTOM_AMBIENT);
                soundMap.put(7, SoundEvents.SPIDER_AMBIENT);
                soundMap.put(8, ModSounds.KNOCKING_AMBIENT.get());
                soundMap.put(9, SoundEvents.ARROW_HIT);
                soundMap.put(10, SoundEvents.AMBIENT_CAVE);
            }
            else {
                soundMap.put(0, SoundEvents.CREEPER_PRIMED);
                soundMap.put(1, SoundEvents.ENDERMAN_AMBIENT);
                soundMap.put(2, ModSounds.KNOCKING_AMBIENT.get());
                soundMap.put(4, SoundEvents.ARROW_HIT);
            }
        }

        return soundMap;
    }


}
