package dev.revivalmodding.metaverse.util;

import dev.revivalmodding.metaverse.common.Registry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MVTabs {

    public static final ItemGroup MV = new ItemGroup("metaverse.global") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Registry.MVBlocks.SUIT_MAKER);
        }
    };
}
