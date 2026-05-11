package wlx.boatire.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.entity.ModEntities;
import wlx.boatire.entity.custom.FmBoatEntity;
import wlx.boatire.item.custom.FmBoatItem;

public class ModItems {
    public static final Item H1_TIRE = register("h1_tire", new Item(new Item.Settings()));
    public static final Item H2_TIRE = register("h2_tire", new Item(new Item.Settings()));
    public static final Item H3_TIRE = register("h3_tire", new Item(new Item.Settings()));
    public static final Item H4_TIRE = register("h4_tire", new Item(new Item.Settings()));
    public static final Item H5_TIRE = register("h5_tire", new Item(new Item.Settings()));

    public static final Item FM_BOAT_ITEM = Registry.register(
            Registries.ITEM,
            new Identifier(Boatire.MOD_ID, "fm_boat_1"),
            new FmBoatItem(FmBoatEntity.FmType.OAK, new Item.Settings().maxCount(1))
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
