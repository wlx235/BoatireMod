package wlx.boatire.block.entity;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import wlx.boatire.Boatire;
import wlx.boatire.entity.custom.FmBoatEntity;
import wlx.boatire.item.ModItems;
import wlx.boatire.screen.TireChangerScreenHandler;

import java.util.List;

public class TireChangerBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory,ImplementedInventory {

    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);

    private static final int INPUT_SLOT = 0;
    //private static final int OUTPUT_SLOT = 2;
    public int tcStatus = 0;
    public int maxTcStatus = 60;

    protected final PropertyDelegate propertyDelegate;

    private int loadedTire = 0;//0=null 1-5=paddle h1-h5

    public TireChangerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TIRE_CHANGER_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index){
                    case 0 -> TireChangerBlockEntity.this.loadedTire;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index){
                    case 0 -> TireChangerBlockEntity.this.loadedTire = value;
                };
            }

            @Override
            public int size() {return 1;}
        };
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.literal("Tire Changer");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new TireChangerScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("tcStatus", this.tcStatus);
        nbt.putInt("tire_changer",loadedTire);
    }
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        this.tcStatus = nbt.getInt("tcStatus");
        loadedTire = nbt.getInt("tire_changer");
    }

    private List<Item> avPaddles = List.of(ModItems.H1_TIRE, ModItems.H2_TIRE, ModItems.H3_TIRE, ModItems.H4_TIRE, ModItems.H5_TIRE);

    public void tick(World world1, BlockPos pos, BlockState state1){

        if (world1.isClient()){return;}
        if (inventory!=null){
            Item inputItem = this.getStack(INPUT_SLOT).getItem();
            if (avPaddles.contains(inputItem)) {//good
                if (inputItem == ModItems.H1_TIRE) {loadedTire = 1;}
                else if (inputItem == ModItems.H2_TIRE) {loadedTire = 2;}
                else if (inputItem == ModItems.H3_TIRE) {loadedTire = 3;}
                else if (inputItem == ModItems.H4_TIRE) {loadedTire = 4;}
                else if (inputItem == ModItems.H5_TIRE) {loadedTire = 5;}

                Direction facing = state1.get(HorizontalFacingBlock.FACING);

                BlockPos centerPos = pos.offset(facing, 2); // 向前两格作为中心
                double range = 1.5; // 检测半径
                Box box = new Box(centerPos).expand(range);
                List<FmBoatEntity> boats = world.getEntitiesByClass(FmBoatEntity.class, box, boat -> true);

                if (boats.size()==0){this.resetTcStatus();}
                else {
                    if(tcStatus!=maxTcStatus){this.increaseTcStatus();}
                    else {
                        for (FmBoatEntity boat : boats) {
                            if ((boat.getLoadedTire() != loadedTire || boat.getTireDur()<=boat.getMaxTireDur(boat.getLoadedTire())*0.999F)) {
                                boat.setLoadedTire(loadedTire,true);
                                //Boatire.LOGGER.info("tc completed!");
                            }
                        }
                        //this.resetTcStatus();
                    }
                }
            }
            else {loadedTire=0;this.resetTcStatus();}
        }
        markDirty(world1, pos, state1);

    }

    public void resetTcStatus() {
        setTcStatus(0);
    }

    public void increaseTcStatus() {
        setTcStatus(this.tcStatus + 1);
    }
    public int getTcStatus(){return this.tcStatus;}

    public void setTcStatus(int status) {
        if (this.tcStatus != status) {
            this.tcStatus = status;
            markDirty();
            if (world instanceof ServerWorld serverWorld) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeBlockPos(this.pos);
                buf.writeInt(status);
                for (ServerPlayerEntity player : PlayerLookup.tracking(serverWorld, this.pos)) {
                    ServerPlayNetworking.send(player, Boatire.TIRE_CHANGER_SYNC, buf);
                }
            }
        }
    }

    public void setClientStatus(int status) {
        this.tcStatus = status;
    }
}
