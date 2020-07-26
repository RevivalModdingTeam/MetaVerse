package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.IAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class BasicAbility implements IAbility {

    private AbilityType<?> type;
    private boolean active;

    public BasicAbility(AbilityType<?> type) {
        this.type = type;
    }

    @Override
    public boolean applyAbility() {
        return active;
    }

    @Override
    public void handleTick(PlayerEntity player) {

    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {

    }

    @Override
    public void handleToggled(PlayerEntity player) {
        active = !active;
    }

    @Override
    public void handleDeactivated(PlayerEntity player) {

    }

    @Override
    public AbilityType<?> getType() {
        return type;
    }

    @Override
    public CompoundNBT writeData() {
        CompoundNBT nbt = new CompoundNBT();
        getType().saveToNBT(nbt);
        nbt.putBoolean("active", active);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        type = AbilityType.readFromNBT(nbt);
        active = nbt.getBoolean("active");
    }
}
