package dev.revivalmodding.metaverse.ability.core;

import dev.revivalmodding.metaverse.ability.interfaces.CooldownAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractCooldownAbility implements IAbility, CooldownAbility {

    private AbilityType<?> type;
    private int cooldownTicks;

    public AbstractCooldownAbility(AbilityType<?> type) {
        this.type = type;
    }

    public abstract int getMaxCooldown();

    public abstract void onUse(PlayerEntity player);

    @Override
    public void handleTick(PlayerEntity player) {
        if(cooldownTicks > 0) {
            --cooldownTicks;
        }
    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {

    }

    @Override
    public float getCooldownProgress() {
        return cooldownTicks / (float) getMaxCooldown();
    }

    @Override
    public boolean isOnCooldown() {
        return cooldownTicks > 0;
    }

    @Override
    public boolean applyAbility() {
        return true;
    }

    @Override
    public final void handleToggled(PlayerEntity player) {
        if(!isOnCooldown()) {
            this.cooldownTicks = getMaxCooldown();
            onUse(player);
        }
    }

    @Override
    public void handleDeactivated(PlayerEntity player) {

    }

    @Override
    public CompoundNBT writeData() {
        CompoundNBT nbt = new CompoundNBT();
        type.saveToNBT(nbt);
        nbt.putInt("remaining", cooldownTicks);
        return nbt;
    }

    @Override
    public void readData(CompoundNBT nbt) {
        type = AbilityType.readFromNBT(nbt);
        cooldownTicks = nbt.getInt("remaining");
    }

    @Override
    public AbilityType<?> getType() {
        return type;
    }
}
