package dev.revivalmodding.metaverse.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class AbilityType<T extends Ability> extends ForgeRegistryEntry<AbilityType<?>> {

    private final Supplier<T> factory;
    private final BiConsumer<T, PlayerEntity> tickHandler;

    private AbilityType(Builder<T> builder) {
        this.factory = builder.factory;
        this.tickHandler = builder.tickHandler;
    }

    public void onUpdate(Ability ability, PlayerEntity player) {
        this.tickHandler.accept((T) ability, player);
    }

    public T newInstance() {
        return factory.get();
    }

    public static class Builder<T extends Ability> {

        private ResourceLocation registryName;
        private final Supplier<T> factory;
        private BiConsumer<T, PlayerEntity> tickHandler;
        private int price;
        private Predicate<PlayerEntity> canActivate;
        private BiConsumer<T, PlayerEntity> onToglePress;

        public Builder(Supplier<T> factory) {
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
}
