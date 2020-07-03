package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.ability.interfaces.CooldownAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class BasicCooldownAbility implements IAbility, CooldownAbility {

    private AbilityType<?> type;
    private int cooldownTicks;
    private final int cooldown;

    public BasicCooldownAbility(AbilityType<?> type, int cooldownTime) {
        this.type = type;
        this.cooldown = cooldownTime;
    }

    @Override
    public void handleTick(PlayerEntity player) {
        if(cooldownTicks > 0) {
            --cooldownTicks;
        }
    }

    @Override
    public void handleLivingUpdate(PlayerEntity player) {
        getType().onLivingUpdate(this, player);
    }

    @Override
    public float getCooldownProgress() {
        return cooldownTicks / (float) cooldown;
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
    public void handleToggled(PlayerEntity player) {
        if(!isOnCooldown()) {
            getType().onToggled(this, player);
            this.cooldownTicks = cooldown;
        }
    }

    @Override
    public void handleDeactivated(PlayerEntity player) {
        getType().handleDeactivated(player);
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
