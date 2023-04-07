package net.splurpy.paranoiamod.sanity;

import net.minecraft.nbt.CompoundTag;

public class PlayerSanity {
    private int sanity;
    private final int MAX_SANITY = 100;
    private final int MIN_SANITY = 0;

    /**
     * Get the Player's current Sanity level.
     *
     * @return      Current sanity level
     */
    public int getSanity() {
        return this.sanity;
    }

    /**
     * Increment the player's sanity by a specified amount.
     *
     * If the resulting sanity is higher than allowed maximum,
     * set sanity to this maximum.
     *
     * @param amt       int amount to increase sanity by
     */
    public void incSanity(int amt) {
        this.sanity = Math.min(this.sanity + amt, MAX_SANITY);
    }

    /**
     * Decrement the player's sanity by a specified amount.
     *
     * If the resulting sanity is lower than allowed minimum,
     * set sanity to this minimum.
     *
     * @param amt       int amount to decrease sanity by
     */
    public void decSanity(int amt) {
        this.sanity = Math.max(this.sanity - amt, MIN_SANITY);
    }

    /**
     * Copy another PlayerSanity's value to this object.
     *
     * @param other     Source object to copy from
     */
    public void from(PlayerSanity other) {
        this.sanity = other.sanity;
    }

    /**
     * Saves the player's current sanity level as a CompoundTag.
     *
     * @param tag       tag to write to
     */
    public void saveNBT(CompoundTag tag) {
        tag.putInt("sanity", this.sanity);
    }

    /**
     * Loads the player's sanity from a CompoundTag.
     *
     * @param tag       tag to load from
     */
    public void loadNBT(CompoundTag tag) {
        this.sanity = tag.getInt("sanity");
    }
}
