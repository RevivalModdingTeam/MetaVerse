package dev.revivalmodding.metaverse;

import com.mojang.brigadier.CommandDispatcher;
import dev.revivalmodding.metaverse.client.Keybinds;
import dev.revivalmodding.metaverse.client.render.NoopEntityRenderer;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.PlayerDataStorage;
import dev.revivalmodding.metaverse.common.command.MetaVerseCommand;
import dev.revivalmodding.metaverse.init.MVAbilities;
import dev.revivalmodding.metaverse.init.MVEntities;
import dev.revivalmodding.metaverse.init.MVTileEntities;
import dev.revivalmodding.metaverse.network.NetworkManager;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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
        MVTileEntities.TYPES.register(mod);
        MVEntities.TYPES.register(mod);
        IEventBus forge = MinecraftForge.EVENT_BUS;
        forge.addListener(this::serverStart);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NetworkManager.init();
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        Keybinds.init();
        RenderingRegistry.registerEntityRenderingHandler(MVEntities.LIGHTNING_PROJECTILE.get(), NoopEntityRenderer::new);
    }

    private void serverStart(FMLServerStartingEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getCommandDispatcher();
        MetaVerseCommand.register(commandDispatcher);
    }

    public static ResourceLocation getResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
