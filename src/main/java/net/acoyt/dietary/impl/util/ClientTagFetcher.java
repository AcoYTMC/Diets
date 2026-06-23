package net.acoyt.dietary.impl.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author AcoYT
 */
@Environment(EnvType.CLIENT)
public class ClientTagFetcher implements ClientTickEvents.EndWorldTick {
    private final Map<TagKey<Item>, ArrayList<Item>> tags = new HashMap<>();

    public void reload() {
        tags.clear();
    }

    public void onEndTick(ClientWorld world) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player != null) {
            for (int i = 0; i < player.getInventory().size(); i++) {
                ItemStack itemStack = player.getInventory().getStack(i);
                itemStack.streamTags().forEach(tagKey -> {
                    ArrayList<Item> alreadyExisting = tags.getOrDefault(tagKey, new ArrayList<>());
                    alreadyExisting.add(itemStack.getItem());
                    if (tags.containsKey(tagKey)) {
                        tags.replace(tagKey, alreadyExisting);
                    } else {
                        tags.put(tagKey, alreadyExisting);
                    }
                });
            }
        }
    }

    public boolean isInTag(TagKey<Item> tagKey, ItemConvertible item) {
        return tags.getOrDefault(tagKey, new ArrayList<>()).contains(item.asItem());
    }
}
