package net.splurpy.paranoiamod.sanity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.splurpy.paranoiamod.block.ModBlocks;

import java.util.Arrays;

public class SanityUtil {

    /**
     * Should, in theory, return a BlockPos that is two blocks behind the player.
     *
     * @param player        Player whose blockPos is used
     * @return              BlockPos that is two blocks behind the player
     */
    public static BlockPos getSoundLocation(Player player) {
        int x, y, z;
        x = player.getBlockX();
        y= player.getBlockY();
        z = player.getBlockZ();
        Direction facingDir = player.getDirection();

        if (facingDir.equals(Direction.NORTH)) {
            if (z < 0) z+=2;
            else z-=2;

            x = player.getRandom().nextInt(x-10, x+10);
        }
        else if (facingDir.equals(Direction.SOUTH)) {
            if (z < 0) z-=2;
            else z+=2;

            x = player.getRandom().nextInt(x-10, x+10);
        }
        else if (facingDir.equals(Direction.WEST)) {
            if (x < 0) x+=2;
            else x-=2;

            z = player.getRandom().nextInt(z-10, z+10);
        }
        else {
            if (x < 0) x-=2;
            else x+=2;

            z = player.getRandom().nextInt(z-10, z+10);
        }

        return new BlockPos(x, y, z);
    }

    public static float getSoundFreq(int sanityLevel) {
        float soundFreq;

        if (sanityLevel < 20) soundFreq = 0.005f;
        else if (sanityLevel < 40) soundFreq = 0.0005f;
        else if (sanityLevel < 60) soundFreq = 0.00007f;
        else if (sanityLevel < 80) soundFreq = 0.000005f;
        else soundFreq = -1.0f;

        // System.out.println("Sanity is " + sanityLevel + ", soundFreq is " + soundFreq);
        return soundFreq;
    }

    public static int getBreakProbability(int sanityLevel) {
        int prob;

        if (sanityLevel < 20) prob = 7;
        else if (sanityLevel < 40) prob = 5;
        else if (sanityLevel < 60) prob = 4;
        else if (sanityLevel < 80) prob = 3;
        else prob = -0;

        return prob;
    }

    public static String getStage(int sanityLevel) {
        if (sanityLevel < 20) return "Insane";
        else if (sanityLevel < 40) return "Paranoid";
        else if (sanityLevel < 60) return "Anxious";
        else if (sanityLevel < 80) return "Unsettled";
        else return "Sane";
    }

    public static boolean isInsane(int sanityLevel) {
        if (sanityLevel < 20) return true;
        else return false;
    }

    public static boolean isNearPurgingTorch(Player player) {
        Level level = player.getLevel();
        Block[] torches = {ModBlocks.LITHIUM_TORCH.get(), ModBlocks.LITHIUM_WALL_TORCH.get()};
        for (int x = player.getBlockX() - 4; x < player.getBlockX() + 5; x++) {

            // player.sendSystemMessage(Component.literal("Block at x = " + x + " is " + level.getBlockState(new BlockPos(x, player.getBlockY(), player.getBlockZ())).getBlock()));

            for (int y = player.getBlockY() - 2; y < player.getBlockY() + 3; y++) {
                // player.sendSystemMessage(Component.literal("Block at y = " + y + " is " + level.getBlockState(new BlockPos(player.getBlockX(), y, player.getBlockZ())).getBlock()));

                for (int z = player.getBlockZ() - 4; z < player.getBlockZ() + 5; z++) {
                    // player.sendSystemMessage(Component.literal("Block at z = " + z + " is " + level.getBlockState(new BlockPos(player.getBlockX(), player.getBlockY(), z)).getBlock()));
                    BlockPos checkPos = new BlockPos(x, y, z);
                    // player.sendSystemMessage(Component.literal("Checking BlockPos: x = " + checkPos.getX() + ", y = " + checkPos.getY() + ", z = " + checkPos.getZ()));
                    Block checkBlock = level.getBlockState(checkPos).getBlock();
                    if (Arrays.asList(torches).contains(checkBlock)) {
                        // player.sendSystemMessage(Component.literal("Found a valid block"));
                        return true;
                    }
                }
            }

            /*
            if (currentBlock == ModBlocks.LITHIUM_TORCH.get() || currentBlock == ModBlocks.LITHIUM_WALL_TORCH.get())
                player.sendSystemMessage(Component.literal("Near Purging Torch"));
            return true;
             */
        }
        // player.sendSystemMessage(Component.literal("No Purging Torch Nearby"));
        return false;
    }
}
