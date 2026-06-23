package net.acoyt.dietary.impl.cca.entity;

import net.acoyt.dietary.impl.Dietary;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

/**
 * @author AcoYT
 */
public class DietComponent implements AutoSyncedComponent, CommonTickingComponent {
    public static final ComponentKey<DietComponent> KEY = ComponentRegistry.getOrCreate(Dietary.id("diet"), DietComponent.class);
    private final PlayerEntity player;

    @Nullable private TagKey<Item> dietTag = null;

    public DietComponent(PlayerEntity player) {
        this.player = player;
    }

    public void sync() {
        KEY.sync(player);
    }

    public void tick() {
        //if (player.getWorld().getTime() % 30 == 0) {
        //    Dietary.LOGGER.info("[{}] Current tag is {}!",
        //            player.getWorld().isClient ? "Client" : "Server",
        //            dietTag != null ? dietTag.id() : "Empty");
        //}
    }

    public void readFromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (nbt.contains("DietTag", NbtElement.COMPOUND_TYPE)) {
            NbtCompound compound = nbt.getCompound("DietTag");
            dietTag = TagKey.codec(RegistryKeys.ITEM).parse(registries.getOps(NbtOps.INSTANCE), compound).resultOrPartial(Dietary.LOGGER::error).orElseThrow();
        } else {
            dietTag = null;
        }
    }

    public void writeToNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        if (dietTag != null) {
            nbt.put("DietTag", TagKey.codec(RegistryKeys.ITEM).encodeStart(registries.getOps(NbtOps.INSTANCE), dietTag).getOrThrow());
        }
    }

    public @Nullable TagKey<Item> getDietTag() {
        return dietTag;
    }

    public void setDietTag(@Nullable TagKey<Item> dietTag) {
        this.dietTag = dietTag;
        sync();
    }
}
