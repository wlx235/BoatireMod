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
        translationBuilder.add(ModItems.FM_BOAT_ITEM_OAK, "Formula Boat");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_WHITE, "Formula Boat(White)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_ORANGE, "Formula Boat (Orange)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_MAGENTA, "Formula Boat (Magenta)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_LIGHT_BLUE, "Formula Boat (Light Blue)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_YELLOW, "Formula Boat (Yellow)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_LIME, "Formula Boat (Lime)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_PINK, "Formula Boat (Pink)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_GRAY, "Formula Boat (Gray)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_LIGHT_GRAY, "Formula Boat (Light Gray)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_CYAN, "Formula Boat (Cyan)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_PURPLE, "Formula Boat (Purple)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_BLUE, "Formula Boat (Blue)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_BROWN, "Formula Boat (Brown)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_GREEN, "Formula Boat (Green)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_RED, "Formula Boat (Red)");
        translationBuilder.add(ModItems.FM_BOAT_ITEM_BLACK, "Formula Boat (Black)");

        translationBuilder.add(ModBlocks.TIRE_CHANGER, "Tire Changer");
        translationBuilder.add(ModBlocks.HIGH_F_ICE, "High Fraction Ice");
        translationBuilder.add(ModBlocks.TIMER_STARTER, "BoatTimer Starter");
        translationBuilder.add(ModBlocks.TIMER_STOPPER, "BoatTimer Stopper");

        translationBuilder.add(ModItemGroups.BOATIRE_GROUP, "Boatire Items");

        translationBuilder.add("hud.boatire.tctext","Tire Changing...");
        translationBuilder.add("text.autoconfig.boatire.title", "Boatire Settings");
        translationBuilder.add("text.autoconfig.boatire.option.showSpeedHud", "Enable Speed Hud");
        translationBuilder.add("text.autoconfig.boatire.option.showTimerHud", "Enable Timer Hud");

        translationBuilder.add("hud.boatire.tcblock.text", "Tire Changer");
        translationBuilder.add("hud.boatire.timerstarter.title", "Starter");
        translationBuilder.add("hud.boatire.timerstopper.title", "Stopper");

    }
}
