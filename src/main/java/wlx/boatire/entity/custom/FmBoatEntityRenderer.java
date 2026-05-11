package wlx.boatire.entity.custom;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.ChestBoatEntityModel;
import net.minecraft.client.render.entity.model.ChestRaftEntityModel;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ModelWithWaterPatch;
import net.minecraft.client.render.entity.model.RaftEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Quaternionf;
import wlx.boatire.Boatire;
import wlx.boatire.entity.client.ModModelLayers;

@Environment(EnvType.CLIENT)
public class FmBoatEntityRenderer extends EntityRenderer<FmBoatEntity> {
    private final Map<FmBoatEntity.FmType, Pair<Identifier, CompositeEntityModel<FmBoatEntity>>> texturesAndModels;

    public FmBoatEntityRenderer(EntityRendererFactory.Context ctx, boolean chest) {
        super(ctx);
        this.shadowRadius = 0.8F;
        this.texturesAndModels = Stream.of(FmBoatEntity.FmType.values())
                .collect(ImmutableMap.toImmutableMap(
                        type -> type,
                        type -> Pair.of(
                                new Identifier(Boatire.MOD_ID, getTexture(type, chest)), // 纹理路径
                                this.createModel(ctx, type, chest)
                        )
                ));
    }

    private CompositeEntityModel<FmBoatEntity> createModel(EntityRendererFactory.Context ctx, FmBoatEntity.FmType type, boolean chest) {
        EntityModelLayer entityModelLayer = ModModelLayers.FM_BOAT_1;
        ModelPart modelPart = ctx.getPart(entityModelLayer);
        return new FmBoatEntityModel(modelPart);
    }

    private static String getTexture(FmBoatEntity.FmType type, boolean chest) {
        return "textures/entity/boat/" + type.getName() + ".png";
    }

    public void render(FmBoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0F, 0.375F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F - f));
        float h = boatEntity.getDamageWobbleTicks() - g;
        float j = boatEntity.getDamageWobbleStrength() - g;
        if (j < 0.0F) {
            j = 0.0F;
        }

        if (h > 0.0F) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(MathHelper.sin(h) * h * j / 10.0F * boatEntity.getDamageWobbleSide()));
        }

        float k = boatEntity.interpolateBubbleWobble(g);
        if (!MathHelper.approximatelyEquals(k, 0.0F)) {
            matrixStack.multiply(new Quaternionf().setAngleAxis(boatEntity.interpolateBubbleWobble(g) * (float) (Math.PI / 180.0), 1.0F, 0.0F, 1.0F));
        }

        Pair<Identifier, CompositeEntityModel<FmBoatEntity>> pair = (Pair<Identifier, CompositeEntityModel<FmBoatEntity>>)this.texturesAndModels
                .get(boatEntity.getVariant());
        Identifier identifier = pair.getFirst();
        CompositeEntityModel<FmBoatEntity> compositeEntityModel = pair.getSecond();
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        compositeEntityModel.setAngles(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(compositeEntityModel.getLayer(identifier));
        compositeEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            if (compositeEntityModel instanceof ModelWithWaterPatch modelWithWaterPatch) {
                modelWithWaterPatch.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
            }
        }

        matrixStack.pop();
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(FmBoatEntity boatEntity) {
        return (Identifier)((Pair)this.texturesAndModels.get(boatEntity.getVariant())).getFirst();
    }
}

