package dev.revivalmodding.metaverse.ability.interfaces;

import net.minecraft.entity.player.PlayerEntity;

public interface UpgradeableAbility extends LevelableAbility {

    boolean canUpgrade(PlayerEntity player);

    int getUpgradeCost();

    void upgrade();
}
