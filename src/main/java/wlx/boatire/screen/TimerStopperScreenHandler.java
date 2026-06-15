package wlx.boatire.screen;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.BlockPos;
import wlx.boatire.block.entity.TimerStopperBlockEntity;
import wlx.boatire.util.TimerRecord;

import java.util.ArrayList;
import java.util.List;

public class TimerStopperScreenHandler extends ScreenHandler {
    private final BlockPos pos;
    private final int initialDetect;
    private List<TimerRecord> recordList = new ArrayList<>();

    public TimerStopperScreenHandler(int syncId, PlayerInventory inv, BlockEntity blockEntity) {
        super(ModScreenHandlers.TIMER_STOPPER_SCREEN_HANDLER, syncId);
        TimerStopperBlockEntity be = (TimerStopperBlockEntity) blockEntity;
        this.pos = be.getPos();
        this.initialDetect = be.getDETECT_LENGTH();
        addPlayerInventory(inv);
    }

    public TimerStopperScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(ModScreenHandlers.TIMER_STOPPER_SCREEN_HANDLER, syncId);
        this.pos = buf.readBlockPos();
        this.initialDetect = buf.readInt();
        addPlayerInventory(inv);

        int size1 = buf.readInt();
        List<TimerRecord> records = new ArrayList<>();
        for (int i=0;i<size1;i++){
            int color = buf.readInt();
            String driver = buf.readString();
            Long totalTime = buf.readLong();
            int textColor = buf.readInt();
            records.add(new TimerRecord(color, driver, totalTime, textColor));
        }
        this.recordList = records;
    }

    public BlockPos getPos() { return pos; }
    public int getInitialDetect() { return initialDetect; }
    public List<TimerRecord> getRecordList() { return recordList; }
    public void updateBoatList(List<TimerRecord> list) {
        this.recordList.addAll(0, list);
        this.recordList = this.recordList.subList(0,Math.min(this.recordList.size(),7));
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