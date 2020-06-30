package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.AbilityType;
import dev.revivalmodding.metaverse.ability.SpeedAbility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

public class Registry {

    // custom registries
    public static IForgeRegistry<AbilityType<?>> ABILITY_TYPES;

    @ObjectHolder(MetaVerse.MODID)
    public static final class AbilityTypes {
        public static final AbilityType<SpeedAbility> SPEED = null;
    }

    @Mod.EventBusSubscriber(modid = MetaVerse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Handler {

        @SuppressWarnings("unchecked")
        @SubscribeEvent
        public static void createRegistries(RegistryEvent.NewRegistry event) {
            makeRegistry(MetaVerse.getResource("ability_type"), AbilityType.class).create();
            ABILITY_TYPES = RegistryManager.ACTIVE.getRegistry(AbilityType.class);
        }

        @SubscribeEvent
        public static void registerAbilityTypes(RegistryEvent.Register<AbilityType<?>> event) {
            event.getRegistry().registerAll(
                    new AbilityType.Builder<>(SpeedAbility::new).name(MetaVerse.getResource("speed")).build()
            );
        }

        private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation resourceLocation, Class<T> type) {
            return new RegistryBuilder<T>().setName(resourceLocation).setType(type).setMaxID(Integer.MAX_VALUE - 1);
        }
    }
}
