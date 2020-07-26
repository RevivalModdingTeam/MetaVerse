package dev.revivalmodding.metaverse.common.blocks;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TrailEditorBlock extends Block {

    public TrailEditorBlock(String name) {
        super(Properties.create(Material.WOOD));
        setRegistryName(MetaVerse.getResource(name));
        Registry.Handler.blockItemsToRegister.add(Utils.createBlockItemFor(this));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }
}
