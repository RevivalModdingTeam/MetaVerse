package dev.revivalmodding.metaverse.common.capability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerDataProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(PlayerData.class)
    public static Capability<PlayerData> CAP = null;
    public LazyOptional<PlayerData> instance = LazyOptional.of(CAP::getDefaultInstance);

    public PlayerDataProvider() {}

    public PlayerDataProvider(PlayerEntity player) {
        instance = LazyOptional.of(() -> new PlayerDataFactory(player));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CAP ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) CAP.getStorage().writeNBT(CAP, instance.orElseThrow(NullPointerException::new), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CAP.getStorage().readNBT(CAP, instance.orElseThrow(NullPointerException::new), null, nbt);
    }
}
