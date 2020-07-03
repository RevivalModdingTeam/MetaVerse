package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.AbilityType;
import dev.revivalmodding.metaverse.ability.BasicAbility;
import dev.revivalmodding.metaverse.ability.BasicCooldownAbility;
import dev.revivalmodding.metaverse.ability.SpeedAbility;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
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
        public static final AbilityType<BasicAbility> WALL_RUNNING = null;
        public static final AbilityType<BasicAbility> LIGHTNING_THROW = null;
        public static final AbilityType<BasicAbility> WATER_RUNNING = null;
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
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().register(new Block(Block.Properties.create(Material.IRON)).setRegistryName("test"));
        }

        @SubscribeEvent
        public static void registerAbilityTypes(RegistryEvent.Register<AbilityType<?>> event) {
            event.getRegistry().registerAll(
                    new AbilityType.Builder<>(SpeedAbility::new)
                            .name(MetaVerse.getResource("speed"))
                            .price(3)
                            .icon("ability_speed")
                            .displayName("speed")
                            .onUpdate((ability, player) -> {
                                int level = ability.getCurrentLevel();
                                float value = 0.1F + 0.05F * level;
                                player.abilities.setWalkSpeed(value);
                            })
                            .deactivate(player -> player.abilities.setWalkSpeed(0.1F))
                            .build(),
                    new AbilityType.Builder<>(BasicAbility::new)
                            .name(MetaVerse.getResource("wall_running"))
                            .price(0)
                            .icon("ability_wallrunning")
                            .displayName("wall_running")
                            .onUpdate((ability, player) -> {
                                if(player.collidedHorizontally) {
                                    Vec3d motion = player.getMotion();
                                    player.setMotion(motion.x, player.getAIMoveSpeed(), motion.z);
                                    player.fallDistance = 0;
                                }
                            })
                            .build(),
                    new AbilityType.Builder<>(type -> new BasicCooldownAbility(type, 200))
                            .name(MetaVerse.getResource("lightning_throw"))
                            .price(10)
                            .icon("ability_lightning_throw")
                            .displayName("lightning_throw")
                            .build(),
                    new AbilityType.Builder<>(BasicAbility::new)
                            .name(MetaVerse.getResource("water_running"))
                            .price(1)
                            .icon("ability_water_running")
                            .displayName("water_running")
                            .build()
            );
        }

        private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation resourceLocation, Class<T> type) {
            return new RegistryBuilder<T>().setName(resourceLocation).setType(type).setMaxID(Integer.MAX_VALUE - 1);
        }
    }
}
