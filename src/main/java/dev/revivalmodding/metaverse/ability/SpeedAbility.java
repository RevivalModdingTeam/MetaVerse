package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.AbstractUpgradeableAbility;
import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;

public class SpeedAbility extends AbstractUpgradeableAbility {

    public SpeedAbility(AbilityType<? extends LevelableAbility> type) {
        super(type);
    }

    @Override
    public int getLevelLimit() {
        return 20;
    }
}
