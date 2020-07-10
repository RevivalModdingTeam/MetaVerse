package dev.revivalmodding.metaverse.common.suit;

import dev.revivalmodding.metaverse.MetaVerse;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BasicSuit extends ArmorItem {

    public String name;

    public BasicSuit(String name, ArmorMaterial materialIn, EquipmentSlotType slot, Item.Properties builder) {
        super(materialIn, slot, builder);
        this.name = name;
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return MetaVerse.MODID + ":textures/suits/" + name + ".png";
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