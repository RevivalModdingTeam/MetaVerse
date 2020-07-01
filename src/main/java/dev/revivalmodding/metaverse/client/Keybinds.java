package dev.revivalmodding.metaverse.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class Keybinds {

    public static KeyBinding abilityScreen;
    public static KeyBinding ability1;
    public static KeyBinding ability2;
    public static KeyBinding ability3;

    public static void init() {
        abilityScreen = register("abilities", 89);
        ability1 = register("ability1", 73);
        ability2 = register("ability2", 79);
        ability3 = register("ability3", 80);
    }

    private static KeyBinding register(String key, int code) {
        KeyBinding bind = new KeyBinding(String.format("metaverse.key.%s", key), code, "metaverse.key.category");
        ClientRegistry.registerKeyBinding(bind);
        return bind;
    }
}
