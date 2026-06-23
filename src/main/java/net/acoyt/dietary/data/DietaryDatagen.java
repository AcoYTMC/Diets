package net.acoyt.dietary.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;

/**
 * @author AcoYT
 */
public class DietaryDatagen implements DataGeneratorEntrypoint {
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider((d, c) -> new FabricLanguageProvider(d, c) {
			public void generateTranslations(RegistryWrapper.WrapperLookup registries, TranslationBuilder builder) {
				builder.add("commands.diet.invalid_tag", "There are no tags named \"%s\"");
				builder.add("commands.diet.set_tag", "Set your dietary tag to \"%s\"");
				builder.add("commands.diet.clear_tag", "Cleared your dietary tag");
				builder.add("commands.diet.get_tag", "Your dietary tag is %s");
			}
		});
	}

	public void buildRegistry(RegistryBuilder builder) {
		//
	}
}
