package net.splurpy.paranoiamod.sanity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

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
}
