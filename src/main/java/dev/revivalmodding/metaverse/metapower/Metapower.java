package dev.revivalmodding.metaverse.metapower;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.util.object.LazyLoad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Metapower {

    // DEFAULT METAPOWERS
    public static Metapower SPEEDSTER_POWERS = register(() -> {
        List<AbilityType<?>> list = new ArrayList<>();
        list.add(Registry.AbilityTypes.SPEED);
        list.add(Registry.AbilityTypes.WATER_RUNNING);
        return list;
    });

    // register all power mixing exceptions here
    private static final LazyLoad<Map<AbilityType<?>, AbilityType<?>[]>> EXCEPTION_MAP = new LazyLoad<>(() -> {
        Map<AbilityType<?>, AbilityType<?>[]> map = new HashMap<>();
        return map;
    });
    private Supplier<List<AbilityType<?>>> toInit;
    private List<AbilityType<?>> containedAbilities;

    public static Metapower register(Supplier<List<AbilityType<?>>> baseAbilities) {
        Metapower metapower = new Metapower();
        metapower.toInit = baseAbilities;
        return metapower;
    }

    private Metapower() {
    }

    /**
     * Call this when attaching some metapower
     * @return new instance with same properties as this instance
     */
    public Metapower copy() {
        Metapower metapower = new Metapower();
        metapower.containedAbilities = this.getContainedAbilities();
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
