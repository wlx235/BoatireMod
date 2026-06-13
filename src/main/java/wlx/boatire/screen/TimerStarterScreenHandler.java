package wlx.boatire.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import wlx.boatire.block.entity.TimerStarterBlockEntity;
import wlx.boatire.util.BoatInfo;

import java.util.List;

public class TimerStarterScreenHandler extends ScreenHandler {
    private final BlockPos pos;
    private final int initialDetect;
    private final int initialLaps;
    private List<BoatInfo> boatList = List.of();

    public TimerStarterScreenHandler(int syncId, PlayerInventory inv, BlockEntity blockEntity) {
        super(ModScreenHandlers.TIMER_STARTER_SCREEN_HANDLER, syncId);
        TimerStarterBlockEntity be = (TimerStarterBlockEntity) blockEntity;
        this.pos = be.getPos();
        this.initialDetect = be.getDETECT_LENGTH();
        this.initialLaps = be.getLAPS_GO();
        addPlayerInventory(inv);
    }

    public TimerStarterScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(ModScreenHandlers.TIMER_STARTER_SCREEN_HANDLER, syncId);
        this.pos = buf.readBlockPos();
        this.initialDetect = buf.readInt();
        this.initialLaps = buf.readInt();
        addPlayerInventory(inv);
    }

    public BlockPos getPos() { return pos; }
    public int getInitialDetect() { return initialDetect; }
    public int getInitialLaps() { return initialLaps; }
    public List<BoatInfo> getBoatList() { return boatList; }
    public void updateBoatList(List<BoatInfo> list) {
        this.boatList = list;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

}