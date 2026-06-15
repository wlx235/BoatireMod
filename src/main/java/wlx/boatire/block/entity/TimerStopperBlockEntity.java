package wlx.boatire.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wlx.boatire.Boatire;
import wlx.boatire.entity.custom.FmBoatEntity;
import wlx.boatire.screen.TimerStopperScreenHandler;
import wlx.boatire.util.TimerRecord;

import java.util.ArrayList;
import java.util.List;

public class TimerStopperBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    private int DETECT_LENGTH = 3;
    private List<FmBoatEntity> detectedBoats = new ArrayList<>();
    private List<FmBoatEntity> lastDetectedBoats = new ArrayList<>();
    private List<TimerRecord> timerRecords = new ArrayList<>();

    protected final PropertyDelegate propertyDelegate;

    public TimerStopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIMER_STOPPER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> TimerStopperBlockEntity.this.DETECT_LENGTH;
                    default -> 0;
                };

            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> TimerStopperBlockEntity.this.DETECT_LENGTH = value;
                }
                markDirty();
            }

            @Override
            public int size() {
                return 1;
            }
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.DETECT_LENGTH);

        List<TimerRecord> records = getTimerRecords();
        buf.writeInt(records.size());
        for (TimerRecord rec : records){
            buf.writeInt(rec.color());
            buf.writeString(rec.driver());
            buf.writeLong(rec.TotalTime());
            buf.writeInt(rec.textColor());
        }
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("hud.boatire.timerstopper.title");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TimerStopperScreenHandler(syncId, playerInventory, this);
    }

    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.putInt("tsp_block_entity_dl", this.DETECT_LENGTH);
        NbtList nbtList = new NbtList();
        for (TimerRecord record : this.timerRecords){
            NbtCompound entry = new NbtCompound();
            entry.putInt("color", record.color());
            entry.putString("driverName", record.driver());
            entry.putLong("totalTime", record.TotalTime());
            entry.putInt("textColor", record.textColor());
            nbtList.add(entry);
        }
        nbt.put("tsp_block_entity_timerRecords", nbtList);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if (nbt.contains("tsp_block_entity_dl")) {
            this.DETECT_LENGTH = nbt.getInt("tsp_block_entity_dl");
        }
        if (this.timerRecords == null) {
            this.timerRecords = new ArrayList<>();
        } else {
            this.timerRecords.clear();
        }
        if (nbt.contains("tsp_block_entity_timerRecords")) {
            NbtList recordList = nbt.getList("tsp_block_entity_timerRecords", NbtList.COMPOUND_TYPE);
            for (int i = 0; i < recordList.size(); i++) {
                NbtCompound entry = recordList.getCompound(i);
                int color = entry.getInt("color");
                String driveName = entry.getString("driverName");
                Long timeElapsed = entry.getLong("totalTime");
                int textColor = entry.getInt("textColor");
                this.timerRecords.add(new TimerRecord(color, driveName, timeElapsed, textColor));
            }
        }
    }

    public void tick(World world1, BlockPos pos, BlockState state1){
        if (world1.isClient()){return;}
        detectBoats(world1, pos, state1);

        boolean powered = false;
        if (world != null) {powered = world.getReceivedRedstonePower(pos) > 0;}
        if (powered)stopTimer();
    }

    private void detectBoats(World world1, BlockPos pos1, BlockState state1){
        if (world1.isClient) return;

        Direction facing = state1.get(HorizontalFacingBlock.FACING);

        int range = Math.max(1, Math.min(this.DETECT_LENGTH, 32)) - 1;

        double halfWidth = 1.0;
        double height = 2.0;


        BlockPos startPos = pos.offset(facing, 1);

        Box box;
        switch (facing) {
            case NORTH: //z-
                box = new Box(
                        startPos.getX() - halfWidth, startPos.getY(), startPos.getZ() - range,
                        startPos.getX() + halfWidth + 1, startPos.getY() + height, startPos.getZ() + 1
                );
                break;
            case SOUTH: //z+
                box = new Box(
                        startPos.getX() - halfWidth, startPos.getY(), startPos.getZ() + 1,
                        startPos.getX() + halfWidth + 1, startPos.getY() + height, startPos.getZ() + range + 1
                );
                break;
            case WEST: //x-
                box = new Box(
                        startPos.getX() - range, startPos.getY(), startPos.getZ() - halfWidth,
                        startPos.getX() + 1, startPos.getY() + height, startPos.getZ() + halfWidth + 1
                );
                break;
            case EAST: //x+
                box = new Box(
                        startPos.getX() + 1, startPos.getY(), startPos.getZ() - halfWidth,
                        startPos.getX() + range + 1, startPos.getY() + height, startPos.getZ() + halfWidth + 1
                );
                break;
            default:
                return;
        }
        this.lastDetectedBoats = this.detectedBoats;
        this.detectedBoats = world.getEntitiesByClass(FmBoatEntity.class, box, boat -> true);
        List<TimerRecord> newList = new ArrayList<>();
        for (FmBoatEntity boat : lastDetectedBoats){
            boat.decDetectCount();
        }
        for (FmBoatEntity boat : detectedBoats) {
            if (this.lastDetectedBoats.contains(boat))continue;
            boat.incDetectCount();
            if (!boat.isTimerActive())continue;
            int boatColorHex = boat.getBoatColorHex();

            String driverName = "---";
            LivingEntity driver = boat.getControllingPassenger();
            if (driver instanceof PlayerEntity player) {
                String fullName = player.getName().getString();
                driverName = fullName.length() >= 6 ? fullName.substring(0, 6) : fullName;
            }
            Long finishTime = world1.getTime()-boat.getTimerStartTime();
            TimerRecord record = new TimerRecord(boatColorHex, driverName, finishTime, getTextColor(0));
            newList.add(record);
            this.timerRecords.add(0,record);
            Boatire.LOGGER.info(driverName);
        }
        this.timerRecords = this.timerRecords.subList(0,Math.min(7,this.timerRecords.size()));
        markDirty();
        if (world instanceof ServerWorld serverWorld) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(pos);
            buf.writeInt(newList.size());
            for (TimerRecord info : newList) {
                buf.writeInt(info.color());
                buf.writeString(info.driver());
                buf.writeLong(info.TotalTime());
                buf.writeInt(info.textColor());
            }
            if (newList.isEmpty())return;
            for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, pos)) {
                ServerPlayNetworking.send(player, Boatire.TIMER_STOPPER_BOAT_INFO_SYNC, buf);
            }
        }
    }

    public void setFields(int detectLength) {
        this.DETECT_LENGTH = detectLength;
        markDirty();
    }

    public int getDETECT_LENGTH() {
        return DETECT_LENGTH;
    }

    public void stopTimer(){
        for (FmBoatEntity boat: this.detectedBoats){
            if (!this.lastDetectedBoats.contains(boat)) {
                if (!boat.isTimerActive()) continue;
                //stoptimer
                if (boat.getLapsToGo() != 0) {
                    boat.minusOneLap();
                    boat.recordLastLapTime();
                } else {
                    boat.recordLastLapTime();
                    boat.pauseTimer();
                }
            }
        }
    }

    private int getTextColor(int situ){//0:FinishTime 1:LapTime
        return switch (situ){
            case 1 -> 0xFFFFFF33;
            default -> 0xFFFFFFFF;
        };
    }

    public List<TimerRecord> getTimerRecords(){return this.timerRecords;}
}
