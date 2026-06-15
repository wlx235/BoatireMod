package wlx.boatire.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.block.custom.TestBlock;
import wlx.boatire.block.custom.TimerStarterBlock;
import wlx.boatire.block.custom.TimerStopperBlock;
import wlx.boatire.block.custom.TireChangerBlock;

public class ModBlocks {

    public static final Block TIRE_CHANGER = register("tire_changer", new TireChangerBlock(FabricBlockSettings.create().requiresTool().strength(1.5F, 6.0F)));
    public static final Block TEST_BLOCK = register("test", new TestBlock(AbstractBlock.Settings.copy(Blocks.STONE)));
    public static final Block TIMER_STARTER = register("timer_starter", new TimerStarterBlock(FabricBlockSettings.create().requiresTool().strength(1.5F, 6.0F)));
    public static final Block TIMER_STOPPER = register("timer_stopper", new TimerStopperBlock(FabricBlockSettings.create().requiresTool().strength(1.5F, 6.0F)));

    public static final Block HIGH_F_ICE = register(
            "high_f_ice",
            new Block(
                    AbstractBlock.Settings.create().mapColor(MapColor.PALE_PURPLE).instrument(Instrument.CHIME).slipperiness(0.925F).strength(0.5F).sounds(BlockSoundGroup.GLASS)
            )
    );

    public static Block register(String id, Block block) {
        registerBlockItems(id, block);
        return Registry.register(Registries.BLOCK, new Identifier(Boatire.MOD_ID, id), block);
    }

    public static void registerBlockItems(String id, Block block){
        Registry.register(Registries.ITEM, new Identifier(Boatire.MOD_ID, id),
                new BlockItem(block, new Item.Settings()));
    }
    public static void registerModBlocks(){

    }
}
