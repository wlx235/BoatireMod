package wlx.boatire.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.block.ModBlocks;

public class ModBlockEntities {

    public static final BlockEntityType<TireChangerBlockEntity> TIRE_CHANGER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Boatire.MOD_ID, "tire_changer_block_entity"),
                    FabricBlockEntityTypeBuilder.create(TireChangerBlockEntity::new, ModBlocks.TIRE_CHANGER).build());

    public static final BlockEntityType<TimerStarterBlockEntity> TIMER_STARTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Boatire.MOD_ID, "timer_starter_block_entity"),
                    FabricBlockEntityTypeBuilder.create(TimerStarterBlockEntity::new, ModBlocks.TIMER_STARTER).build());

    public static void registerBlockEntities(){}
}
