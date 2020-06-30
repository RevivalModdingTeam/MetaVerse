package dev.revivalmodding.metaverse.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {

    public static KeyBinding ABILITY_SCREEN;
    public static KeyBinding ABILITY_1;
    public static KeyBinding ABILITY_2;
    public static KeyBinding ABILITY_3;

    public static void init() {

    }

    private static KeyBinding register(String key, int code) {
        KeyBinding bind = new KeyBinding(String.format("metaverse.key.%s", key), code, "metaverse.key.category");
        ClientRegistry.registerKeyBinding(bind);
        return bind;
    }
}
