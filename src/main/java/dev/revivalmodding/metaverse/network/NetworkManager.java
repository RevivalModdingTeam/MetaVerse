package dev.revivalmodding.metaverse.network;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.network.packet.CPacketCapSync;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.function.Predicate;

/**
 * Manages all client -> server and server -> client communications
 */
public class NetworkManager {

    private static final String protocolVersion = "metaverse-main";
    private static final SimpleChannel networkChannel = NetworkRegistry.ChannelBuilder
            .named(MetaVerse.getResource("network"))
            .networkProtocolVersion(() -> protocolVersion)
            .clientAcceptedVersions(protocolVersion::equals)
            .serverAcceptedVersions(protocolVersion::equals)
            .simpleChannel();
    private static int index = -1;

    public static void init() {
        register(CPacketCapSync.class, new CPacketCapSync());
    }

    /**
     * Sends the packet to specified player
     * @param player - the receiver of the packet
     * @param packet - the packet to be sent
     */
    public static void sendClientPacket(ServerPlayerEntity player, Packet<?> packet) {
        networkChannel.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    /**
     * Sends the packet to all players currently active on this {@link MinecraftServer} instance
     * @param packet - the packet to be sent
     * @param condition - player filter (when you want to target only specific player, for example in one dimension)
     */
    public static void sendToAllClients(Packet<?> packet, Predicate<ServerPlayerEntity> condition) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().stream().filter(condition).forEach(serverPlayer -> sendClientPacket(serverPlayer, packet));
    }

    /**
     * Same as {@link NetworkManager#sendToAllClients(Packet, Predicate)}
     * but is sent to ALL players
     * @param packet - the packet to be sent
     */
    public static void sendToAllClients(Packet<?> packet) {
        sendToAllClients(packet, player -> true);
    }

    /**
     * Sends packet from this Minecraft client to server
     * @param packet - the packet to be sent
     */
    public static void sendServerPacket(Packet<?> packet) {
        networkChannel.sendToServer(packet);
    }

    private static <T> void register(Class<T> tClass, Packet<T> packet) {
        networkChannel.registerMessage(++index, tClass, packet::encode, packet::decode, packet::handle);
    }
}
