package net.superblaubeere27.clientbase.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {
    private final KeyBinding[] moveKeyBinds = new KeyBinding[]{
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump,
            mc.gameSettings.keyBindSprint
    };
    private final BooleanValue shift = new BooleanValue("Shift", false);

    public InvMove() {
        super("InvMove", "Move freely while in GUIs", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onMotionUpdate(MotionUpdateEvent event) {
        if (!getState()) return;

        if (mc.currentScreen instanceof GuiChat || mc.currentScreen == null)
            return;

        for (KeyBinding bind : moveKeyBinds)
            KeyBinding.setKeyBindState(bind.getKeyCode(), Keyboard.isKeyDown(bind.getKeyCode()));

        if (shift.getObject())
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()));
    }
}
