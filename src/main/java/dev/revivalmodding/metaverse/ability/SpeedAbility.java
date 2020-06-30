package dev.revivalmodding.metaverse.ability;

import net.minecraft.nbt.CompoundNBT;

public class SpeedAbility extends Ability implements LevelableAbility {

    protected int speedLevel;

    public SpeedAbility(AbilityType<?> type) {
        super(type);
    }

    @Override
    protected CompoundNBT writeData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", speedLevel);
        return nbt;
    }

    @Override
    protected void readData(CompoundNBT nbt) {
        speedLevel = nbt.getInt("level");
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
        return 20;
    }
}
