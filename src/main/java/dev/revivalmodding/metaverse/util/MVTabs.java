package dev.revivalmodding.metaverse.util;

import dev.revivalmodding.metaverse.init.MVBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MVTabs {

    public static final ItemGroup MV = new ItemGroup("metaverse.global") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(MVBlocks.SUIT_MAKER);
        }
    };
}
