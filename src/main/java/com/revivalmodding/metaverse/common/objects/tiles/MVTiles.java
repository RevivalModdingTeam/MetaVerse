package com.revivalmodding.metaverse.common.objects.tiles;

import com.revivalmodding.metaverse.Metaverse;
import com.revivalmodding.metaverse.common.objects.blocks.MVBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MVTiles {

    public static List<TileEntityType<?>> TILES = new ArrayList<>();

    public static final TileEntityType<?> BREACH = register(TileEntityType.Builder.create(BreachTile::new, MVBlocks.BREACH).build(null), "breach");

    private static TileEntityType<?> register(TileEntityType<?> tile, String name) {
        tile.setRegistryName(new ResourceLocation(Metaverse.MODID, name));
        TILES.add(tile);
        return tile;
    }

}
