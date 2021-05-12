package pw.yallxe.outrage.modules.modules.render;

import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class FullBright extends Module {

    private float gamma = mc.gameSettings.gammaSetting;

    public FullBright() {
        super("FullBright", "See in complete darkness", ModuleCategory.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.gameSettings.gammaSetting = 100;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = gamma;
    }
}
