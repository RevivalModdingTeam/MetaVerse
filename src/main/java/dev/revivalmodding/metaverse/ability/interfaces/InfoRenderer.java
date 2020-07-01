package dev.revivalmodding.metaverse.ability.interfaces;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface InfoRenderer {

    @OnlyIn(Dist.CLIENT)
    void renderExtra(Minecraft minecraft, MainWindow window, float partialTicks);
}
