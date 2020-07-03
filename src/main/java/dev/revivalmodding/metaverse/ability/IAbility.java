package dev.revivalmodding.metaverse.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public interface IAbility {

    AbilityType<?> getType();

    CompoundNBT writeData();

    void readData(CompoundNBT nbt);

    void handleTick(PlayerEntity player);

    void handleLivingUpdate(PlayerEntity player);

    void handleToggled(PlayerEntity player);

    void handleDeactivated(PlayerEntity player);

    boolean applyAbility();
}
