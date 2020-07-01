package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;
import dev.revivalmodding.metaverse.ability.interfaces.UpgradeableAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class SpeedAbility extends AbstractLevelableAbility implements UpgradeableAbility {

    protected int speedLevel;
    protected int maxLevel = 1;

    public SpeedAbility(AbilityType<? extends LevelableAbility> type) {
        super(type);
    }

    @Override
    public boolean canUpgrade(PlayerEntity player) {
        return maxLevel < 20;
    }

    @Override
    public void upgrade() {
        ++maxLevel;
    }

    @Override
    public int getUpgradeCost() {
        return maxLevel;
    }

    @Override
    protected CompoundNBT writeAdditionalData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", speedLevel);
        nbt.putInt("max", maxLevel);
        return nbt;
    }

    @Override
    protected void readAdditionalData(CompoundNBT nbt) {
        speedLevel = nbt.getInt("level");
        maxLevel = nbt.getInt("max");
    }

    @Override
    public void setLevel(int level) {
        this.speedLevel = level;
    }

    @Override
    public int getCurrentLevel() {
        return speedLevel;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }
}
