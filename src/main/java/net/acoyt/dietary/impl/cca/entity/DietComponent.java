package net.acoyt.dietary.impl.cca.entity;

import net.acoyt.dietary.impl.Dietary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AcoYT
 */
public class DietComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<DietComponent> KEY = ComponentRegistry.getOrCreate(Dietary.id("diet"), DietComponent.class);
    private final PlayerEntity player;

    private final List<RegistryEntry<Item>> allowedItems = new ArrayList<>();
    private final List<RegistryEntry<Item>> toRemove = new ArrayList<>();

    public DietComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(player);
    }

    public void tick() {
        if (player.getWorld().getTime() % 30 == 0) {
            Dietary.LOGGER.info("[{}] Current diet consists of {}",
                    player.getWorld().isClient ? "Client" : "Server",
                    getAllowedItems().stream().map(RegistryEntry::getIdAsString).toList());
        }

        List<RegistryEntry<Item>> modified = allowedItems.stream().filter(entry -> !toRemove.contains(entry)).toList();
        if (modified.size() != allowedItems.size()) {
            allowedItems.clear();
            allowedItems.addAll(modified);
            sync();
        }
    }

    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        allowedItems.clear();

        NbtList itemList = nbt.getList("AllowedItems", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : itemList) {
            RegistryEntry<Item> entry = ItemStack.ITEM_CODEC.decode(registries.getOps(NbtOps.INSTANCE), element).resultOrPartial().orElseThrow().getFirst();
            allowedItems.add(entry);
        }
    }

    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList itemList = new NbtList();
        for (RegistryEntry<Item> entry : allowedItems) {
            NbtElement element = ItemStack.ITEM_CODEC.encodeStart(registries.getOps(NbtOps.INSTANCE), entry).getOrThrow();
            itemList.add(element);
        }

        nbt.put("AllowedItems", itemList);
    }

    public List<RegistryEntry<Item>> getAllowedItems() {
        return allowedItems;
    }

    public void addAllowedItem(RegistryEntry<Item> entry) {
        allowedItems.add(entry);
        sync();
    }

    public void addAllowedItems(List<RegistryEntry<Item>> entries) {
        allowedItems.addAll(entries);
        sync();
    }

    public void removeAllowedItem(RegistryEntry<Item> entry) {
        toRemove.add(entry);
        sync();
    }

    public void removeAllowedItems(List<RegistryEntry<Item>> entries) {
        toRemove.addAll(entries);
        sync();
    }

    public void setAllowedItems(List<RegistryEntry<Item>> entries) {
        allowedItems.clear();
        allowedItems.addAll(entries);
        sync();
    }

    public void clearAllowedItems() {
        allowedItems.clear();
        sync();
    }
}
