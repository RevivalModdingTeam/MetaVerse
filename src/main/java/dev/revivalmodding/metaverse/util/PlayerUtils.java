package dev.revivalmodding.metaverse.util;

import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerUtils {

    public static int getLevel(PlayerEntity player) {
        PlayerData data = PlayerDataFactory.getCapability(player).orElse(null);
        return data != null ? data.getPlayerAbilities().getLevel() : 0;
    }
}
