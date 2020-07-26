package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.AbstractUpgradeableAbility;
import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;
import net.minecraft.entity.player.PlayerEntity;

public class SpeedAbility extends AbstractUpgradeableAbility {

    public SpeedAbility(AbilityType<? extends LevelableAbility> type) {
        super(type);
    }

    @Override
    public int getLevelLimit() {
        return 20;
    }

    @Override
    public void handleTick(PlayerEntity player) {
        float f = 0.1F + 0.05F * getCurrentLevel();
        player.abilities.setWalkSpeed(f);
    }

    @Override
    public void handleDeactivated(PlayerEntity player) {
        player.abilities.setWalkSpeed(0.1F);
    }
}
