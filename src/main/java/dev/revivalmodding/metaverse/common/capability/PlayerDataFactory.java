package dev.revivalmodding.metaverse.common.capability;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.network.NetworkManager;
import dev.revivalmodding.metaverse.network.packet.CPacketCapSync;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class PlayerDataFactory implements PlayerData {

    private final PlayerEntity player;

    public static LazyOptional<PlayerData> getCapability(PlayerEntity player) {
        return player.getCapability(PlayerDataProvider.CAP);
    }

    public PlayerDataFactory() {
        this(null);
    }

    public PlayerDataFactory(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public void sync() {
        if(!player.world.isRemote) {
            NetworkManager.sendClientPacket((ServerPlayerEntity) player, new CPacketCapSync(player.getUniqueID(), this.serializeNBT()));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }

    @Mod.EventBusSubscriber(modid = MetaVerse.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof PlayerEntity) {
                event.addCapability(MetaVerse.getResource("playerdata"), new PlayerDataProvider((PlayerEntity) event.getObject()));
            }
        }

        @SubscribeEvent
        public static void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
            PlayerDataFactory.getCapability(event.getPlayer()).ifPresent(PlayerData::sync);
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            LazyOptional<PlayerData> original = PlayerDataFactory.getCapability(event.getOriginal());
            LazyOptional<PlayerData> newCap = PlayerDataFactory.getCapability(event.getPlayer());
            if(!original.isPresent() || !newCap.isPresent()) {
                return;
            }
            newCap.orElse(null).deserializeNBT(original.orElse(null).serializeNBT());
        }
    }
}
