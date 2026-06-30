package net.acoyt.dietary.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.acoyt.dietary.impl.cca.entity.DietComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
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
    private boolean dietary$onlyEdibleIfAllowed(PlayerEntity instance, boolean ignoreHunger, Operation<Boolean> original,
                                                @Local(argsOnly = true) Hand hand) {
        ItemStack stack = instance.getStackInHand(hand);
        DietComponent component = DietComponent.KEY.get(instance);

        return original.call(instance, ignoreHunger)
                && (component.getAllowedItems().contains(stack.getRegistryEntry()) || component.getAllowedItems().isEmpty());
    }

    @WrapMethod(method = "getUseAction")
    private UseAction dietary$dontUseAnimationIfNotAllowed(ItemStack stack, Operation<UseAction> original) {
        DietComponent component = DietComponent.KEY.getNullable(stack.dietary$getStackHolder());
        if (component == null || component.getAllowedItems().contains(stack.getRegistryEntry()) || component.getAllowedItems().isEmpty()) {
            return original.call(stack);
        }

        return UseAction.NONE;
    }
}
