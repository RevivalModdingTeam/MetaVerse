package dev.revivalmodding.metaverse.init;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.tileentity.SuitMakerTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MVTileEntities {

    public static final DeferredRegister<TileEntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MetaVerse.MODID);
    public static final RegistryObject<TileEntityType<SuitMakerTileEntity>> SUIT_MAKER = TYPES.register("suit_maker", () -> TileEntityType.Builder.create(SuitMakerTileEntity::new, MVBlocks.SUIT_MAKER).build(null));
}
