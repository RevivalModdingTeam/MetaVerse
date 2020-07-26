package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.Vec3d;

public class WaterRunningAbility extends BasicAbility {

    public WaterRunningAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    public void handleTick(PlayerEntity player) {
        if(player.isSprinting() && !player.isInWater() && player.world.getFluidState(player.getPosition().add(0, -0.1, 0)).isTagged(FluidTags.WATER)) {
            Vec3d vec3d = player.getMotion();
            player.setMotion(vec3d.x, 0, vec3d.z);
            player.fallDistance = 0.0F;
            player.onGround = true;
        }
    }
}
