package dev.revivalmodding.metaverse.ability.core;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.metapower.Metapower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * Defines ability behaviour like toggle actions, activation etc.
 * Registered in {@link Registry.Handler#registerAbilityTypes(RegistryEvent.Register)}
 * @param <T> - the {@link IAbility} implementation
 * @author Toma
 */
public class AbilityType<T extends IAbility> extends ForgeRegistryEntry<AbilityType<?>> {

    /** Handles creating new {@link T} instances */
    private final IFactory<T> factory;
    /** Additional conditions for unlocking/activation */
    private final Predicate<PlayerEntity> canActivate;
    /** The amount of levels removed once player unlocks this ability */
    private final int price;
    /** Icon which will be shown in {@link dev.revivalmodding.metaverse.client.screen.AbilityScreen} */
    private final ResourceLocation iconTexture;
    /** Usually {@link TranslationTextComponent} for displaying name on various locations, for example in {@link dev.revivalmodding.metaverse.client.screen.AbilityScreen} */
    private final ITextComponent displayName;
    /** If this ability can be used even if player has no {@link Metapower} or the ability is not supported */
    private final boolean ignoreMetapowers;

    /** Obviously private constructor. Use the {@link Builder} to create new types */
    private AbilityType(Builder<T> builder) {
        this.factory = builder.factory;
        this.canActivate = builder.canActivate;
        this.price = builder.price;
        this.iconTexture = builder.iconTexture;
        this.displayName = builder.displayName;
        this.ignoreMetapowers = builder.ignoreMetapower;
    }

    public boolean canActivate(PlayerEntity player) {
        return ignoreMetapowers || Metapower.getPlayersPower(player) != null && Metapower.getPlayersPower(player).allows(this) && canActivate.test(player);
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

    public boolean isSameType(@Nullable IAbility ability) {
        return ability != null && ability.getType().getRegistryName().equals(getRegistryName());
    }

    public static class Builder<T extends IAbility> {

        private final IFactory<T> factory;
        private ResourceLocation registryName;
        private Predicate<PlayerEntity> canActivate = player -> true;
        private ResourceLocation iconTexture;
        private ITextComponent displayName;
        private int price;
        private boolean ignoreMetapower;

        public Builder(IFactory<T> factory) {
            this.factory = factory;
        }

        public Builder<T> name(ResourceLocation location) {
            this.registryName = location;
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

        public Builder<T> setIgnoreMetapowers() {
            this.ignoreMetapower = true;
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
