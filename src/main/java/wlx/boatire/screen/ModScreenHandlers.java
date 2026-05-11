package wlx.boatire.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import wlx.boatire.Boatire;

public class ModScreenHandlers {
    public static final ScreenHandlerType<TireChangerScreenHandler> TIRE_CHANGER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Boatire.MOD_ID, "tire_changer"),
                    new ExtendedScreenHandlerType<>(TireChangerScreenHandler::new));
    public static void registerScreenHandlers(){}
}
