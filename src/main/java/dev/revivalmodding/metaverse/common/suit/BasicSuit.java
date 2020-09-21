package dev.revivalmodding.metaverse.common.suit;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.client.ClientEventHandler;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.init.MVAbilities;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

import static dev.revivalmodding.metaverse.init.Registry.ARMOR;

public class BasicSuit extends ArmorItem {

    public String modid;
    public String name;
    public Supplier<List<AbilityType<?>>> abilities;

    public BasicSuit(String modid, String name, ArmorMaterial materialIn, EquipmentSlotType slot, Supplier<List<AbilityType<?>>> abilities, Item.Properties builder) {
        super(materialIn, slot, builder);
        this.name = name;
        this.modid = modid;
        this.abilities = abilities;
    }

    @Override
    public void onArmorTick(ItemStack item, World world, PlayerEntity entity) {
        PlayerDataFactory.getCapability(entity).ifPresent(s -> {
            for (AbilityType<?> type : abilities.get()) {
                s.getPlayerAbilities().unlock(type);
            }
            s.sync();
        });
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return modid + ":textures/suits/" + name + ".png";
    }

    @OnlyIn(Dist.CLIENT)
    public static void setModelAngles(BipedModel<?> model, BipedModel<?> parent) {
        model.isChild = parent.isChild;
        model.isSitting = parent.isSitting;
        model.isSneak = parent.isSneak;
        model.rightArmPose = parent.rightArmPose;
        model.leftArmPose = parent.leftArmPose;
    }
}