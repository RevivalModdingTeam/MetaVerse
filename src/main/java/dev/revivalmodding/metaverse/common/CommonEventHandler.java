package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.common.suit.BasicSuit;
import dev.revivalmodding.metaverse.init.MVAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) PlayerDataFactory.getCapability(event.player).ifPresent(PlayerData::playerTick);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntity() instanceof PlayerEntity) {
            PlayerDataFactory.getCapability((PlayerEntity) event.getEntity()).ifPresent(PlayerData::livingTick);
        }
    }

    @SubscribeEvent
    public static void onChangeEquipment(LivingEquipmentChangeEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntityLiving();
            if (e.getFrom().getItem() instanceof BasicSuit) {
                BasicSuit suit = (BasicSuit) e.getFrom().getItem();
                PlayerDataFactory.getCapability(player).ifPresent(s -> {
                    for (AbilityType<?> type : suit.abilities.get()) {
                        s.getPlayerAbilities().lock(type);
                    }
                    s.sync();
                });
            }
        }
    }
}
