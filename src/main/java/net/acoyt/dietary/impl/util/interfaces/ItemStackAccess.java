package net.acoyt.dietary.impl.util.interfaces;

import net.minecraft.entity.player.PlayerEntity;

/**
 * @author AcoYT
 */
public interface ItemStackAccess {
    default PlayerEntity dietary$getStackHolder() {
        return null;
    }
}
