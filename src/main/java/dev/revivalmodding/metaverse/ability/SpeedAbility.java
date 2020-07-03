package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;

public class SpeedAbility extends BasicUpgradeableAbility {

    public SpeedAbility(AbilityType<? extends LevelableAbility> type) {
        super(type, 20);
    }
}
