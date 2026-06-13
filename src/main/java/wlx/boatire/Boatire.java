package wlx.boatire;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wlx.boatire.block.ModBlocks;
import wlx.boatire.block.entity.ModBlockEntities;
import wlx.boatire.block.entity.TimerStarterBlockEntity;
import wlx.boatire.entity.ModEntities;
import wlx.boatire.item.ModItemGroups;
import wlx.boatire.item.ModItems;
import wlx.boatire.screen.ModScreenHandlers;

public class Boatire implements ModInitializer {
	public static final String MOD_ID = "boatire";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier TIRE_CHANGER_SYNC =
			new Identifier(MOD_ID, "tire_changer_sync");
	public static final Identifier BOAT_INPUT_SYNC = new Identifier(MOD_ID, "boat_input_sync");
	public static final Identifier TIMER_STARTER_UPDATE = new Identifier(MOD_ID, "timer_starter_update");
	public static final Identifier TIMER_STARTER_BOAT_INFO_SYNC = new Identifier(MOD_ID, "timer_starter_boat_info_sync");
	public static BoatireConfig config;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModItems.registerItems();
		ModItemGroups.registerGroups();
		ModBlocks.registerModBlocks();
		ModEntities.registerEntities();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();

		//FabricDefaultAttributeRegistry.register(ModEntities.FM_BOAT1, FmBoatEntity.createBoatAttribute());
		AutoConfig.register(BoatireConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(BoatireConfig.class).getConfig();

		ServerPlayNetworking.registerGlobalReceiver(TIMER_STARTER_UPDATE, (server, player, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			int detectLength = buf.readInt();
			int lapsGo = buf.readInt();
			server.execute(() -> {
				World world = server.getWorld(player.getSpawnPointDimension());
				if (world.getBlockEntity(pos) instanceof TimerStarterBlockEntity be) {
					be.setFields(detectLength, lapsGo); // 待添加的方法
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(TIMER_STARTER_BOAT_INFO_SYNC, (server, player, handler, buf, responseSender) -> {});

		LOGGER.info("Hello Fabric world!");
	}
}