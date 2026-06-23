package net.acoyt.dietary.impl;

import com.mojang.logging.LogUtils;
import net.acoyt.dietary.impl.command.DietCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

/**
 * @author AcoYT
 */
public class Dietary implements ModInitializer {
	public static final String MOD_ID = "dietary";
	public static final Logger LOGGER = LogUtils.getLogger();

	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(DietCommand::register);
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
