package wlx.boatire;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import wlx.boatire.datagen.*;

public class BoatireModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModEnUsLangProvider::new);
        pack.addProvider(ModBlockTagsProvider::new);
        pack.addProvider(ModModelsProvider::new);
        pack.addProvider(ModRecipesProvider::new);
        pack.addProvider(ModItemTagsProvider::new);
        pack.addProvider(ModLootTableProvider::new);
    }
}
