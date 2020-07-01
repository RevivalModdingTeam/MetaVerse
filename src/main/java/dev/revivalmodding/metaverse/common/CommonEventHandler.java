package dev.revivalmodding.metaverse.common;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.phase == TickEvent.Phase.START) PlayerDataFactory.getCapability(event.player).ifPresent(PlayerData::playerTick);
    }
}
