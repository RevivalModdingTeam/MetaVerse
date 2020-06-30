package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.common.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class AbilityType<T extends Ability> extends ForgeRegistryEntry<AbilityType<?>> {

    private final IFactory<T> factory;
    private final BiConsumer<T, PlayerEntity> tickHandler;
    private final BiConsumer<T, PlayerEntity> onToggle;
    private final Predicate<PlayerEntity> canActivate;
    private final int price;

    private AbilityType(Builder<T> builder) {
        this.factory = builder.factory;
        this.tickHandler = builder.tickHandler;
        this.onToggle = builder.onToglePress;
        this.canActivate = builder.canActivate;
        this.price = builder.price;
    }

    public void onUpdate(Ability ability, PlayerEntity player) {
        this.tickHandler.accept((T) ability, player);
    }

    public void onToggled(Ability ability, PlayerEntity player) {
        this.onToggle.accept((T) ability, player);
    }

    public boolean canActivate(PlayerEntity player) {
        return canActivate.test(player);
    }

    public int getPrice() {
        return price;
    }

    public T newInstance() {
        return factory.create(this);
    }

    public void saveToNBT(CompoundNBT nbt) {
        nbt.putString("type", this.getRegistryName().toString());
    }

    public static AbilityType<?> readFromNBT(CompoundNBT nbt) {
        ResourceLocation location = new ResourceLocation(nbt.getString("type"));
        return Registry.ABILITY_TYPES.getValue(location);
    }

    public static class Builder<T extends Ability> {

        private ResourceLocation registryName;
        private final IFactory<T> factory;
        private BiConsumer<T, PlayerEntity> tickHandler;
        private int price;
        private Predicate<PlayerEntity> canActivate;
        private BiConsumer<T, PlayerEntity> onToglePress;

        public Builder(IFactory<T> factory) {
            this.factory = factory;
        }

        public Builder<T> name(ResourceLocation location) {
            this.registryName = location;
            return this;
        }

        public Builder<T> onUpdate(BiConsumer<T, PlayerEntity> onUpdate) {
            this.tickHandler = onUpdate;
            return this;
        }

        public Builder<T> price(int price) {
            this.price = price;
            return this;
        }

        public Builder<T> canActivate(Predicate<PlayerEntity> predicate) {
            this.canActivate = predicate;
            return this;
        }

        public Builder<T> onToggled(BiConsumer<T, PlayerEntity> onToglePress) {
            this.onToglePress = onToglePress;
            return this;
        }

        @SuppressWarnings("unchecked")
        public AbilityType<T> build() {
            return (AbilityType<T>) new AbilityType<>(this).setRegistryName(this.registryName);
        }
    }

    public interface IFactory<T extends Ability> {

        T create(AbilityType<T> abilityType);
    }
}
