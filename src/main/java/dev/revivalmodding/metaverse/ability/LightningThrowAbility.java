package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.AbstractCooldownAbility;
import dev.revivalmodding.metaverse.ability.interfaces.UpgradeableAbility;
import net.minecraft.nbt.CompoundNBT;

public class LightningThrowAbility extends AbstractCooldownAbility implements UpgradeableAbility {

    private int level = 1;

    public LightningThrowAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    public int getMaxCooldown() {
        int i = level - 1;
        return 200 - 10 * i;
    }

    @Override
    public boolean isMaxedOut() {
        return level == 10;
    }

    @Override
    public int getCurrentLevel() {
        return level;
    }

    @Override
    public int getMaxLevel() {
        return level;
    }

    @Override
    public int getUpgradeCost() {
        return 1;
    }

    @Override
    public void upgrade() {
        ++level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean shouldDisplayCurrentLevel() {
        return false;
    }

    @Override
    public CompoundNBT writeData() {
        CompoundNBT nbt = super.writeData();
        nbt.putInt("level", level);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        super.readData(nbt);
        level = Math.max(1, nbt.getInt("level"));
    }
}
