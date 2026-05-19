package wlx.boatire.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import wlx.boatire.block.ModBlocks;
import wlx.boatire.item.ModItems;

public class ModModelsProvider extends FabricModelProvider {
    public ModModelsProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        //blockStateModelGenerator.registerSimpleState(ModBlocks.TIRE_CHANGER);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.TEST_BLOCK);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.TIRE_CHANGER);
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.HIGH_F_ICE);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(ModBlocks.TIMER_STARTER);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.H1_TIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.H2_TIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.H3_TIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.H4_TIRE, Models.GENERATED);
        itemModelGenerator.register(ModItems.H5_TIRE, Models.GENERATED);

    }
}
