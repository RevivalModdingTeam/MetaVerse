package dev.revivalmodding.metaverse.client.screen;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.ability.core.IAbility;
import dev.revivalmodding.metaverse.ability.interfaces.UpgradeableAbility;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.init.MVRegistries;
import dev.revivalmodding.metaverse.network.NetworkManager;
import dev.revivalmodding.metaverse.network.packet.SPacketAbilityAction;
import dev.revivalmodding.metaverse.network.packet.SPacketUpgradeAbility;
import dev.revivalmodding.metaverse.util.AbilityHelper;
import dev.revivalmodding.metaverse.util.RenderUtils;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AbilityScreen extends Screen {

    static final ResourceLocation BACKGROUND = MetaVerse.getResource("textures/ui/ability_ui.png");
    private Abilities playerAbilities;
    private int left;
    private int top;
    private final int xSize = 200;
    private final int ySize = 180;
    private int scrollOffset;
    private List<AbilityType<?>> allTypes;

    public AbilityScreen() {
        super(new StringTextComponent("Ability screen"));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void init() {
        buttons.clear();
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
        playerAbilities = PlayerDataFactory.getCapability(minecraft.player).orElseThrow(NullPointerException::new).getPlayerAbilities();
        allTypes = new ArrayList<>(playerAbilities.getAvailableTypes());
        for(int i = scrollOffset; i < scrollOffset + 5; i++) {
            if(i >= allTypes.size()) break;
            int n = i - scrollOffset;
            AbilityType<?> type = allTypes.get(i);
            IAbility ability = type.newInstance();
            AbilityButton button = new AbilityButton(left + 10, top + 50 + 25 * n, 150, 20, this, playerAbilities, type);
            addButton(button);
            if(button.state == AbilityButton.State.TO_DEACTIVATE && ability instanceof UpgradeableAbility) {
                addButton(new UpgradeAbilityButton(left + 161, top + 50 + 25 * n, 20, 20, playerAbilities, (AbilityType<? extends UpgradeableAbility>) type, button));
            }
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderUtils.renderColor(0, 0, width, height, 0, 0, 0, 0.5F);
        RenderUtils.renderTexture(left, top, left + xSize, top + ySize, 0.0F, 0.0F, 200/255F, 180/255F, BACKGROUND);
        font.drawString(minecraft.player.getName().getFormattedText(), left + 10, top + 10, 0x333333);
        RenderUtils.renderColor(left + 80, top + 10, left + 160, top + 18, 0.4F, 0.4F, 0.4F, 1.0F);
        int currentXp = playerAbilities.getXp();
        int requiredXp = playerAbilities.getXpRequired();
        int level = playerAbilities.getLevel();
        float levelProgress = currentXp / (float) requiredXp;
        RenderUtils.renderColor(left + 80, top + 10, left + 80 + (int) (80 * levelProgress), top + 18, 0.9F, 0.9F, 0.0F, 1.0F);
        String levelString = level + "";
        font.drawString(levelString, left + 77 - font.getStringWidth(levelString), top + 10, 0xFF8800);
        font.drawString(1 + level + "", left + 163, top + 10, 0xFF8800);
        String xpInfo = String.format("%d/%d XP", currentXp, requiredXp);
        font.drawString(xpInfo, left + 80 + (80 - font.getStringWidth(xpInfo)) / 2f, top + 20, 0xFFFFFF);
        renderScrollbar(left + 184, 7);
        font.drawStringWithShadow(String.format("Active: %d/3", playerAbilities.getActiveAbilityCount()), left + 10, top + 35, 0xFFFF00);
        font.drawStringWithShadow(String.format("Unlocked: %d/%d", playerAbilities.getAvailableTypes().size(), allTypes.size()), left + 90, top + 35, 0xFFFF00);
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double value) {
        int next = scrollOffset - (int) value;
        if(next >= 0 && next <= allTypes.size() - 5) {
            scrollOffset = next;
            init();
            return true;
        }
        return false;
    }

    public void renderScrollbar(int x, int width) {
        int barHeight = 4 * 25 + 20;
        RenderUtils.renderColor(x, top + 49, x + width, top + 51 + barHeight, 0.2F, 0.2F, 0.2F, 1.0F);
        double step = barHeight / (double) allTypes.size();
        double start = scrollOffset * step;
        double end = Math.min(barHeight, (scrollOffset + 5) * step);
        RenderUtils.renderColor(x + 1, (int)(top + 50 + start), x + width - 1, (int)(top + 50 + end), 0.75F, 0.75F, 0.75F, 1.0F);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if(mouseButton == 0) {
            for(Widget widget : buttons) {
                if(widget.mouseClicked(mouseX, mouseY, mouseButton)) {
                    return true;
                }
            }
        }
        return false;
    }

    static class UpgradeAbilityButton extends Button {

        static final ResourceLocation TEXTURE = MetaVerse.getResource("textures/ui/upgradebutton.png");
        final UpgradeableAbility upgradeableAbility;
        final AbilityType<? extends UpgradeableAbility> type;
        final Abilities abilities;
        final boolean canPurchase;
        final AbilityButton parent;
        final int index;

        public UpgradeAbilityButton(int x, int y, int w, int h, Abilities abilities, AbilityType<? extends UpgradeableAbility> type, AbilityButton parent) {
            super(x, y, w, h, "", UpgradeAbilityButton::pressed);
            this.abilities = abilities;
            this.type = type;
            UpgradeableAbility tmp = null;
            int iTmp = 0;
            for(int i = 0; i < abilities.getActiveAbilities().length; i++) {
                IAbility ability = abilities.getActiveAbilities()[i];
                if(ability instanceof UpgradeableAbility && ability.getType().getRegistryName().equals(type.getRegistryName())) {
                    tmp = (UpgradeableAbility) ability;
                    iTmp = i;
                    break;
                }
            }
            upgradeableAbility = tmp;
            index = iTmp;
            this.canPurchase = upgradeableAbility != null && upgradeableAbility.canUpgrade(Minecraft.getInstance().player);
            this.parent = parent;
        }

        @Override
        public void render(int mouseX, int mouseY, float partialTicks) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            int color = parent.state.getColor().getValue(hovered);
            float alpha = ((color >> 24) & 0xff) / 255.0F;
            float red = ((color >> 16) & 0xff) / 255.0F;
            float green = ((color >> 8) & 0xff) / 255.0F;
            float blue = (color & 0xff) / 255.0F;
            RenderUtils.renderColoredTexture(x, y, x + width, y + height, red, green, blue, alpha, TEXTURE);
            FontRenderer renderer = Minecraft.getInstance().fontRenderer;
            String level = upgradeableAbility.getMaxLevel() + "";
            if(upgradeableAbility.canUpgrade(Minecraft.getInstance().player)) {
                long current = System.currentTimeMillis();
                long l = current % 2000L;
                boolean flag = l < 1000L;
                float variableColor = flag ? 1.0F * (l / (float) 1000L) : 1.0F - 1.0F * ((l - 1000L) / (float) 1000L);
                renderer.drawStringWithShadow(level, x + (20 - renderer.getStringWidth(level)) / 2.0F, y + 6, 0xFFFF00 | (int) (variableColor * 255));
            } else if(upgradeableAbility.isMaxedOut()) {
                renderer.drawStringWithShadow("MAX", x + (20 - renderer.getStringWidth("MAX")) / 2.0F, y + 6, 0xffffff);
            } else {
                renderer.drawString(level, x + (20 - renderer.getStringWidth(level)) / 2.0f, y + 6, 0x565656);
            }
        }

        static void pressed(Button button) {
            UpgradeAbilityButton abilityButton = (UpgradeAbilityButton) button;
            if(abilityButton.canPurchase) {
                NetworkManager.sendServerPacket(new SPacketUpgradeAbility(abilityButton.index));
            }
        }
    }

    static class AbilityButton extends Button {

        static final ResourceLocation TEXTURE = MetaVerse.getResource("textures/ui/abilitybutton.png");
        final AbilityScreen parentScreen;
        final Abilities abilities;
        final AbilityType<?> heldType;
        State state;

        AbilityButton(int x, int y, int w, int h, AbilityScreen screen, Abilities abilities, AbilityType<?> heldType) {
            super(x, y, w, h, "", AbilityButton::onPressed);
            this.parentScreen = screen;
            this.abilities = abilities;
            this.heldType = heldType;
            this.state = State.get(heldType, abilities);
        }

        @Override
        public void render(int mouseX, int mouseY, float partialTicks) {
            boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
            int color = state.getColor().getValue(hovered);
            float alpha = ((color >> 24) & 0xff) / 255.0F;
            float red = ((color >> 16) & 0xff) / 255.0F;
            float green = ((color >> 8) & 0xff) / 255.0F;
            float blue = (color & 0xff) / 255.0F;
            RenderUtils.renderColoredTexture(x, y, x + width, y + height, red, green, blue, alpha, TEXTURE);
            ResourceLocation icon = heldType.getIcon();
            RenderUtils.renderTexture(x + 2, y + 2, x + 18, y + 18, icon);
            FontRenderer renderer = Minecraft.getInstance().fontRenderer;
            ITextComponent component = heldType.getDisplayName();
            renderer.drawStringWithShadow(component.getFormattedText(), x + 20, y + 6, hovered ? 0xFFFFFF00 : 0xFFFFFFFF);
            if(state == State.UNAVAILABLE || state == State.UNLOCKABLE) {
                int price = heldType.getPrice();
                renderer.drawStringWithShadow("" + price, x + width - 20, y + 6, 0xFFFF00);
            }
            state = State.get(heldType, abilities);
        }

        static void onPressed(Button button) {
            AbilityButton btn = (AbilityButton) button;
            switch (btn.state) {
                case UNLOCKABLE: {
                    NetworkManager.sendServerPacket(new SPacketAbilityAction(SPacketAbilityAction.ActionType.PURCHASE, btn.heldType));
                    break;
                }
                case TO_ACTIVATE: {
                    NetworkManager.sendServerPacket(new SPacketAbilityAction(SPacketAbilityAction.ActionType.ACTIVATE, btn.heldType));
                    break;
                }
                case TO_DEACTIVATE: {
                    NetworkManager.sendServerPacket(new SPacketAbilityAction(SPacketAbilityAction.ActionType.DEACTIVATE, btn.heldType));
                    break;
                }
                default: break;
            }
        }

        enum State {
            UNAVAILABLE(new HoveredColor(0xFF222222)),
            UNLOCKABLE(new HoveredColor(0xFF55FF55, 0xFF00FF00)),
            TO_ACTIVATE(new HoveredColor(0xFFBBBB00, 0xFFFFFF00)),
            TO_DEACTIVATE(new HoveredColor(0xFFFF3333, 0xFFFF0000));

            final HoveredColor color;

            State(HoveredColor color) {
                this.color = color;
            }

            static State get(AbilityType<?> type, Abilities abilities) {
                if(AbilityHelper.hasActiveAbility(type, abilities)) {
                    return TO_DEACTIVATE;
                } else if(Utils.contains(type, abilities.getAvailableTypes(), (t1, t2) -> t1 == t2)) {
                    if(type.canActivate(Minecraft.getInstance().player)) {
                        return TO_ACTIVATE;
                    } else return UNAVAILABLE;
                } else {
                    int price = type.getPrice();
                    int currency = abilities.getLevel();
                    return price <= currency ? UNLOCKABLE : UNAVAILABLE;
                }
            }

            public HoveredColor getColor() {
                return color;
            }
        }

        static class HoveredColor {
            final int hovered, normal;

            HoveredColor(int singleColor) {
                this(singleColor, singleColor);
            }

            HoveredColor(int hovered, int normal) {
                this.hovered = hovered;
                this.normal = normal;
            }

            int getValue(boolean f) {
                return f ? hovered : normal;
            }
        }
    }
}
