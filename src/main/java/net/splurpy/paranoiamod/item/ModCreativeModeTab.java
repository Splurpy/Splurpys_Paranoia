package net.splurpy.paranoiamod.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {

    public static final CreativeModeTab TAB_PARANOIA = new CreativeModeTab("paranoiatab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.LITHIUM.get());
        }
    };
}
