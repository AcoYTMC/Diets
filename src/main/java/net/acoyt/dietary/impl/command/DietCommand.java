package net.acoyt.dietary.impl.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.acoyt.dietary.impl.cca.entity.DietComponent;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.RegistryEntryReferenceArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

/**
 * @author AcoYT
 */
public class DietCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess access, CommandManager.RegistrationEnvironment ignoredEnvironment) {
        dispatcher.register(literal("diet")
                .then(literal("add")
                        .then(argument("item", RegistryEntryReferenceArgumentType.registryEntry(access, RegistryKeys.ITEM))
                                .executes(context -> {
                                    ServerCommandSource source = context.getSource();
                                    ServerPlayerEntity player = source.getPlayerOrThrow();

                                    RegistryEntry<Item> entry = RegistryEntryReferenceArgumentType.getRegistryEntry(context, "item", RegistryKeys.ITEM);
                                    DietComponent.KEY.get(player).addAllowedItem(entry);
                                    source.sendFeedback(() -> Text.translatable("commands.diet.add_item", entry.getIdAsString()), false);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                ).then(literal("clear")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();

                            DietComponent.KEY.get(player).clearAllowedItems();
                            source.sendFeedback(() -> Text.translatable("commands.diet.clear_items"), false);

                            return Command.SINGLE_SUCCESS;
                        })
                ).then(literal("get")
                        .executes(context -> {
                            ServerCommandSource source = context.getSource();
                            ServerPlayerEntity player = source.getPlayerOrThrow();

                            List<RegistryEntry<Item>> allowedItems = DietComponent.KEY.get(player).getAllowedItems();
                            source.sendFeedback(() -> Text.translatable("commands.diet.get_items", allowedItems.stream().map(RegistryEntry::getIdAsString).toList().toString()), false);

                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }
}
