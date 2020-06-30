package dev.revivalmodding.metaverse.ability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public abstract class Ability {

    private AbilityType<?> type;

    public Ability(AbilityType<?> type) {
        this.type = type;
    }

    protected abstract CompoundNBT writeData();

    protected abstract void readData(CompoundNBT nbt);

    public void onUpdate(PlayerEntity player) {
        this.type.onUpdate(this, player);
    }

    public void toggle(PlayerEntity player) {
        this.type.onToggled(this, player);
    }

    public AbilityType<?> getType() {
        return type;
    }

    public final CompoundNBT writeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        type.saveToNBT(nbt);
        nbt.put("customData", writeData());
        return nbt;
    }

    public final void readNBT(CompoundNBT nbt) {
        type = AbilityType.readFromNBT(nbt);
        readData(nbt.contains("customData") ? nbt.getCompound("customData") : new CompoundNBT());
    }

    public static <T extends Ability & LevelableAbility> void toggleBetween(T ability, PlayerEntity player) {
        int i = player.isSneaking() ? -1 : 1;
        int next = ability.getCurrentLevel() + i;
        if(next >= 0 && next <= ability.getMaxLevel()) {
            ability.setLevel(next);
        }
    }
}
