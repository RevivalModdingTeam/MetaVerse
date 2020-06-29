package dev.revivalmodding.metaverse;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MetaVerse.MODID)
public class MetaVerse {

    public static final String MODID = "metaverse";

    public MetaVerse() {
        IEventBus mod = FMLJavaModLoadingContext.get().getModEventBus();
        mod.addListener(this::commonSetup);
        mod.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }

    private void clientSetup(FMLClientSetupEvent event) {

    }
}
