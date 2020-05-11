package com.revivalmodding.metaverse;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("metaverse")
public class Metaverse {

    public static final String MODID = "metaverse";
    public static final boolean DEBUG = true; //Set to false when releases are made!
    private static final Logger LOGGER = LogManager.getLogger();

    public Metaverse() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        if(DEBUG) LOGGER.warn("Debug mode is enabled!");
        LOGGER.info(MODID+" is setup!");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        LOGGER.info(MODID+" is now setup on the client!");
    }
}
