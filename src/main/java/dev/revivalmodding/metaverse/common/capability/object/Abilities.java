package dev.revivalmodding.metaverse.common.capability.object;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.ability.AbilityType;
import dev.revivalmodding.metaverse.ability.IAbility;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.util.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Abilities {

    private final PlayerData data;
    private int level;
    private int xp;
    private final IAbility[] activeAbilities = new IAbility[3];
    private final List<AbilityType<?>> availableTypes = new ArrayList<>();
    private final Map<ResourceLocation, CompoundNBT> cache = new HashMap<>();

    public Abilities(PlayerData data) {
        this.data = data;
    }

    public void tick() {
        float required = getXpRequired();
        if(!data.getOwner().world.isRemote && xp >= required) {
            levelUp();
        }
        for(IAbility ability : activeAbilities) {
            if(ability != null && ability.applyAbility()) {
                ability.handleTick(data.getOwner());
            }
        }
    }

    public void livingTick() {
        for(IAbility ability : activeAbilities) {
            if(ability != null && ability.applyAbility()) {
                ability.handleLivingUpdate(data.getOwner());
            }
        }
    }

    void levelUp() {
        ++level;
        xp = 0;
        data.sync();
    }

    public void toggle(int i) {
        IAbility ability = activeAbilities[i];
        if(ability == null) return;
        ability.handleToggled(data.getOwner());
    }

    public void activate(AbilityType<?> type) {
        int id = Utils.firstNull(activeAbilities);
        if(id < 0) {
            MetaVerse.log.error("Could not activate {} ability", type.getRegistryName());
            return;
        }
        activeAbilities[id] = type.newInstance();
        CompoundNBT nbt = loadFromCache(type);
        if(nbt != null) {
            activeAbilities[id].readData(nbt);
        }
        data.sync();
    }

    public void deactivate(AbilityType<?> type) {
        for(int i = 0; i < activeAbilities.length; i++) {
            IAbility ability = activeAbilities[i];
            if(ability == null) continue;
            if(ability.getType().getRegistryName().equals(type.getRegistryName())) {
                activeAbilities[i] = null;
                ability.getType().handleDeactivated(data.getOwner());
                data.sync();
                cache(ability);
                break;
            }
        }
    }

    public void unlock(AbilityType<?> type) {
        if(!availableTypes.contains(type)) {
            cache.remove(type.getRegistryName());
            availableTypes.add(type);
            level -= type.getPrice();
            data.sync();
        }
    }

    public void lock(AbilityType<?> type) {
        deactivate(type);
        availableTypes.remove(type);
        cache.remove(type.getRegistryName());
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public IAbility[] getActiveAbilities() {
        return activeAbilities;
    }

    public List<AbilityType<?>> getAvailableTypes() {
        return availableTypes;
    }

    public int getXpRequired() {
        return 100 + (100 * level) / 2;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getActiveAbilityCount() {
        int count = 0;
        for(IAbility ability : activeAbilities) {
            if(ability != null) ++count;
        }
        return count;
    }

    public CompoundNBT write() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level);
        nbt.putInt("xp", xp);
        // active abilities
        ListNBT list = new ListNBT();
        for(int i = 0; i < activeAbilities.length; i++) {
            IAbility ability = activeAbilities[i];
            if(ability == null) continue;
            CompoundNBT tag = new CompoundNBT();
            tag.putInt("index", i);
            cache(ability);
            tag.put("ability", ability.writeData());
            list.add(tag);
        }
        nbt.put("active", list);
        // unlocked abilities
        ListNBT list2 = new ListNBT();
        for(AbilityType<?> type : availableTypes) {
            list2.add(StringNBT.valueOf(type.getRegistryName().toString()));
        }
        nbt.put("available", list2);
        // ability data cache
        CompoundNBT cacheNBT = new CompoundNBT();
        for(Map.Entry<ResourceLocation, CompoundNBT> entry : cache.entrySet()) {
            cacheNBT.put(entry.getKey().toString(), entry.getValue());
        }
        nbt.put("cache", cacheNBT);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        Utils.clear(activeAbilities);
        availableTypes.clear();
        level = nbt.getInt("level");
        xp = nbt.getInt("xp");
        // cache
        if(nbt.contains("cache")) {
            CompoundNBT cacheNBT = nbt.getCompound("cache");
            for(String string : cacheNBT.keySet()) {
                ResourceLocation key = new ResourceLocation(string);
                if(Registry.ABILITY_TYPES.containsKey(key)) {
                    cache.put(key, cacheNBT.getCompound(string));
                }
            }
        }
        ListNBT active = nbt.contains("active") ? nbt.getList("active", Constants.NBT.TAG_COMPOUND) : new ListNBT();
        for(int i = 0; i < active.size(); i++) {
            CompoundNBT compoundNBT = active.getCompound(i);
            int index = compoundNBT.getInt("index");
            CompoundNBT ability = compoundNBT.getCompound("ability");
            AbilityType<?> type = AbilityType.readFromNBT(ability);
            if(type == null) continue;
            IAbility instance = type.newInstance();
            instance.readData(ability);
            activeAbilities[index] = instance;
            CompoundNBT cachedNBT = cache.get(type.getRegistryName());
            if(cachedNBT != null) {
                activeAbilities[index].readData(cachedNBT);
            }
        }
        ListNBT available = nbt.contains("available") ? nbt.getList("available", Constants.NBT.TAG_STRING) : new ListNBT();
        for(int i = 0; i < available.size(); i++) {
            ResourceLocation location = new ResourceLocation(available.getString(i));
            AbilityType<?> type = Registry.ABILITY_TYPES.getValue(location);
            if(type == null) continue;
            availableTypes.add(type);
        }
    }

    private CompoundNBT loadFromCache(AbilityType<?> type) {
        return cache.get(type.getRegistryName());
    }

    public void cache(IAbility ability) {
        ResourceLocation key = ability.getType().getRegistryName();
        CompoundNBT data = ability.writeData();
        this.cache.put(key, data);
    }
}
