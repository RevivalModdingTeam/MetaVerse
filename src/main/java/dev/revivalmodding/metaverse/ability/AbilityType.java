package dev.revivalmodding.metaverse.ability;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.Registry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class AbilityType<T extends IAbility> extends ForgeRegistryEntry<AbilityType<?>> {

    private final IFactory<T> factory;
    private final BiConsumer<T, PlayerEntity> tickHandler;
    private final BiConsumer<T, PlayerEntity> onToggle;
    private final Consumer<PlayerEntity> onDeactivated;
    private final Predicate<PlayerEntity> canActivate;
    private final int price;
    private final ResourceLocation iconTexture;
    private final ITextComponent displayName;

    private AbilityType(Builder<T> builder) {
        this.factory = builder.factory;
        this.tickHandler = builder.tickHandler;
        this.onToggle = builder.onToglePress;
        this.onDeactivated = builder.onDeactivated;
        this.canActivate = builder.canActivate;
        this.price = builder.price;
        this.iconTexture = builder.iconTexture;
        this.displayName = builder.displayName;
    }

    public void onUpdate(IAbility ability, PlayerEntity player) {
        this.tickHandler.accept((T) ability, player);
    }

    public void onToggled(IAbility ability, PlayerEntity player) {
        this.onToggle.accept((T) ability, player);
    }

    public void handleDeactivated(PlayerEntity player) {
        this.onDeactivated.accept(player);
    }

    public boolean canActivate(PlayerEntity player) {
        return canActivate.test(player);
    }

    public int getPrice() {
        return price;
    }

    public ResourceLocation getIcon() {
        return iconTexture;
    }

    public ITextComponent getDisplayName() {
        return displayName;
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

    @Override
    public String toString() {
        return getRegistryName().toString();
    }

    public static class Builder<T extends IAbility> {

        private final IFactory<T> factory;
        private ResourceLocation registryName;
        private BiConsumer<T, PlayerEntity> tickHandler = (ability, player) -> {};
        private Predicate<PlayerEntity> canActivate = player -> true;
        private BiConsumer<T, PlayerEntity> onToglePress = (ability, player) -> {};
        private Consumer<PlayerEntity> onDeactivated = player -> {};
        private ResourceLocation iconTexture;
        private ITextComponent displayName;
        private int price;

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

        public Builder<T> deactivate(Consumer<PlayerEntity> onDeactivated) {
            this.onDeactivated = onDeactivated;
            return this;
        }

        public Builder<T> onToggled(BiConsumer<T, PlayerEntity> onToglePress) {
            this.onToglePress = onToglePress;
            return this;
        }

        public Builder<T> icon(ResourceLocation location) {
            this.iconTexture = location;
            return this;
        }

        public Builder<T> icon(String path) {
            this.iconTexture = MetaVerse.getResource("textures/icons/" + path + ".png");
            return this;
        }

        public Builder<T> displayName(ITextComponent component) {
            this.displayName = component;
            return this;
        }

        public Builder<T> displayName(String key) {
            this.displayName = new TranslationTextComponent("ability." + key);
            return this;
        }

        @SuppressWarnings("unchecked")
        public AbilityType<T> build() {
            return (AbilityType<T>) new AbilityType<>(this).setRegistryName(this.registryName);
        }
    }

    public interface IFactory<T extends IAbility> {

        T create(AbilityType<T> abilityType);
    }
}
