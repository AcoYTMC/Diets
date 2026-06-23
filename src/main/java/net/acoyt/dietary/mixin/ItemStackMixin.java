package net.acoyt.dietary.mixin;

import net.acoyt.dietary.impl.util.interfaces.ItemStackAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author AcoYT
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {
    @Unique private PlayerEntity dietary$stackHolder = null;

    @Inject(method = "inventoryTick", at = @At("TAIL"))
    private void dietary$saveHolder(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
        if (entity instanceof PlayerEntity player) dietary$stackHolder = player;
    }

    @Override
    public PlayerEntity dietary$getStackHolder() {
        return dietary$stackHolder;
    }
}
