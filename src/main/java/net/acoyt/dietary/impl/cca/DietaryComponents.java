package net.acoyt.dietary.impl.cca;

import net.acoyt.dietary.impl.cca.entity.DietComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

/**
 * @author AcoYT
 */
public class DietaryComponents implements EntityComponentInitializer {
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(DietComponent.KEY, DietComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}
