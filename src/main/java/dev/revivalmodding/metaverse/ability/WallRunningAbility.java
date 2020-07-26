package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class WallRunningAbility extends BasicAbility {

    public WallRunningAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    public void handleTick(PlayerEntity player) {
        if(player.collidedHorizontally) {
            Vec3d motion = player.getMotion();
            player.setMotion(motion.x, player.getAIMoveSpeed(), motion.z);
            player.fallDistance = 0;
            // todo modify limbswing for swinging arms?
        }
    }
}
