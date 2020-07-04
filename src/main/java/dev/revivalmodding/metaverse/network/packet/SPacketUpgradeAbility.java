package dev.revivalmodding.metaverse.network.packet;

import dev.revivalmodding.metaverse.ability.core.IAbility;
import dev.revivalmodding.metaverse.ability.interfaces.UpgradeableAbility;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import dev.revivalmodding.metaverse.network.Packet;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SPacketUpgradeAbility implements Packet<SPacketUpgradeAbility> {

    private int index;

    public SPacketUpgradeAbility() {

    }

    public SPacketUpgradeAbility(int index) {
        this.index = index;
    }

    @Override
    public void encode(SPacketUpgradeAbility sPacketUpgradeAbility, PacketBuffer buf) {
        buf.writeInt(sPacketUpgradeAbility.index);
    }

    @Override
    public SPacketUpgradeAbility decode(PacketBuffer buf) {
        return new SPacketUpgradeAbility(buf.readInt());
    }

    @Override
    public void handle(SPacketUpgradeAbility sPacketUpgradeAbility, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            PlayerDataFactory.getCapability(player).ifPresent(cap -> {
                Abilities abilities = cap.getPlayerAbilities();
                int id = sPacketUpgradeAbility.index;
                IAbility[] array = abilities.getActiveAbilities();
                if(index < 0 || index > 2) return;
                IAbility ability = array[id];
                if(ability instanceof UpgradeableAbility) {
                    UpgradeableAbility upgradeableAbility = (UpgradeableAbility) ability;
                    int price = upgradeableAbility.getUpgradeCost();
                    if(abilities.getLevel() < price) {
                        return;
                    }
                    if(upgradeableAbility.canUpgrade(player)) {
                        abilities.setLevel(abilities.getLevel() - price);
                        upgradeableAbility.upgrade();
                    }
                    cap.sync();
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }
}
