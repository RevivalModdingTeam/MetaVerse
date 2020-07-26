package dev.revivalmodding.metaverse.ability.core;

import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractLevelableAbility implements IAbility, LevelableAbility {

    private AbilityType<?> type;

    public AbstractLevelableAbility(AbilityType<?> type) {
        this.type = type;
    }

    protected abstract CompoundNBT writeAdditionalData();

    protected abstract void readAdditionalData(CompoundNBT nbt);

    @Override
    public CompoundNBT writeData() {
        CompoundNBT nbt = new CompoundNBT();
        getType().saveToNBT(nbt);
        nbt.put("extra", writeAdditionalData());
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        type = AbilityType.readFromNBT(nbt);
        readAdditionalData(nbt.contains("extra") ? nbt.getCompound("extra") : new CompoundNBT());
    }

    @Override
    public void handleToggled(PlayerEntity player) {
        toggleBetween(this, player);
    }

    @Override
    public void handleTick(PlayerEntity player) {

    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {

    }

    @Override
    public void handleDeactivated(PlayerEntity player) {

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
