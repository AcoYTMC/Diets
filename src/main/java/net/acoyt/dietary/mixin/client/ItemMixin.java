package net.acoyt.dietary.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.acoyt.dietary.impl.DietaryClient;
import net.acoyt.dietary.impl.cca.entity.DietComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/**
 * @author AcoYT
 */
@Mixin(Item.class)
public abstract class ItemMixin {
    @WrapOperation(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z"
            )
    )
    private boolean dietary$onlyEdibleIfInTag(PlayerEntity instance, boolean ignoreHunger, Operation<Boolean> original,
                                              @Local(argsOnly = true) Hand hand) {
        ItemStack stack = instance.getStackInHand(hand);
        DietComponent component = DietComponent.KEY.get(instance);
        if (instance.getWorld().isClient) {
            return original.call(instance, ignoreHunger) && (component.getDietTag() == null || DietaryClient.FETCHER.isInTag(component.getDietTag(), stack.getItem()));
        } else {
            return original.call(instance, ignoreHunger) && isEdible(instance, instance.getStackInHand(hand));
        }
    }

    @Unique
    private boolean isEdible(Entity entity, ItemStack stack) {
        if (!(entity instanceof PlayerEntity player)) return false;
        DietComponent component = DietComponent.KEY.get(player);
        return component.getDietTag() == null || stack.isIn(component.getDietTag());
    }
}
