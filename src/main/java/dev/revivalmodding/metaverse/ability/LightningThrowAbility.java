package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.AbstractCooldownAbility;

public class LightningThrowAbility extends AbstractCooldownAbility {

    public LightningThrowAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    public int getMaxCooldown() {
        return 200;
    }
}
