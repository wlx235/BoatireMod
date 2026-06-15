package wlx.boatire;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "boatire")
public class BoatireConfig implements ConfigData {
    public boolean showSpeedHud = true;
    public boolean showTimerHud = true;
}
