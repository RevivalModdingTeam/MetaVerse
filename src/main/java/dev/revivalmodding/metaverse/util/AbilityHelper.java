package dev.revivalmodding.metaverse.util;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;

public class AbilityHelper {

    public static boolean hasActiveAbility(AbilityType<?> type, Abilities abilities) {
        return Utils.contains(type, abilities.getActiveAbilities(), AbilityType::isSameType);
    }
}
