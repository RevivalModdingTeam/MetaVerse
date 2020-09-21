package dev.revivalmodding.metaverse.client;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.core.IAbility;
import dev.revivalmodding.metaverse.ability.interfaces.CooldownAbility;
import dev.revivalmodding.metaverse.ability.interfaces.InfoRenderer;
import dev.revivalmodding.metaverse.ability.interfaces.LevelableAbility;
import dev.revivalmodding.metaverse.client.screen.AbilityScreen;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.init.MVAbilities;
import dev.revivalmodding.metaverse.network.NetworkManager;
import dev.revivalmodding.metaverse.network.packet.SPacketToggleAbility;
import dev.revivalmodding.metaverse.util.AbilityHelper;
import dev.revivalmodding.metaverse.util.RenderUtils;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MetaVerse.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onPlayerRenderPre(RenderPlayerEvent.Pre e) {
        e.getMatrixStack().push();
        PlayerEntity player = e.getPlayer();
        PlayerData data = PlayerDataFactory.getCapability(player).orElse(null);
        if (data != null && AbilityHelper.hasActiveAbility(MVAbilities.WALL_RUNNING, data.getPlayerAbilities())) {
            Vec3d vec0 = new Vec3d(player.getPosX(), player.getPosY() + 1, player.getPosZ());
            Vec3d vec1 = player.getLookVec();
            Vec3d vec2 = vec0.add(vec1);
            RayTraceResult rayTraceResult = player.world.rayTraceBlocks(new RayTraceContext(vec0, vec2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));
            boolean posChanged = player.prevPosY != player.getPosY();
            boolean isActuallyWallrunning = rayTraceResult != null && rayTraceResult.getType() == RayTraceResult.Type.BLOCK && posChanged;
            if (!isActuallyWallrunning) {
                return;
            }

            Direction facing = player.getHorizontalFacing();
            switch (facing) {
                case SOUTH: {
                    e.getMatrixStack().rotate(Vector3f.XP.rotation(-95));
                    break;
                }
                case NORTH: {
                    e.getMatrixStack().rotate(Vector3f.XP.rotation(95));
                    break;
                }
                case WEST: {
                    e.getMatrixStack().rotate(Vector3f.ZP.rotation(-95));
                    break;
                }
                case EAST: {
                    e.getMatrixStack().rotate(Vector3f.ZP.rotation(95));
                    break;
                }
                default:
                    break;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRenderPost(RenderPlayerEvent.Post e) {
        e.getMatrixStack().pop();
    }

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
                for(int i = 0; i < active.length; i++) {
                    IAbility ability = active[i];
                    renderAbility(ability, i, mc, event.getWindow(), event.getPartialTicks());
                }
            });
        }
    }

    private static void renderAbility(IAbility ability, int index, Minecraft mc, MainWindow window, float partialTicks) {
        boolean empty = ability == null;
        int invertedIndex = 2 - index;
        int startX = window.getScaledWidth() - 30 - invertedIndex * 30;
        int startY = 10;
        RenderUtils.renderColor(startX, startY, startX + 20, startY + 20, 0.0F, 0.0F, 0.0F, 0.5F);
        KeyBinding bind = index == 0 ? Keybinds.ability1 : index == 1 ? Keybinds.ability2 : Keybinds.ability3;
        FontRenderer renderer = mc.fontRenderer;
        String key = bind.getLocalizedName().toUpperCase();
        if(!empty) {
            boolean useable = ability.applyAbility();
            RenderUtils.renderTexture(startX + 2, startY + 2, startX + 18, startY + 18, ability.getType().getIcon());
            if(ability instanceof LevelableAbility && ((LevelableAbility) ability).shouldDisplayCurrentLevel() && ability.applyAbility()) {
                String string = ((LevelableAbility) ability).getCurrentLevel() + "";
                renderer.drawStringWithShadow(string, startX + 20 - renderer.getStringWidth(string) / 2.0F, startY + 16, 0xFFFF00);
            }
            if(ability instanceof InfoRenderer) {
                ((InfoRenderer) ability).renderExtra(mc, window, partialTicks);
            }
            if(ability instanceof CooldownAbility) {
                CooldownAbility cooldownAbility = (CooldownAbility) ability;
                RenderUtils.renderColor(startX - 1, startY + 19, startX + 21, startY + 22, 0.0F, 0.0F, 0.0F, 1.0F);
                float f = 1.0F - cooldownAbility.getCooldownProgress();
                useable = !cooldownAbility.isOnCooldown();
                RenderUtils.renderColor(startX, startY + 20, startX + (int)(20 * f), startY + 21, 0.0F, 1.0F, 0.0F, 1.0F);
            }
            renderer.drawStringWithShadow(key, startX + (20 - renderer.getStringWidth(key)) / 2.0F, startY - 6, useable ? 0x00FF00 : 0xFF0000);
        } else {
            renderer.drawStringWithShadow("?", startX + 10.5F - renderer.getStringWidth("?") / 2.0F, startY + 7, 0xFFFFFF);
            renderer.drawStringWithShadow(key, startX + (20 - renderer.getStringWidth(key)) / 2.0F, startY - 6, 0xFFFFFF);
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

    public static void onPlayerCapUpdated() {
        Minecraft mc = Minecraft.getInstance();
        if(mc.currentScreen instanceof AbilityScreen) {
            MainWindow window = mc.getMainWindow();
            mc.currentScreen.init(mc, window.getScaledWidth(), window.getScaledHeight());
        }
    }
}
