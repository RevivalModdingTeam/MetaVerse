package com.revivalmodding.metaverse.common.objects.items;

import com.revivalmodding.metaverse.Metaverse;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MVItems {

    public static List<Item> ITEMS = new ArrayList<>();

    private static Item register(Item item, String name) {
        item.setRegistryName(new ResourceLocation(Metaverse.MODID, name));
        ITEMS.add(item);
        return item;
    }
}
