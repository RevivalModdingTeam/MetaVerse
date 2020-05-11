package com.revivalmodding.metaverse.common.objects;

import com.revivalmodding.metaverse.common.objects.blocks.MVBlocks;
import com.revivalmodding.metaverse.common.objects.items.MVItems;
import com.revivalmodding.metaverse.common.objects.tiles.MVTiles;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MVRegisterObjects {

    @SubscribeEvent
    public static void onItemRegister(final RegistryEvent.Register<Item> event) {
        for(Item item : MVItems.ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(final RegistryEvent.Register<Block> event) {
        for(Block block : MVBlocks.BLOCKS) {
            event.getRegistry().register(block);
        }
    }

    @SubscribeEvent
    public static void onTileEntityRegister(final RegistryEvent.Register<TileEntityType<?>> event) {
        for(TileEntityType<?> tiles : MVTiles.TILES) {
            event.getRegistry().register(tiles);
        }
    }

}
