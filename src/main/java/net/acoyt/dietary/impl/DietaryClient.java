package net.acoyt.dietary.impl;

import net.acoyt.dietary.impl.util.ClientTagFetcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

/**
 * @author AcoYT
 */
@Environment(EnvType.CLIENT)
public class DietaryClient implements ClientModInitializer {
	public static final ClientTagFetcher FETCHER = new ClientTagFetcher();

	public void onInitializeClient() {
		ClientTickEvents.END_WORLD_TICK.register(FETCHER);
	}
}
