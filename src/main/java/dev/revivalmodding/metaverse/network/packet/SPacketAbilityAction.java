package dev.revivalmodding.metaverse.network.packet;

import dev.revivalmodding.metaverse.ability.AbilityType;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.network.Packet;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketAbilityAction implements Packet<SPacketAbilityAction> {

    private ActionType actionType;
    private AbilityType<?> abilityType;

    public SPacketAbilityAction() {

    }

    public SPacketAbilityAction(ActionType actionType, AbilityType<?> abilityType) {
        this.actionType = actionType;
        this.abilityType = abilityType;
    }

    @Override
    public void encode(SPacketAbilityAction sPacketAbilityAction, PacketBuffer buf) {
        buf.writeEnumValue(sPacketAbilityAction.actionType);
        buf.writeResourceLocation(sPacketAbilityAction.abilityType.getRegistryName());
    }

    @Override
    public SPacketAbilityAction decode(PacketBuffer buf) {
        return new SPacketAbilityAction(buf.readEnumValue(ActionType.class), Registry.ABILITY_TYPES.getValue(buf.readResourceLocation()));
    }

    @Override
    public void handle(SPacketAbilityAction sPacketAbilityAction, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            AbilityType<?> type = sPacketAbilityAction.abilityType;
            if(type == null) return;
            PlayerDataFactory.getCapability(player).ifPresent(data -> {
                Abilities abilities = data.getPlayerAbilities();
                switch (sPacketAbilityAction.actionType) {
                    case PURCHASE: {
                        int level = abilities.getLevel();
                        int required = type.getPrice();
                        if(level >= required) {
                            abilities.unlock(type);
                        }
                        break;
                    }
                    case ACTIVATE: {
                        int i = Utils.firstNull(abilities.getActiveAbilities());
                        if(i >= 0 && !Utils.contains(type, abilities.getActiveAbilities(), (t, a) -> a != null && a.getType().getRegistryName().equals(t.getRegistryName()))) {
                            abilities.activate(type);
                        }
                        break;
                    }
                    case DEACTIVATE: {
                        abilities.deactivate(type);
                        break;
                    }
                }
                data.sync();
            });
        });
        ctx.get().setPacketHandled(true);
    }

    public enum ActionType {
        PURCHASE,
        ACTIVATE,
        DEACTIVATE
    }
}
