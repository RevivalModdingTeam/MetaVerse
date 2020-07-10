package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.LightningThrowAbility;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.BasicAbility;
import dev.revivalmodding.metaverse.ability.SpeedAbility;
import dev.revivalmodding.metaverse.common.entity.LightningProjectile;
import dev.revivalmodding.metaverse.common.suit.BipedSuit;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tags.FluidTags;
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

    @ObjectHolder(MetaVerse.MODID)
    public static final class EntityTypes {
        public static final EntityType<?> LIGHTNING_PROJECTILE = null;
    }

    @ObjectHolder(MetaVerse.MODID)
    public static final class Items {
    }

    @ObjectHolder(MetaVerse.MODID)
    public static final class Suit {
        public static final BipedSuit TEST = null;
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

        }

        @SubscribeEvent
        public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().registerAll(
                    EntityType.Builder.create(LightningProjectile::new, EntityClassification.MISC).setTrackingRange(128).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true).size(0.2F, 0.2F).build("metaverse:lightning_projectile").setRegistryName(MetaVerse.getResource("lightning_projectile"))
            );
        }

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
        }

        @SubscribeEvent
        public static void registerSuits(RegistryEvent.Register<Item> event) {
            registerBipedSuit(event, ArmorMaterial.TURTLE, "test");

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
                    new AbilityType.Builder<>(LightningThrowAbility::new)
                            .name(MetaVerse.getResource("lightning_throw"))
                            .price(10)
                            .icon("ability_lightning_throw")
                            .displayName("lightning_throw")
                            .onToggled((ability, player) -> LightningProjectile.shoot(player.world, player, 3 + ability.getCurrentLevel()))
                            .build(),
                    new AbilityType.Builder<>(BasicAbility::new)
                            .name(MetaVerse.getResource("water_running"))
                            .price(1)
                            .icon("ability_water_running")
                            .displayName("water_running")
                            .onUpdate((ability, player) -> {
                                if(player.isSprinting() && !player.isInWater() && player.world.getFluidState(player.getPosition().add(0, -0.1, 0)).isTagged(FluidTags.WATER)) {
                                    Vec3d vec3d = player.getMotion();
                                    player.setMotion(vec3d.x, 0, vec3d.z);
                                    player.fallDistance = 0.0F;
                                    player.onGround = true;
                                }
                            })
                            .build(),
                    new AbilityType.Builder<>(BasicAbility::new)
                            .name(MetaVerse.getResource("phasing"))
                            .price(6)
                            .icon("ability_phasing")
                            .displayName("phasing")
                            .onLivingUpdate((ability, player) -> {
                                if(player.world.getBlockState(player.getPosition().add(0, -0.1, 0)).getMaterial().isSolid()) {
                                    player.noClip = true;
                                    Vec3d vec3d = player.getMotion();
                                    player.setMotion(vec3d.x, 0, vec3d.z);
                                    player.onGround = true;
                                }
                            })
                            .build()
            );
        }

        private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation resourceLocation, Class<T> type) {
            return new RegistryBuilder<T>().setName(resourceLocation).setType(type).setMaxID(Integer.MAX_VALUE - 1);
        }
    }
}
