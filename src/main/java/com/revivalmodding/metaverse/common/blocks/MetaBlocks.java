package com.revivalmodding.metaverse.common.blocks;

import com.revivalmodding.metaverse.common.items.MetaItems;
import com.revivalmodding.metaverse.util.helper.IHaveItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MetaBlocks {

    public static final List<Block> BLOCK_LIST = new ArrayList<>();


    public static Block RegisterBlock(Block block, String name, boolean tab) {
        block.setRegistryName(name);
        block.setTranslationKey(name);
        MetaBlocks.BLOCK_LIST.add(block);

        if (block instanceof IHaveItem) {
            if (((IHaveItem) block).hasItem()) {
                ItemBlock itemBlock = (ItemBlock) new ItemBlock(block).setRegistryName(name);

                if (tab) {
                    System.out.println("No Tab Defined");
                }
                MetaItems.registerRender(itemBlock);
                MetaItems.ITEM_LIST.add(itemBlock);
            }
        }
        return block;
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(MetaBlocks.BLOCK_LIST.toArray(new Block[0]));
    }
}
