package dev.revivalmodding.metaverse.common.blocks;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class SmallParticleAcceleratorBlock extends Block {

    public SmallParticleAcceleratorBlock(String name) {
        super(Properties.create(Material.IRON));
        setRegistryName(MetaVerse.getResource(name));
        Registry.Handler.blockItemsToRegister.add(Utils.createBlockItemFor(this));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
