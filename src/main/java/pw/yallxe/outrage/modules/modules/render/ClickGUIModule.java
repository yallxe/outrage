/*
 * Copyright (c) 2018 superblaubeere27
 */

/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.render;

import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.gui.clickgui.ClickGUI;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {
    public static final @NotNull ClickGUI clickGui = new ClickGUI();

    public ClickGUIModule() {
        super("ClickGUI", "The click gui", ModuleCategory.RENDER, true, true, Keyboard.KEY_RSHIFT);
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(clickGui);
        setState(false);
    }
}
