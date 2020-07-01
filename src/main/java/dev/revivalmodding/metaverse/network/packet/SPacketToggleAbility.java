package dev.revivalmodding.metaverse.network.packet;

import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.network.Packet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketToggleAbility implements Packet<SPacketToggleAbility> {

    private int id;

    public SPacketToggleAbility() {

    }

    public SPacketToggleAbility(int id) {
        this.id = id;
    }

    @Override
    public void encode(SPacketToggleAbility sPacketToggleAbility, PacketBuffer buf) {
        buf.writeInt(sPacketToggleAbility.id);
    }

    @Override
    public SPacketToggleAbility decode(PacketBuffer buf) {
        return new SPacketToggleAbility(buf.readInt());
    }

    @Override
    public void handle(SPacketToggleAbility sPacketToggleAbility, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            PlayerDataFactory.getCapability(player).ifPresent(cap -> {
                Abilities abilities = cap.getPlayerAbilities();
                abilities.toggle(Math.min(2, Math.max(0, sPacketToggleAbility.id)));
                cap.sync();
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
