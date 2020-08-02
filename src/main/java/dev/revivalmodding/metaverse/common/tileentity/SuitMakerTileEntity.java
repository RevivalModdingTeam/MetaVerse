package dev.revivalmodding.metaverse.common.tileentity;

import net.minecraft.tileentity.TileEntity;

import static dev.revivalmodding.metaverse.init.MVTileEntities.SUIT_MAKER;

public class SuitMakerTileEntity extends TileEntity {

    public SuitMakerTileEntity() {
        super(SUIT_MAKER.get());
    }
}
