package dev.revivalmodding.metaverse.ability.core;

import dev.revivalmodding.metaverse.ability.BasicAbility;
import dev.revivalmodding.metaverse.ability.SpeedAbility;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;

/**
 * Base interface for ability handling
 * Every ability must implement this
 */
public interface IAbility {

    /**
     * The ability type
     * @return type
     */
    AbilityType<?> getType();

    /**
     * Save all data into nbt
     * See {@link BasicAbility#writeData()} for example
     * @return new {@link CompoundNBT} instance with data inside
     */
    CompoundNBT writeData();

    /**
     * Load data from nbt
     * See {@link BasicAbility#readData(CompoundNBT)} for example
     * @param nbt - the {@link CompoundNBT} object with saved data
     */
    void readData(CompoundNBT nbt);

    /**
     * Called every tick from {@link net.minecraftforge.event.TickEvent.PlayerTickEvent} during START {@link net.minecraftforge.event.TickEvent.Phase}
     * Called on both CLIENT and SERVER
     * @param player - ticked player
     */
    void handleTick(PlayerEntity player);

    /**
     * Called every tick from {@link net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent}
     * @param player - ticked player
     */
    void handleLivingUpdate(PlayerEntity player);

    /**
     * Called when player presses keybind to which is this ability assigned
     * Called only on SERVER side
     * @param player - the player who pressed the button
     */
    void handleToggled(PlayerEntity player);

    /**
     * Called when player deactivates/locks the ability
     * Useful for resetting some variables, for example movement speed for {@link SpeedAbility}
     * @param player - the player who deactivated this ability
     */
    void handleDeactivated(PlayerEntity player);

    /**
     * Allows ability ticking, see {@link BasicAbility#applyAbility()} for example
     * @return if this ability can tick
     */
    boolean applyAbility();
}
