package dev.revivalmodding.metaverse.network.packet;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.network.Packet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CPacketCapSync implements Packet<CPacketCapSync> {

    private UUID playerUUID;
    private CompoundNBT data;

    public CPacketCapSync() {}

    public CPacketCapSync(UUID playerUUID, CompoundNBT data) {
        this.playerUUID = playerUUID;
        this.data = data;
    }

    @Override
    public void encode(CPacketCapSync cPacketCapSync, PacketBuffer buf) {
        buf.writeUniqueId(cPacketCapSync.playerUUID);
        buf.writeCompoundTag(cPacketCapSync.data);
    }

    @Override
    public CPacketCapSync decode(PacketBuffer buf) {
        return new CPacketCapSync(buf.readUniqueId(), buf.readCompoundTag());
    }

    @Override
    public void handle(CPacketCapSync cPacketCapSync, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientWorld world = Minecraft.getInstance().world;
            PlayerEntity player = world.getPlayerByUuid(cPacketCapSync.playerUUID);
            if(player == null) {
                MetaVerse.log.error("Couldn't sync data for player, UUID: {}", cPacketCapSync.playerUUID);
                return;
            }
            PlayerDataFactory.getCapability(Minecraft.getInstance().player).ifPresent(data -> data.deserializeNBT(cPacketCapSync.data));
        });
        ctx.get().setPacketHandled(true);
    }
}
