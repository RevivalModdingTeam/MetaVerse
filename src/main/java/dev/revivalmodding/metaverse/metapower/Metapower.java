package dev.revivalmodding.metaverse.metapower;

import com.google.common.collect.Lists;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.init.MVAbilities;
import dev.revivalmodding.metaverse.init.MVRegistries;
import dev.revivalmodding.metaverse.util.object.LazyLoad;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Metapower {

    // DEFAULT METAPOWERS
    public static Metapower SPEEDSTER_POWERS = register("speedster", () -> {
        List<AbilityType<?>> list = new ArrayList<>();
        list.add(MVAbilities.SPEED);
        list.add(MVAbilities.WATER_RUNNING);
        return list;
    });

    // register all power mixing exceptions here
    private static final LazyLoad<Map<AbilityType<?>, AbilityType<?>[]>> EXCEPTION_MAP = new LazyLoad<>(() -> {
        Map<AbilityType<?>, AbilityType<?>[]> map = new HashMap<>();
        return map;
    });
    private Supplier<List<AbilityType<?>>> toInit;
    private List<AbilityType<?>> containedAbilities;
    private final String name;

    public static Metapower register(String name, Supplier<List<AbilityType<?>>> baseAbilities) {
        Metapower metapower = new Metapower(name);
        metapower.toInit = baseAbilities;
        return metapower;
    }

    private Metapower(String name) {
        this.name = name;
    }

    @Nullable
    public static Metapower getPlayersPower(PlayerEntity player) {
        LazyOptional<PlayerData> lazyOptional = PlayerDataFactory.getCapability(player);
        if(lazyOptional.isPresent()) {
            PlayerData data = lazyOptional.orElse(null);
            return data.getMetapower();
        }
        return null;
    }

    public boolean allows(AbilityType<?> type) {
        return getContainedAbilities().contains(type);
    }

    /**
     * Call this when attaching some metapower
     * @return new instance with same properties as this instance
     */
    public Metapower copy() {
        Metapower metapower = new Metapower(name);
        metapower.containedAbilities = this.getContainedAbilities();
        return metapower;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("name", name);
        ListNBT contained = new ListNBT();
        List<AbilityType<?>> types = getContainedAbilities();
        for(AbilityType<?> type : types) {
            contained.add(StringNBT.valueOf(type.getRegistryName().toString()));
        }
        nbt.put("contained", contained);
        return nbt;
    }

    public static Metapower read(CompoundNBT nbt) {
        String origin = nbt.getString("name");
        Metapower metapower = new Metapower(origin);
        metapower.containedAbilities = Lists.newArrayList();
        ListNBT contained = nbt.getList("contained", Constants.NBT.TAG_STRING);

        for (int i = 0; i < contained.size(); i++) {
            ResourceLocation resourceLocation = new ResourceLocation(contained.getString(i));
            AbilityType<?> type = MVRegistries.ABILITIES.getValue(resourceLocation);
            if(type != null) metapower.containedAbilities.add(type);
        }
        return metapower;
    }

    public void mix(Metapower other) {
        List<AbilityType<?>> list = other.getContainedAbilities();
        for(AbilityType<?> type : list) {
            if(canMix(type)) {
                containedAbilities.add(type);
            }
        }
    }

    public boolean canMix(AbilityType<?> type) {
        for(AbilityType<?> abilityType : this.getContainedAbilities()) {
            AbilityType<?>[] array = EXCEPTION_MAP.get().get(abilityType);
            if(array != null && array.length > 0) {
                for(AbilityType<?> exceptionType : array) {
                    if(exceptionType == type) {
                        return false;
                    }
                }
            }
            return !containedAbilities.contains(type);
        }
        return true;
    }

    public List<AbilityType<?>> getContainedAbilities() {
        if(containedAbilities == null) {
            containedAbilities = toInit.get();
            toInit = null;
        }
        return containedAbilities;
    }
}
