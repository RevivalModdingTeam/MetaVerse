package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;
import dev.revivalmodding.metaverse.ability.interfaces.UpgradeableAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class BasicUpgradeableAbility implements IAbility, UpgradeableAbility {

    private AbilityType<?> type;
    private int level;
    private int maxCurrentLevel = 1;
    private final int maxLevel;

    public BasicUpgradeableAbility(AbilityType<?> type, int maxLevel) {
        this.type = type;
        this.maxLevel = maxLevel;
    }

    @Override
    public boolean canUpgrade(PlayerEntity player) {
        return level < maxCurrentLevel && maxCurrentLevel < maxLevel;
    }

    @Override
    public int getUpgradeCost() {
        return maxCurrentLevel;
    }

    @Override
    public void upgrade() {
        maxCurrentLevel++;
    }

    @Override
    public int getMaxLevel() {
        return maxCurrentLevel;
    }

    @Override
    public int getCurrentLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public CompoundNBT writeData() {
        CompoundNBT nbt = new CompoundNBT();
        getType().saveToNBT(nbt);
        nbt.putInt("level", level);
        nbt.putInt("maxLevel", maxCurrentLevel);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        type = AbilityType.readFromNBT(nbt);
        level = nbt.getInt("level");
        maxCurrentLevel = nbt.getInt("maxLevel");
    }

    @Override
    public void handleToggled(PlayerEntity player) {
        toggleBetween(this, player);
    }

    @Override
    public void handleTick(PlayerEntity player) {
        getType().onUpdate(this, player);
    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {
        getType().onLivingUpdate(this, player);
    }

    @Override
    public void handleDeactivated(PlayerEntity player) {
        getType().handleDeactivated(player);
    }

    @Override
    public boolean applyAbility() {
        return getCurrentLevel() > 0;
    }

    @Override
    public AbilityType<?> getType() {
        return type;
    }

    public static <T extends IAbility & LevelableAbility> void toggleBetween(T ability, PlayerEntity player) {
        int i = player.isSneaking() ? -1 : 1;
        int next = ability.getCurrentLevel() + i;
        if(next >= 0 && next <= ability.getMaxLevel()) {
            ability.setLevel(next);
            if(next == 0) {
                ability.handleDeactivated(player);
            }
        }
    }
}
