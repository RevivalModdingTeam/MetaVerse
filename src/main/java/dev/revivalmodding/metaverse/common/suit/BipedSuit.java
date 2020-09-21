package dev.revivalmodding.metaverse.common.suit;

import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.client.models.SuitModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.Supplier;

public class BipedSuit extends BasicSuit {

    public BipedSuit(String modid, String name, ArmorMaterial materialIn, EquipmentSlotType slot, Supplier<List<AbilityType<?>>> abilities, Properties builder) {
        super(modid, name, materialIn, slot, abilities, builder);
    }

    @SuppressWarnings("unchecked")
    @OnlyIn(Dist.CLIENT)
    @Override
    public BipedModel<?> getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, BipedModel _default) {
        if (stack != ItemStack.EMPTY) {
            if (stack.getItem() instanceof BasicSuit) {
                SuitModel model = new SuitModel(0.1F, false);

                if(armorSlot == EquipmentSlotType.HEAD){
                    model.bipedHead.showModel = true;
                    model.bipedHeadwear.showModel = true;
                }

                if(armorSlot == EquipmentSlotType.CHEST){
                    model.bipedBody.showModel = true;
                    model.bipedBodyWear.showModel = true;
                    model.bipedLeftArmwear.showModel = true;
                    model.bipedRightArmwear.showModel = true;
                    model.bipedLeftArm.showModel = true;
                    model.bipedRightArm.showModel = true;
                }

                if(armorSlot == EquipmentSlotType.LEGS){
                    model.bipedLeftLeg.showModel = true;
                    model.bipedRightLeg.showModel = true;
                }

                if(armorSlot == EquipmentSlotType.FEET){
                    model.bipedLeftLegwear.showModel = true;
                    model.bipedRightLegwear.showModel = true;
                }
                setModelAngles(model, _default);
                return model;
            }
        }
        return null;
    }
}
