package wlx.boatire.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.entity.custom.FmBoatEntity;
import wlx.boatire.item.custom.FmBoatItem;

public class ModItems {
    public static final Item H1_TIRE = register("h1_tire", new Item(new Item.Settings()));
    public static final Item H2_TIRE = register("h2_tire", new Item(new Item.Settings()));
    public static final Item H3_TIRE = register("h3_tire", new Item(new Item.Settings()));
    public static final Item H4_TIRE = register("h4_tire", new Item(new Item.Settings()));
    public static final Item H5_TIRE = register("h5_tire", new Item(new Item.Settings()));

    public static final Item FM_BOAT_ITEM_OAK = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_oak"),
            new FmBoatItem(FmBoatEntity.FmType.OAK, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_WHITE = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_white"),
            new FmBoatItem(FmBoatEntity.FmType.WHITE, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_ORANGE = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_orange"),
            new FmBoatItem(FmBoatEntity.FmType.ORANGE, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_MAGENTA = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_magenta"),
            new FmBoatItem(FmBoatEntity.FmType.MAGENTA, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_LIGHT_BLUE = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_light_blue"),
            new FmBoatItem(FmBoatEntity.FmType.LIGHT_BLUE, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_YELLOW = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_yellow"),
            new FmBoatItem(FmBoatEntity.FmType.YELLOW, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_LIME = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_lime"),
            new FmBoatItem(FmBoatEntity.FmType.LIME, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_PINK = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_pink"),
            new FmBoatItem(FmBoatEntity.FmType.PINK, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_GRAY = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_gray"),
            new FmBoatItem(FmBoatEntity.FmType.GRAY, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_LIGHT_GRAY = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_light_gray"),
            new FmBoatItem(FmBoatEntity.FmType.LIGHT_GRAY, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_CYAN = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_cyan"),
            new FmBoatItem(FmBoatEntity.FmType.CYAN, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_PURPLE = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_purple"),
            new FmBoatItem(FmBoatEntity.FmType.PURPLE, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_BLUE = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_blue"),
            new FmBoatItem(FmBoatEntity.FmType.BLUE, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_BROWN = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_brown"),
            new FmBoatItem(FmBoatEntity.FmType.BROWN, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_GREEN = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_green"),
            new FmBoatItem(FmBoatEntity.FmType.GREEN, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_RED = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_red"),
            new FmBoatItem(FmBoatEntity.FmType.RED, new Item.Settings().maxCount(1))
    );
    public static final Item FM_BOAT_ITEM_BLACK = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_black"),
            new FmBoatItem(FmBoatEntity.FmType.BLACK, new Item.Settings().maxCount(1))
    );

    //public static final Item BOAT_DETECTOR = register("boat_detector", new Item(new Item.Settings()));

    public static Item register(String id, Item item) {
        return register(new Identifier(Boatire.MOD_ID, id), item);
    }

    public static Item register(Identifier id, Item item) {
        return register(RegistryKey.of(Registries.ITEM.getKey(), id), item);
    }

    public static Item register(RegistryKey<Item> key, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem)item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, key, item);
    }

    private static void addItemToItemGroup(FabricItemGroupEntries entries){
        //entries.add(TIRE_CHANGER);
        entries.add(H1_TIRE);
    }

    public static void registerItems(){
        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModItems::addItemToItemGroup);
    }
}
