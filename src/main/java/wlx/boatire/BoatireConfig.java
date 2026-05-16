package wlx.boatire;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "boatire")
public class BoatireConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean showSpeedHud = true; 
}
