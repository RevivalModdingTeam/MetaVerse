package com.revivalmodding.metaverse.common.objects.blocks;

import com.revivalmodding.metaverse.Metaverse;
import com.revivalmodding.metaverse.common.objects.items.MVItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MVBlocks {

    public static List<Block> BLOCKS = new ArrayList<>();

    public static final Block BREACH = register(new Breach(Block.Properties.create(Material.MISCELLANEOUS).lightValue(6)), ItemGroup.MISC, "breach");

    private static Block register(Block block, String name) {
        block.setRegistryName(new ResourceLocation(Metaverse.MODID, name));
        BLOCKS.add(block);
        return block;
    }

    private static Block register(Block block, ItemGroup group, String name) {
        block.setRegistryName(new ResourceLocation(Metaverse.MODID, name));
        BLOCKS.add(block);
        MVItems.ITEMS.add(new BlockItem(block, new Item.Properties().group(group)).setRegistryName(new ResourceLocation(Metaverse.MODID, name)));
        return block;
    }
}
