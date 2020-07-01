package dev.revivalmodding.metaverse.client;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.IAbility;
import dev.revivalmodding.metaverse.ability.interfaces.InfoRenderer;
import dev.revivalmodding.metaverse.client.screen.AbilityScreen;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.network.NetworkManager;
import dev.revivalmodding.metaverse.network.packet.SPacketToggleAbility;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        if(Keybinds.abilityScreen.isPressed()) {
            Minecraft.getInstance().displayGuiScreen(new AbilityScreen());
        } else if(Keybinds.ability1.isPressed()) {
            tryToggleAbility(0);
        } else if(Keybinds.ability2.isPressed()) {
            tryToggleAbility(1);
        } else if(Keybinds.ability3.isPressed()) {
            tryToggleAbility(2);
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerDataFactory.getCapability(mc.player).ifPresent(cap -> {
                Abilities abilities = cap.getPlayerAbilities();
                IAbility[] active = abilities.getActiveAbilities();
                for(IAbility ability : active) {
                    if(ability instanceof InfoRenderer) {
                        ((InfoRenderer) ability).renderExtra(mc, event.getWindow(), event.getPartialTicks());
                    }
                }
            });
        }
    }

    private static void tryToggleAbility(int id) {
        PlayerDataFactory.getCapability(Minecraft.getInstance().player).ifPresent(cap -> {
            Abilities abilities = cap.getPlayerAbilities();
            IAbility ability = abilities.getActiveAbilities()[id];
            if(ability != null) {
                NetworkManager.sendServerPacket(new SPacketToggleAbility(id));
            }
        });
    }
}
