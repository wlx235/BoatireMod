package wlx.boatire.block.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wlx.boatire.Boatire;
import wlx.boatire.screen.TimerStarterScreenHandler;

public class TimerStarterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    private int DETECT_LENGTH = 3;
    private int LAPS_GO = 0;

    protected final PropertyDelegate propertyDelegate;

    public TimerStarterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIMER_STARTER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TimerStarterBlockEntity.this.DETECT_LENGTH;
                    case 1 -> TimerStarterBlockEntity.this.LAPS_GO;
                    default -> 0;
                };

            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TimerStarterBlockEntity.this.DETECT_LENGTH = value;
                    case 1 -> TimerStarterBlockEntity.this.LAPS_GO = value;
                }
                markDirty();
                Boatire.LOGGER.info(TimerStarterBlockEntity.this.DETECT_LENGTH+"111");
            }

            @Override
            public int size() {
                return 2;
            }
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.DETECT_LENGTH);  // 初始探测长度
        buf.writeInt(this.LAPS_GO);        // 初始圈数
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("hud.boatire.timerstarter.title");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TimerStarterScreenHandler(syncId, playerInventory, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.putInt("ts_block_entity_dl", this.DETECT_LENGTH);
        nbt.putInt("ts_block_entity_lg", this.LAPS_GO);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if (nbt.contains("ts_block_entity_dl")) {
            this.DETECT_LENGTH = nbt.getInt("ts_block_entity_dl");
        }
        if (nbt.contains("ts_block_entity_lg")) {
            this.LAPS_GO = nbt.getInt("ts_block_entity_lg");
        }
    }

    public void tick(World world1, BlockPos pos, BlockState state1){
        if (world1.isClient()){return;}
        detectBoats();
    }

    private void detectBoats(){

    }

    public void setFields(int detectLength, int lapsGo) {
        this.DETECT_LENGTH = detectLength;
        this.LAPS_GO = lapsGo;
        markDirty();
    }

    public int getDETECT_LENGTH() {
        return DETECT_LENGTH;
    }

    public int getLAPS_GO() {
        return LAPS_GO;
    }
}
