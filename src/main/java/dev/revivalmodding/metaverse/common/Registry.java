package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.*;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.blocks.SmallParticleAcceleratorBlock;
import dev.revivalmodding.metaverse.common.blocks.SuitMakerBlock;
import dev.revivalmodding.metaverse.common.blocks.TrailEditorBlock;
import dev.revivalmodding.metaverse.common.entity.LightningProjectile;
import dev.revivalmodding.metaverse.common.suit.BipedSuit;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.List;

public class Registry {

    // custom registries
    /** Holds all Ability types */
    public static IForgeRegistry<AbilityType<?>> ABILITY_TYPES;

    // object holders & deferred registries

    @ObjectHolder(MetaVerse.MODID)
    public static final class AbilityTypes {
        public static final AbilityType<SpeedAbility> SPEED = null;
        public static final AbilityType<BasicAbility> WALL_RUNNING = null;
        public static final AbilityType<LightningThrowAbility> LIGHTNING_THROW = null;
        public static final AbilityType<BasicAbility> WATER_RUNNING = null;
        public static final AbilityType<BasicAbility> PHASING = null;
    }

    @ObjectHolder(MetaVerse.MODID)
    public static final class EntityTypes {
        public static final EntityType<?> LIGHTNING_PROJECTILE = null;
    }

    @ObjectHolder(MetaVerse.MODID)
    public static final class MVItems {
        public static final BipedSuit TEST = null;
    }

    @ObjectHolder(MetaVerse.MODID)
    public static final class MVBlocks {
        public static final SuitMakerBlock SUIT_MAKER = null;
        public static final SmallParticleAcceleratorBlock SMALL_PARTICLE_ACCELERATOR = null;
        public static final TrailEditorBlock TRAIL_EDITOR = null;
    }

    // Common mod registry handler
    @Mod.EventBusSubscriber(modid = MetaVerse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Handler {

        public static List<BlockItem> blockItemsToRegister = new ArrayList<>();

        @SuppressWarnings("unchecked")
        @SubscribeEvent
        public static void createRegistries(RegistryEvent.NewRegistry event) {
            makeRegistry(MetaVerse.getResource("ability_type"), AbilityType.class).create();
            ABILITY_TYPES = RegistryManager.ACTIVE.getRegistry(AbilityType.class);
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new SuitMakerBlock("suit_maker"),
                    new SmallParticleAcceleratorBlock("small_particle_accelerator"),
                    new TrailEditorBlock("trail_editor")
            );
        }

        @SubscribeEvent
        public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    EntityType.Builder.create(LightningProjectile::new, EntityClassification.MISC).setTrackingRange(128).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).size(0.2F, 0.2F).build("metaverse:lightning_projectile").setRegistryName(MetaVerse.getResource("lightning_projectile"))
            );
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registerBipedSuit(event, ArmorMaterial.TURTLE, "test");
            blockItemsToRegister.forEach(registry::register);
            blockItemsToRegister = null;
        }

        public static final EquipmentSlotType[] ARMOR = {EquipmentSlotType.FEET, EquipmentSlotType.LEGS, EquipmentSlotType.CHEST, EquipmentSlotType.HEAD};

        private static void registerBipedSuit(RegistryEvent.Register<Item> event, ArmorMaterial material, String name) {
            for(EquipmentSlotType slot : ARMOR) {
                event.getRegistry().register(new BipedSuit(name, material, slot, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(name+"_"+slot.getName()));
            }
        }

        @SubscribeEvent
        public static void registerAbilityTypes(RegistryEvent.Register<AbilityType<?>> event) {
            event.getRegistry().registerAll(
                    new AbilityType.Builder<>(SpeedAbility::new)
                            .name(MetaVerse.getResource("speed"))
                            .price(3)
                            .icon("ability_speed")
                            .displayName("speed")
                            .build(),
                    new AbilityType.Builder<>(WallRunningAbility::new)
                            .name(MetaVerse.getResource("wall_running"))
                            .price(0)
                            .icon("ability_wallrunning")
                            .displayName("wall_running")
                            .setIgnoreMetapowers()
                            .build(),
                    new AbilityType.Builder<>(LightningThrowAbility::new)
                            .name(MetaVerse.getResource("lightning_throw"))
                            .price(10)
                            .icon("ability_lightning_throw")
                            .displayName("lightning_throw")
                            .setIgnoreMetapowers()
                            .build(),
                    new AbilityType.Builder<>(WaterRunningAbility::new)
                            .name(MetaVerse.getResource("water_running"))
                            .price(1)
                            .icon("ability_water_running")
                            .displayName("water_running")
                            .build(),
                    new AbilityType.Builder<>(PhasingAbility::new)
                            .name(MetaVerse.getResource("phasing"))
                            .price(6)
                            .icon("ability_phasing")
                            .displayName("phasing")
                            .setIgnoreMetapowers()
                            .build()
            );
        }

        private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation resourceLocation, Class<T> type) {
            return new RegistryBuilder<T>().setName(resourceLocation).setType(type).setMaxID(Integer.MAX_VALUE - 1);
        }
    }
}
