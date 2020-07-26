package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class PhasingAbility extends BasicAbility {

    public PhasingAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {
        if(player.world.getBlockState(player.getPosition().add(0, -0.1, 0)).getMaterial().isSolid()) {
            player.noClip = true;
            Vec3d vec3d = player.getMotion();
            player.setMotion(vec3d.x, 0, vec3d.z);
            player.onGround = true;
        }
    }
}
