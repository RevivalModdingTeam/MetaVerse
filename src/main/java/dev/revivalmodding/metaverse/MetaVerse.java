package dev.revivalmodding.metaverse;

import dev.revivalmodding.metaverse.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MetaVerse.MODID)
public class MetaVerse {

    public static final String MODID = "metaverse";
    public static final Logger log = LogManager.getLogger(MODID);

    public MetaVerse() {
        IEventBus mod = FMLJavaModLoadingContext.get().getModEventBus();
        mod.addListener(this::commonSetup);
        mod.addListener(this::clientSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.init();
    }

    private void clientSetup(FMLClientSetupEvent event) {

    }

    public static ResourceLocation getResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
