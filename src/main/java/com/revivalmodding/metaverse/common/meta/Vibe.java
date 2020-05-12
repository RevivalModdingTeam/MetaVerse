package com.revivalmodding.metaverse.common.meta;

import com.revivalmodding.metaverse.Metaverse;
import com.revivalmodding.metaverse.common.objects.blocks.Breach;
import com.revivalmodding.metaverse.common.objects.blocks.MVBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Metaverse.MODID)
public class Vibe {

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickEmpty event) {
        if(event.getPlayer().isSneaking()) {
            if(getPosLookingAt(event.getEntity()) != null) {
                Vec3d selected = getPosLookingAt(event.getEntity());
                assert selected != null;
                BlockPos newSelection = event.getWorld().getHeight(Heightmap.Type.WORLD_SURFACE, new BlockPos(selected));
                BlockPos currentLocation = event.getPlayer().getPosition();
                Block breach = new Breach(Block.Properties.from(MVBlocks.BREACH));
                if(event.getPlayer().getHorizontalFacing().equals(Direction.NORTH)) {
                    event.getWorld().setBlockState(currentLocation.north(2), breach.getDefaultState().with(DirectionalBlock.FACING, Direction.NORTH));
                } else if(event.getPlayer().getHorizontalFacing().equals(Direction.EAST)) {
                    event.getWorld().setBlockState(currentLocation.east(2), breach.getDefaultState().with(DirectionalBlock.FACING, Direction.EAST));
                } else if(event.getPlayer().getHorizontalFacing().equals(Direction.WEST)) {
                    event.getWorld().setBlockState(currentLocation.west(2), breach.getDefaultState().with(DirectionalBlock.FACING, Direction.WEST));
                } else {
                    event.getWorld().setBlockState(currentLocation.south(2), breach.getDefaultState().with(DirectionalBlock.FACING, Direction.SOUTH));
                }
            }
                BlockPos selectedPos = new BlockPos(selected);
                BlockPos newSelection = event.getWorld().getHeight(Heightmap.Type.WORLD_SURFACE, selectedPos);
                event.getPlayer().setPosition(newSelection.getX(), newSelection.getY(), newSelection.getZ());
            }
        }
    }

    private static Vec3d getPosLookingAt(Entity entity) {
        Vec3d lookVec = entity.getLookVec();
        for (int i = 0; i < 200 * 2; i++) {
            float scale = i / 2F;
            Vec3d pos = entity.getPositionVector().add(0, entity.getEyeHeight(), 0).add(lookVec.scale(scale));
            if (entity.world.getBlockState(new BlockPos(pos)).getCollisionShape(entity.world, new BlockPos(pos)) != VoxelShapes.empty() && !entity.world.isAirBlock(new BlockPos(pos))) {
                return pos;
            }
        }
        return null;
    }
}
