package dev.revivalmodding.metaverse.ability.interfaces;

import dev.revivalmodding.metaverse.util.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;

public interface UpgradeableAbility extends LevelableAbility {

    boolean isMaxedOut();

    int getUpgradeCost();

    void upgrade();

    default boolean canUpgrade(PlayerEntity player) {
        return PlayerUtils.getLevel(player) >= getUpgradeCost() && !isMaxedOut();
    }
}
