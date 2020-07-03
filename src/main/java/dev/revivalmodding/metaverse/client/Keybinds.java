package dev.revivalmodding.metaverse.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    public static KeyBinding abilityScreen;
    public static KeyBinding ability1;
    public static KeyBinding ability2;
    public static KeyBinding ability3;

    public static void init() {
        abilityScreen = register("abilities", GLFW.GLFW_KEY_Z);
        ability1 = register("ability1", GLFW.GLFW_KEY_I);
        ability2 = register("ability2", GLFW.GLFW_KEY_O);
        ability3 = register("ability3", GLFW.GLFW_KEY_P);
    }

    private static KeyBinding register(String key, int code) {
        KeyBinding bind = new KeyBinding(String.format("metaverse.key.%s", key), code, "metaverse.key.category");
        ClientRegistry.registerKeyBinding(bind);
        return bind;
    }
}
