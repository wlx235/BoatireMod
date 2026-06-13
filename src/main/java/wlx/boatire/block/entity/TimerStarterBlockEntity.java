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
import wlx.boatire.screen.TimerStarterScreenHandler;
import wlx.boatire.util.BoatInfo;

import java.util.ArrayList;
import java.util.List;

public class TimerStarterBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory {

    private int DETECT_LENGTH = 3;
    private int LAPS_GO = 0;
    private List<BoatInfo> currentBoats = List.of();
    private int syncCD = 0;
    private boolean lastPowered = false;
    private List<FmBoatEntity> detectedBoats;

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
        detectBoats(world1, pos, state1);

        boolean powered = world.getReceivedRedstonePower(pos) > 0;
        if (powered && !lastPowered) {startTimer();}
        lastPowered = powered;
    }

    private void detectBoats(World world1, BlockPos pos1, BlockState state1){
        if (world.isClient) return;

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

        this.detectedBoats = world.getEntitiesByClass(FmBoatEntity.class, box, boat -> true);
        List<BoatInfo> newList = new ArrayList<>();

        for (FmBoatEntity boat : detectedBoats) {
            int boatColorHex = boat.getBoatColorHex();

            String driverName = "---";
            LivingEntity driver = boat.getControllingPassenger();
            if (driver instanceof PlayerEntity player) {
                String fullName = player.getName().getString();
                driverName = fullName.length() >= 6 ? fullName.substring(0, 6) : fullName;
            }
            newList.add(new BoatInfo(boatColorHex, driverName));
        }

        if (syncCD > 0) {
            syncCD--;
            return;
        }
        syncCD = 10;
        currentBoats = newList;
        markDirty();
        if (world instanceof ServerWorld serverWorld) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBlockPos(pos);
            buf.writeInt(newList.size());
            for (BoatInfo info : newList) {
                buf.writeInt(info.color());
                buf.writeString(info.driver());
            }
            if (newList.isEmpty())return;
            for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, pos)) {
                ServerPlayNetworking.send(player, Boatire.TIMER_STARTER_BOAT_INFO_SYNC, buf);
            }
        }
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

    public void startTimer(){
        for (FmBoatEntity boat: this.detectedBoats){
            if (boat.isTimerActive())continue;
            boat.startTimer(this.getLAPS_GO());
        }
    }
}
