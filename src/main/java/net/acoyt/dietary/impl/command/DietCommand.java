package net.acoyt.dietary.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.acoyt.dietary.impl.cca.entity.DietComponent;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryPredicateArgumentType;
import net.minecraft.command.argument.RegistryPredicateArgumentType.RegistryPredicate;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * @author AcoYT
 */
public class DietCommand {
    private static final DynamicCommandExceptionType INVALID_TAG_EXCEPTION = new DynamicCommandExceptionType(
            id -> Text.stringifiedTranslatable("commands.diet.invalid_tag", id)
    );

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess ignoredAccess, CommandManager.RegistrationEnvironment ignoredEnvironment) {
        dispatcher.register(literal("diet")
                .then(literal("set")
                        .then(argument("tag", RegistryPredicateArgumentType.registryPredicate(RegistryKeys.ITEM))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayerOrThrow();

                                    RegistryPredicate<Item> predicate = RegistryPredicateArgumentType.getPredicate(
                                            context, "tag", RegistryKeys.ITEM, INVALID_TAG_EXCEPTION);

                                    predicate.getKey().ifRight(tag -> {
                                        DietComponent.KEY.get(player).setDietTag(tag);
                                        source.sendFeedback(() -> Text.translatable("commands.diet.set_tag", "#" + tag.id()), false);
                                    });

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                ).then(literal("clear")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();

                            DietComponent.KEY.get(player).setDietTag(null);
                            source.sendFeedback(() -> Text.translatable("commands.diet.clear_tag"), false);

                            return Command.SINGLE_SUCCESS;
                        })
                ).then(literal("get")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();

                            TagKey<Item> itemTag = DietComponent.KEY.get(player).getDietTag();
                            source.sendFeedback(() -> Text.translatable("commands.diet.get_tag", itemTag != null
                                    ? "\"#" + itemTag.id().toString() + "\"" // eg. "#minecraft:wool"
                                    : "empty"
                            ), false);

                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
