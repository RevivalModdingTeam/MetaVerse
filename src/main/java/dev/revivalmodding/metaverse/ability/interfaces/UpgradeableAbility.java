package dev.revivalmodding.metaverse.ability.interfaces;

import net.minecraft.entity.player.PlayerEntity;

// TODO implement behavior
public interface UpgradeableAbility {

    boolean canUpgrade(PlayerEntity player);

    int getUpgradeCost();

    void upgrade();
}
