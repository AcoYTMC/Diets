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
				builder.add("commands.diet.add_item", "Added item %s to your diet");
				builder.add("commands.diet.clear_items", "Cleared your diet");
				builder.add("commands.diet.get_items", "Your diet consists of %s");
			}
		});
	}

	public void buildRegistry(RegistryBuilder builder) {
		//
	}
}
