package wlx.boatire.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.entity.custom.FmBoatEntity;


public class ModEntities {
    public static final EntityType<FmBoatEntity> FM_BOAT_ENTITY = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(Boatire.MOD_ID, "fm_boat"),
            FabricEntityTypeBuilder.<FmBoatEntity>create(SpawnGroup.MISC,FmBoatEntity::new)
                    .dimensions(EntityDimensions.fixed(1.375F,0.5625F))
                    .trackRangeChunks(10)
                    .build());

    public static void registerEntities(){}
}

