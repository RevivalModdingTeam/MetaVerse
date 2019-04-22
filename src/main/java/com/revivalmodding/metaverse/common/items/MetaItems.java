package com.revivalmodding.metaverse.common.items;

import com.revivalmodding.metaverse.MetaVerse;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class MetaItems {

    public static final List<Item> ITEM_LIST = new ArrayList<>();


    public static Item temp;

    public static void init() {

    }

    public static void registerRenders() {
        registerRender(temp);
    }

    public static Item registerItem(Item item, boolean tab) {
        if (tab)
            System.out.println("No Tab Defined");
        ITEM_LIST.add(item);
        return item;
    }

    public static void registerRender(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    public static void registerRenderMeta(Item item, int meta, String name) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(MetaVerse.MODID, name), "inventory"));
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(MetaItems.ITEM_LIST.toArray(new Item[0]));
    }
}
