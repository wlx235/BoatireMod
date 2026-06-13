package wlx.boatire.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;
import wlx.boatire.util.BoatInfo;

import java.util.List;

import static java.lang.Math.min;

public class TimerStarterScreen extends HandledScreen<TimerStarterScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(Boatire.MOD_ID, "textures/gui/boat_timer_starter_gui.png");
    private TextFieldWidget detectLengthField;
    private TextFieldWidget lapsGoField;

    public TimerStarterScreen(TimerStarterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 6;
        playerInventoryTitleY = this.backgroundHeight - 94;

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        int initDetect = this.handler.getInitialDetect();
        int initLaps   = this.handler.getInitialLaps();

        this.detectLengthField = new TextFieldWidget(
                textRenderer, x + 10, y + 27, 41, 10, Text.empty()
        );
        this.lapsGoField = new TextFieldWidget(
                textRenderer, x + 10, y + 57, 41, 10, Text.empty()
        );

        this.detectLengthField.setMaxLength(2);
        this.detectLengthField.setTextPredicate(text -> text.matches("\\d*"));
        this.detectLengthField.setDrawsBackground(false);
        this.detectLengthField.setText(String.valueOf(initDetect));
        addDrawableChild(this.detectLengthField);

        this.lapsGoField.setMaxLength(2);
        this.lapsGoField.setTextPredicate(text -> text.matches("\\d*"));
        this.lapsGoField.setDrawsBackground(false);
        this.lapsGoField.setText(String.valueOf(initLaps));
        addDrawableChild(this.lapsGoField);
    }

    private int clampInt(String text, int min, int max) {
        if (text.isEmpty()) return min;
        try {
            int val = Integer.parseInt(text);
            return Math.max(min, min(val, max));
        } catch (NumberFormatException e) {
            return min;
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
        renderBoatContent(context);
    }

    @Override
    public void close() {
        int detect = clampInt(this.detectLengthField != null ? this.detectLengthField.getText() : "3", 1, 32);
        int laps = clampInt(this.lapsGoField != null ? this.lapsGoField.getText() : "0", 0, 99);

        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(this.handler.getPos());
        buf.writeInt(detect);
        buf.writeInt(laps);
        ClientPlayNetworking.send(Boatire.TIMER_STARTER_UPDATE, buf);
        super.close();
    }

    public void renderBoatContent(DrawContext context){
        List<BoatInfo> boats = handler.getBoatList();
        int startX = (width - backgroundWidth) / 2 + 58;
        int startY = (height - backgroundHeight) / 2 + 12;
        int lineHeight = 8;
        for (int i = 0;i < min(boats.size(),10);i++) {
            if (i >= 7 && startX == (width - backgroundWidth) / 2 + 58){startX += 54;startY -= lineHeight*7;}
            BoatInfo info = boats.get(i);
            int y = startY + lineHeight*i + 1;
            context.fill(startX + 1, y, startX + 11, y + lineHeight - 1, info.color() | 0xFF000000);
            context.drawText(textRenderer, Text.literal(info.driver()), startX + 13, y, 0xFFFFFFFF, true);
        }

    }

}