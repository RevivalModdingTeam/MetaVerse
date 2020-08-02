package dev.revivalmodding.metaverse.init;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.*;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.blocks.SmallParticleAcceleratorBlock;
import dev.revivalmodding.metaverse.common.blocks.SuitMakerBlock;
import dev.revivalmodding.metaverse.common.blocks.TrailEditorBlock;
import dev.revivalmodding.metaverse.common.suit.BipedSuit;
import net.minecraft.block.Block;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Registry {

    public static List<BlockItem> blockItemsToRegister = new ArrayList<>();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new SuitMakerBlock("suit_maker"),
                new SmallParticleAcceleratorBlock("small_particle_accelerator"),
                new TrailEditorBlock("trail_editor")
        );
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registerBipedSuit(event, ArmorMaterial.TURTLE, "test");
        blockItemsToRegister.forEach(registry::register);
        blockItemsToRegister = null;
    }

    @SubscribeEvent
    public static void registerAbilities(RegistryEvent.Register<AbilityType<?>> event) {
        event.getRegistry().registerAll(
                new AbilityType.Builder<>(SpeedAbility::new).price(0).icon("ability_speed").displayName("speed").build().setRegistryName("speed"),
                new AbilityType.Builder<>(WallRunningAbility::new).price(0).icon("ability_wall_running").displayName("wall_running").setIgnoreMetapowers().build().setRegistryName("wall_running"),
                new AbilityType.Builder<>(LightningThrowAbility::new).price(10).icon("ability_lightning_throw").displayName("lightning_throw").setIgnoreMetapowers().build().setRegistryName("lightning_throw"),
                new AbilityType.Builder<>(WaterRunningAbility::new).price(1).icon("ability_water_running").displayName("water_running").build().setRegistryName("water_running"),
                new AbilityType.Builder<>(PhasingAbility::new).price(6).icon("ability_phasing").displayName("phasing").setIgnoreMetapowers().build().setRegistryName("phasing")
        );
    }

    public static final EquipmentSlotType[] ARMOR = {EquipmentSlotType.FEET, EquipmentSlotType.LEGS, EquipmentSlotType.CHEST, EquipmentSlotType.HEAD};

    private static void registerBipedSuit(RegistryEvent.Register<Item> event, ArmorMaterial material, String name) {
        for(EquipmentSlotType slot : ARMOR) {
            event.getRegistry().register(new BipedSuit(name, material, slot, new Item.Properties().group(ItemGroup.COMBAT)).setRegistryName(name+"_"+slot.getName()));
        }
    }
}
