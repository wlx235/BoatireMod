package wlx.boatire.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.block.ModBlocks;

public class ModItemGroups {

    public static final RegistryKey<ItemGroup> BOATIRE_GROUP = register("boatire_group");

    private static RegistryKey<ItemGroup> register(String id) {
        return RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(Boatire.MOD_ID, id));
    }

    public static void registerGroups(){
        Registry.register(Registries.ITEM_GROUP,
                BOATIRE_GROUP,
                ItemGroup.create(ItemGroup.Row.TOP, 7)
                        .displayName(Text.translatable("itemGroup.boatire_group"))
                        .icon(() -> new ItemStack(ModItems.H1_TIRE))
                        .entries((displayContext, entries) -> {
                            entries.add(ModItems.H1_TIRE);
                            entries.add(ModItems.H2_TIRE);
                            entries.add(ModItems.H3_TIRE);
                            entries.add(ModItems.H4_TIRE);
                            entries.add(ModItems.H5_TIRE);

                            entries.add(ModBlocks.TIRE_CHANGER);
                            entries.add(ModBlocks.TEST_BLOCK);
                            entries.add(ModBlocks.HIGH_F_ICE);
                            entries.add(ModBlocks.TIMER_STARTER);

                            entries.add(ModItems.FM_BOAT_ITEM_OAK);
                            entries.add(ModItems.FM_BOAT_ITEM_WHITE);
                            entries.add(ModItems.FM_BOAT_ITEM_ORANGE);
                            entries.add(ModItems.FM_BOAT_ITEM_MAGENTA);
                            entries.add(ModItems.FM_BOAT_ITEM_LIGHT_BLUE);
                            entries.add(ModItems.FM_BOAT_ITEM_YELLOW);
                            entries.add(ModItems.FM_BOAT_ITEM_LIME);
                            entries.add(ModItems.FM_BOAT_ITEM_PINK);
                            entries.add(ModItems.FM_BOAT_ITEM_GRAY);
                            entries.add(ModItems.FM_BOAT_ITEM_LIGHT_GRAY);
                            entries.add(ModItems.FM_BOAT_ITEM_CYAN);
                            entries.add(ModItems.FM_BOAT_ITEM_PURPLE);
                            entries.add(ModItems.FM_BOAT_ITEM_BLUE);
                            entries.add(ModItems.FM_BOAT_ITEM_BROWN);
                            entries.add(ModItems.FM_BOAT_ITEM_GREEN);
                            entries.add(ModItems.FM_BOAT_ITEM_RED);
                            entries.add(ModItems.FM_BOAT_ITEM_BLACK);
                            //entries.add(ModItems.TIRE_CHANGER);
                        }).build());

    }
}
