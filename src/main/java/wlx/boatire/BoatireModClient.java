package wlx.boatire;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import wlx.boatire.block.entity.TireChangerBlockEntity;
import wlx.boatire.entity.ModEntities;
import wlx.boatire.entity.client.ModModelLayers;
import wlx.boatire.entity.custom.FmBoatEntity;
import wlx.boatire.entity.custom.FmBoatEntityModel;
import wlx.boatire.entity.custom.FmBoatEntityRenderer;
import wlx.boatire.screen.ModScreenHandlers;
import wlx.boatire.screen.TimerStarterScreen;
import wlx.boatire.screen.TimerStopperScreen;
import wlx.boatire.screen.TireChangerScreen;
import wlx.boatire.util.BoatInfo;
import wlx.boatire.util.TimerRecord;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.util.math.MathHelper.ceil;
import static net.minecraft.util.math.MathHelper.floor;
import static wlx.boatire.Boatire.*;

public class BoatireModClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        HandledScreens.register(ModScreenHandlers.TIRE_CHANGER_SCREEN_HANDLER, TireChangerScreen::new);
        HandledScreens.register(ModScreenHandlers.TIMER_STARTER_SCREEN_HANDLER, TimerStarterScreen::new);
        HandledScreens.register(ModScreenHandlers.TIMER_STOPPER_SCREEN_HANDLER, TimerStopperScreen::new);

        EntityModelLayerRegistry.registerModelLayer(ModModelLayers.FM_BOAT_1, FmBoatEntityModel::getTexturedModelData);

        EntityRendererRegistry.register(
                ModEntities.FM_BOAT_ENTITY,
                ctx -> new FmBoatEntityRenderer(ctx, false) // false = 普通船，true = 箱子船
        );

        HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
            renderBoatHud(drawContext);
        });

        ClientPlayNetworking.registerGlobalReceiver(
                Boatire.TIRE_CHANGER_SYNC,
                (client, handler, buf, responseSender) -> {
                    BlockPos pos = buf.readBlockPos();
                    int status = buf.readInt();
                    client.execute(() -> {
                        if (client.world != null) {
                            BlockEntity be = client.world.getBlockEntity(pos);
                            if (be instanceof TireChangerBlockEntity tireChanger) {
                                tireChanger.setClientStatus(status);
                            }
                        }
                    });
                }
        );

        ClientPlayNetworking.registerGlobalReceiver(TIMER_STARTER_BOAT_INFO_SYNC, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int count = buf.readInt();
            List<BoatInfo> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int color = buf.readInt();
                String name = buf.readString();
                list.add(new BoatInfo(color, name));
            }
            client.execute(() -> {
                if (client.currentScreen instanceof TimerStarterScreen screen) {
                    if (screen.getScreenHandler().getPos().equals(pos)) {
                        screen.getScreenHandler().updateBoatList(list);
                    }
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(TIMER_STOPPER_BOAT_INFO_SYNC, (client, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int count = buf.readInt();
            List<TimerRecord> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                int color = buf.readInt();
                String name = buf.readString();
                Long recordTime = buf.readLong();
                int textColor = buf.readInt();
                list.add(new TimerRecord(color, name, recordTime, textColor));
            }
            client.execute(() -> {
                if (client.currentScreen instanceof TimerStopperScreen screen) {
                    System.out.println(11);
                    if (screen.getScreenHandler().getPos().equals(pos)) {
                        screen.getScreenHandler().updateBoatList(list);

                    }
                }
            });
        });
    }

    private void renderBoatHud(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        if (!(client.player.getVehicle() instanceof FmBoatEntity boat)) return;

        int tireId = boat.getLoadedTire();
        String tireName = switch (tireId) {
            case 1 -> "H1";
            case 2 -> "H2";
            case 3 -> "H3";
            case 4 -> "H4";
            case 5 -> "H5";
            default -> "*" + tireId;
        };

        int radius = 4;
        BlockPos playerPos = client.player.getBlockPos();
        int tcStat=0;
        for (BlockPos pos : BlockPos.iterateOutwards(playerPos, radius, radius, radius)) {
            BlockEntity be = client.world.getBlockEntity(pos);
            if (be instanceof TireChangerBlockEntity tireChanger) {
                tcStat = tireChanger.getTcStatus();
                break;
            }
        }

        TextRenderer font = client.textRenderer;
        int color = 0xFFFFFFFF;
        int tcColor = 0xFF7FFF00;
        int bkgColor = 0xFF696969;
        int durColor = boat.getTireColor(tireId);
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        int x = screenWidth - 10;
        int y = 10;
        int lenMult = 50;//should be 50

        int lenDur = floor((boat.getTireDur()/(FmBoatEntity.basicDur*1.0F))*lenMult);
        int maxLenDur = floor((boat.getMaxTireDur(boat.getLoadedTire())*1.0F/FmBoatEntity.basicDur)*lenMult);
        //should be 50px H5 full dur

        drawContext.drawHorizontalLine(x - maxLenDur,x,y+font.fontHeight/2,bkgColor);
        drawContext.drawHorizontalLine(x - lenDur,x,y+font.fontHeight/2,durColor);

        drawContext.drawText(font, Text.literal(tireName),
                x - font.getWidth(tireName)-maxLenDur-6, y, color, true);

        if (boat.isTireLow()){drawContext.drawText(font, Text.literal("L"),
                x - font.getWidth(tireName)-maxLenDur-11-font.getWidth("L"), y, 0xFFFFA500, true);}
        if (boat.isTireBroken()){drawContext.drawText(font, Text.literal("D"),
                x - font.getWidth(tireName)-maxLenDur-11-font.getWidth("D"), y, 0xFFAB0000, true);}

        float maxTcStat = 60.0F;
        y += font.fontHeight + 2;
        Text tcText = Text.translatable("hud.boatire.tctext");
        int lenTc = ceil(tcStat/maxTcStat*100);
        if (tcStat!=0) {
            if (tcStat==maxTcStat){tcText=Text.of("√");}
            drawContext.drawText(font, tcText,
                    x - 106 - font.getWidth(tcText), y, color, true);
            drawContext.drawHorizontalLine(x - (100 - lenTc), x, y + font.fontHeight / 2, bkgColor);
            drawContext.drawHorizontalLine(x - 100, x - (100 - lenTc), y + font.fontHeight / 2, tcColor);
        }
        //SPEEDHUD
        int y0 = screenHeight - 2 - font.fontHeight;
        int x0 = screenWidth - 2;
        if (Boatire.config.showSpeedHud) {
            double speed = boat.getVelocity().horizontalLength()*20*3.6;
            String speedText = String.format("%.1f kb/h", speed);
            //if (!client.player.isCreative()) y0 -= 18;
            drawContext.drawText(font, speedText, x0 - 112, y0, color, true);
            //y0 -= font.fontHeight+2;
        }
        //TIMERHUD
        if (Boatire.config.showTimerHud) {
            if (boat.getInDetectArea() || boat.isTimerActive() || !boat.isTimerZero()) {
                long elTicks = boat.getElapsedTicks();
                int timerColor1 = (boat.isTimerActive())?0xFF33FF33:0xFFFFFFFF;
                int timerColor2 = 0xFFFFFF33;
                String timerText = ((elTicks >= 72000) ? elTicks / 72000 + ":" : "") +
                        String.format("%02d", (elTicks % 72000) / 1200) + ":" +
                        String.format("%02d", (elTicks % 1200) / 20) + ":" +
                        String.format("%02d", elTicks % 20 * 5);//(h+":")
                drawContext.drawText(font, timerText, x0 - font.getWidth(timerText), y0, timerColor1, false);
                long lastLapTime = boat.getLastLapTime();
                if (lastLapTime!=0){
                    String timerText2 = ((lastLapTime >= 72000) ? lastLapTime / 72000 + ":" : "") +
                            String.format("%02d", (lastLapTime % 72000) / 1200) + ":" +
                            String.format("%02d", (lastLapTime % 1200) / 20) + ":" +
                            String.format("%02d", lastLapTime % 20 * 5);//(h+":")
                    drawContext.drawText(font, timerText2, x0 - font.getWidth(timerText2), y0-font.fontHeight, timerColor2, false);
                }
            }
        }
    }
}
