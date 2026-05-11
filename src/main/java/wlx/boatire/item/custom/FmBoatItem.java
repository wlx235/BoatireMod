package wlx.boatire.item.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import wlx.boatire.entity.ModEntities;
import wlx.boatire.entity.custom.FmBoatEntity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import wlx.boatire.entity.ModEntities;
import wlx.boatire.entity.custom.FmBoatEntity;

import java.util.List;
import java.util.function.Predicate;

public class FmBoatItem extends Item {
    private static final Predicate<Entity> RIDERS = EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit);

    // 如果你不需要区分木材，可以先只用一个固定类型，或去掉 type 字段
    private final FmBoatEntity.FmType type;

    public FmBoatItem(FmBoatEntity.FmType type, Settings settings) {
        super(settings);
        this.type = type;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        HitResult hit = raycast(world, user, RaycastContext.FluidHandling.ANY);

        if (hit.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(stack);
        }

        // --- 原版的视线内实体检测，防止在实体身上放船 ---
        Vec3d lookVec = user.getRotationVec(1.0F);
        List<Entity> list = world.getOtherEntities(user,
                user.getBoundingBox().stretch(lookVec.multiply(5.0)).expand(1.0), RIDERS);
        if (!list.isEmpty()) {
            Vec3d eyePos = user.getEyePos();
            for (Entity entity : list) {
                Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
                if (box.contains(eyePos)) {
                    return TypedActionResult.pass(stack);
                }
            }
        }
        // ----------------------------------------

        if (hit.getType() == HitResult.Type.BLOCK) {
            // 创建你的船实体
            FmBoatEntity boat = ModEntities.FM_BOAT_ENTITY.create(world);
            if (boat == null) {
                return TypedActionResult.pass(stack);
            }

            boat.setPosition(hit.getPos().x, hit.getPos().y, hit.getPos().z);
            boat.setVariant(this.type);   // 设置木材类型（如果你有）
            boat.setYaw(user.getYaw());

            if (!world.isSpaceEmpty(boat, boat.getBoundingBox())) {
                return TypedActionResult.fail(stack);
            }

            if (!world.isClient) {
                world.spawnEntity(boat);
                world.emitGameEvent(user, GameEvent.ENTITY_PLACE, hit.getPos());
                if (!user.getAbilities().creativeMode) {
                    stack.decrement(1);
                }
            }

            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(stack, world.isClient());
        }

        return TypedActionResult.pass(stack);
    }
}