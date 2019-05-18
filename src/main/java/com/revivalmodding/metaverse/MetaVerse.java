package com.revivalmodding.metaverse;

import com.revivalmodding.metaverse.proxy.IProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = MetaVerse.MODID, name = MetaVerse.NAME, version = MetaVerse.VERSION, dependencies = "required-after:revivalcore@[0.1.4,)",updateJSON = MetaVerse.UPDATEURL)
public class MetaVerse
{
    @Mod.Instance
    public static MetaVerse instance;

    @SidedProxy(clientSide = "com.revivalmodding.metaverse.proxy.ClientProxy", serverSide = "com.revivalmodding.metaverse.proxy.ServerProxy")
    public static IProxy proxy;
    private static Logger logger;

    public static final String MODID = "metaverse";
    public static final String NAME = "Meta Verse";
    public static final String VERSION = "0.0.1";
    public static final String UPDATEURL  = "https://raw.githubusercontent.com/RevivalModdingTeam/RevivalModding-ModBugs/master/updatemeta.json";


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
}
