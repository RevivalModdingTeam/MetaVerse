package dev.revivalmodding.metaverse.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.revivalmodding.metaverse.ability.core.AbilityType;
import dev.revivalmodding.metaverse.common.Registry;
import dev.revivalmodding.metaverse.common.capability.PlayerData;
import dev.revivalmodding.metaverse.common.capability.PlayerDataFactory;
import dev.revivalmodding.metaverse.common.capability.object.Abilities;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ResourceLocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.stream.Stream;

public class MetaVerseCommand {

    static final DynamicCommandExceptionType INVALID_ABILITY = new DynamicCommandExceptionType(obj -> new StringTextComponent("Unknown ability type: " + obj));
    static final SimpleCommandExceptionType ALREADY_UNLOCKED = new SimpleCommandExceptionType(new StringTextComponent("This ability is already unlocked"));
    static final SimpleCommandExceptionType ALREADY_LOCKED = new SimpleCommandExceptionType(new StringTextComponent("This ability is already locked"));
    static final DynamicCommandExceptionType INVALID_SENDER = new DynamicCommandExceptionType(obj -> new StringTextComponent("Invalid command sender - " + obj.getClass().getName()));
    static final DynamicCommandExceptionType NO_DATA = new DynamicCommandExceptionType(obj -> new StringTextComponent("Couldn't get data for " + ((PlayerEntity) obj).getName()));
    static final SuggestionProvider<CommandSource> SUGGESTIONS_ALL = (ctx, builder) -> ISuggestionProvider.func_212476_a(Registry.ABILITY_TYPES.getValues().stream().map(IForgeRegistryEntry::getRegistryName), builder);
    static final SuggestionProvider<CommandSource> SUGGESTIONS_ACTIVE = (ctx, builder) -> {
        Entity entity = ctx.getSource().getEntity();
        if(entity instanceof PlayerEntity) {
            LazyOptional<PlayerData> lazyOptional = PlayerDataFactory.getCapability((PlayerEntity) entity);
            if(lazyOptional.isPresent()) {
                PlayerData data = lazyOptional.orElse(null);
                return ISuggestionProvider.func_212476_a(data.getPlayerAbilities().getAvailableTypes().stream().map(IForgeRegistryEntry::getRegistryName), builder);
            }
        }
        return ISuggestionProvider.func_212476_a(Stream.empty(), builder);
    };

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal("metaverse")
                        .requires(src -> src.hasPermissionLevel(2))
                        .then(Commands.literal("level").executes(ctx -> errorMsg(ctx, "You must specify level value"))
                                .then(Commands.argument("level", IntegerArgumentType.integer(0, 999)).executes(ctx -> setLevel(ctx, IntegerArgumentType.getInteger(ctx, "level"))))
                        )
                        .then(Commands.literal("ability").executes(ctx -> errorMsg(ctx, "You must specify action - lock/unlock"))
                                .then(Commands.literal("unlock").executes(ctx -> errorMsg(ctx, "You must specify the ability"))
                                        .then(Commands.argument("ability", ResourceLocationArgument.resourceLocation()).suggests(SUGGESTIONS_ALL).executes(ctx -> manageAbility(ctx, ResourceLocationArgument.getResourceLocation(ctx, "ability"), false)))
                                ).then(Commands.literal("lock").executes(ctx -> errorMsg(ctx, "You must specify the ability"))
                                        .then(Commands.argument("ability", ResourceLocationArgument.resourceLocation()).suggests(SUGGESTIONS_ACTIVE).executes(ctx -> manageAbility(ctx, ResourceLocationArgument.getResourceLocation(ctx, "ability"), true)))
                                )
                        )
                        .executes(ctx -> errorMsg(ctx, "Unknown operation"))
        );
    }

    private static int manageAbility(CommandContext<CommandSource> ctx, ResourceLocation ability, boolean lock) throws CommandSyntaxException {
        AbilityType<?> type = Registry.ABILITY_TYPES.getValue(ability);
        if(type == null) {
            throw INVALID_ABILITY.create(ability);
        }
        Entity entity = ctx.getSource().getEntity();
        if(!(entity instanceof PlayerEntity)) {
            throw INVALID_SENDER.create(entity);
        }
        PlayerData playerData = PlayerDataFactory.getCapability((PlayerEntity) entity).orElseThrow(() -> NO_DATA.create(entity));
        Abilities abilities = playerData.getPlayerAbilities();
        if(lock) {
            if(!abilities.getAvailableTypes().contains(type)) {
                throw ALREADY_LOCKED.create();
            }
            abilities.lock(type);
            ctx.getSource().sendFeedback(new StringTextComponent("Sucessfully locked " + type.getDisplayName().getFormattedText() + " ability"), true);
        } else {
            if(abilities.getAvailableTypes().contains(type)) {
                throw ALREADY_UNLOCKED.create();
            }
            abilities.unlock(type);
            ctx.getSource().sendFeedback(new StringTextComponent("Sucessfully unlocked " + type.getDisplayName().getFormattedText() + " ability"), true);
        }
        playerData.sync();
        return 0;
    }

    private static int setLevel(CommandContext<CommandSource> ctx, int level) {
        Entity entity = ctx.getSource().getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            PlayerDataFactory.getCapability(player).ifPresent(cap -> {
                Abilities abilities = cap.getPlayerAbilities();
                abilities.setLevel(level);
                cap.sync();
                ctx.getSource().sendFeedback(new StringTextComponent("You have set your level to " + level), false);
            });
        }
        return level;
    }

    private static int errorMsg(CommandContext<CommandSource> ctx, String error) {
        ctx.getSource().sendFeedback(new StringTextComponent(TextFormatting.RED + error), false);
        return 0;
    }
}
