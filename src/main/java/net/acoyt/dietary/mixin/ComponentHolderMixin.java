package net.acoyt.dietary.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.acoyt.dietary.impl.cca.entity.DietComponent;
import net.minecraft.component.ComponentHolder;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author AcoYT
 */
@Mixin(ComponentHolder.class)
public interface ComponentHolderMixin {
    @SuppressWarnings("ConstantValue")
    @WrapMethod(method = "contains")
    private boolean dietary$notConsumable(ComponentType<?> type, Operation<Boolean> original) {
        ComponentHolder self = (ComponentHolder)this;
        if (self instanceof ItemStack stack && stack.dietary$getStackHolder() != null && type == DataComponentTypes.FOOD) {
            DietComponent component = DietComponent.KEY.get(stack.dietary$getStackHolder());
            return original.call(type) && (component.getAllowedItems().contains(stack.getRegistryEntry()) || component.getAllowedItems().isEmpty());
        }

        return original.call(type);
    }
}
