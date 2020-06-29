package dev.revivalmodding.metaverse.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Simple interface for creating network packets
 * @param <T> - the packet object type
 */
public interface Packet<T> {

    /**
     * Encodes data to the provided {@link PacketBuffer} from {@link T} instance
     * @param t - the packet instance
     * @param buf - for writing data
     */
    void encode(T t, PacketBuffer buf);

    /**
     * Creates new {@link T} instance from data which are provided by {@link PacketBuffer}
     * @param buf - for reading data
     * @return new {@link T} instance
     */
    T decode(PacketBuffer buf);

    /**
     * Handles the data on receiving {@link net.minecraftforge.api.distmarker.Dist}
     * @param t - the packet instance
     * @param ctx - network context for running operations on correct thread
     */
    void handle(T t, Supplier<NetworkEvent.Context> ctx);
}
