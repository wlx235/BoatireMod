package wlx.boatire.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import wlx.boatire.block.ModBlocks;
import wlx.boatire.item.ModItemGroups;
import wlx.boatire.item.ModItems;

public class ModEnUsLangProvider extends FabricLanguageProvider {
    public ModEnUsLangProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.H1_TIRE, "H1 Tire");
        translationBuilder.add(ModItems.H2_TIRE, "H2 Tire");
        translationBuilder.add(ModItems.H3_TIRE, "H3 Tire");
        translationBuilder.add(ModItems.H4_TIRE, "H4 Tire");
        translationBuilder.add(ModItems.H5_TIRE, "H5 Tire");
        translationBuilder.add(ModItems.FM_BOAT_ITEM, "Formula Boat");

        translationBuilder.add(ModBlocks.TIRE_CHANGER, "Tire Changer");
        translationBuilder.add(ModBlocks.HIGH_F_ICE, "High Fraction Ice");

        translationBuilder.add(ModItemGroups.BOATIRE_GROUP, "Boatire Items");

        translationBuilder.add("hud.boatire.tctext","Tire Changing...");

    }
}
