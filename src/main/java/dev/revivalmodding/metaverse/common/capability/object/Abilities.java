package dev.revivalmodding.metaverse.common.capability.object;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.Ability;
import dev.revivalmodding.metaverse.ability.AbilityType;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class Abilities {

    private final PlayerData data;
    private int level;
    private float xp;
    private final Ability[] activeAbilities = new Ability[3];
    private final List<AbilityType<?>> availableTypes = new ArrayList<>();

    public Abilities(PlayerData data) {
        this.data = data;
    }

    public void activate(AbilityType<?> type) {
        int id = Utils.firstNull(activeAbilities);
        if(id < 0) {
            MetaVerse.log.error("Could not activate {} ability", type.getRegistryName());
            return;
        }
        activeAbilities[id] = type.newInstance();
        data.sync();
    }

    public void deactivate(AbilityType<?> type) {
        for(int i = 0; i < activeAbilities.length; i++) {
            Ability ability = activeAbilities[i];
            if(ability == null) continue;
            if(ability.getType().getRegistryName().equals(type.getRegistryName())) {
                activeAbilities[i] = null;
                data.sync();
            }
        }
    }

    public void unlock(AbilityType<?> type) {
        if(!availableTypes.contains(type)) {
            availableTypes.add(type);
            level -= type.getPrice();
            data.sync();
        }
    }

    public int getLevel() {
        return level;
    }

    public float getXp() {
        return xp;
    }

    public Ability[] getActiveAbilities() {
        return activeAbilities;
    }

    public List<AbilityType<?>> getAvailableTypes() {
        return availableTypes;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level);
        nbt.putFloat("xp", xp);
        ListNBT list = new ListNBT();
        for(int i = 0; i < activeAbilities.length; i++) {
            Ability ability = activeAbilities[i];
            if(ability == null) continue;
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("index", i);
            tag.put("ability", ability.writeNBT());
            list.add(tag);
        }
        nbt.put("active", list);
        ListNBT list2 = new ListNBT();
        for(AbilityType<?> type : availableTypes) {
            list2.add(StringNBT.valueOf(type.getRegistryName().toString()));
        }
        nbt.put("available", list2);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        Utils.clear(activeAbilities);
        availableTypes.clear();
        level = nbt.getInt("level");
        xp = nbt.getFloat("xp");
        ListNBT active = nbt.contains("active") ? nbt.getList("active", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        for(int i = 0; i < active.size(); i++) {
            CompoundNBT compoundNBT = active.getCompound(i);
            int index = compoundNBT.getInt("index");
            CompoundNBT ability = compoundNBT.getCompound("ability");
            AbilityType<?> type = AbilityType.readFromNBT(ability);
            Ability instance = type.newInstance();
            instance.readNBT(ability);
            activeAbilities[index] = instance;
        }
        ListNBT available = nbt.contains("available") ? nbt.getList("available", Constants.NBT.TAG_STRING) : new ListNBT();
        for(int i = 0; i < available.size(); i++) {
            ResourceLocation location = new ResourceLocation(available.getString(i));
            AbilityType<?> type = Registry.ABILITY_TYPES.getValue(location);
            if(type == null) continue;
            availableTypes.add(type);
        }
    }
}
