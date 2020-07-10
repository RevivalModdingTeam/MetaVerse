package dev.revivalmodding.metaverse.common.suit;

import dev.revivalmodding.metaverse.MetaVerse;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nullable;

public  class BasicSuit extends ArmorItem implements IForgeItem {

    public static String name;

    public BasicSuit(String name, ArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builder) {
        super(materialIn, slot, builder);
        this.name = name;
    }

    @Override
    public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, BipedModel _default) {

        return null;
    }
    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return MetaVerse.MODID + ":textures/suits/" + name + ".png";
    }

    public static void setModelAngles(BipedModel model, BipedModel parent) {
        model.isChild = parent.isChild;
        model.isSitting = parent.isSitting;
        model.isSneak = parent.isSneak;
        model.rightArmPose = parent.rightArmPose;
        model.leftArmPose = parent.leftArmPose;
    }
}