package dev.revivalmodding.metaverse.init;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MVRegistries {

    public static IForgeRegistry<AbilityType<?>> ABILITIES;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        create(MetaVerse.getResource("ability_type"), AbilityType.class).create();
        ABILITIES = RegistryManager.ACTIVE.getRegistry(AbilityType.class);
    }

    private static <V extends IForgeRegistryEntry<V>> RegistryBuilder<V> create(ResourceLocation location, Class<V> type) {
        return new RegistryBuilder<V>().setName(location).setType(type).setMaxID(Integer.MAX_VALUE - 1);
    }
}
