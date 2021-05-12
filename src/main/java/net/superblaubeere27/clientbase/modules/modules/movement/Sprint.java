package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.utils.MovementUtil;
import net.superblaubeere27.clientbase.valuesystem.BooleanValue;

public class Sprint extends Module {
    private final BooleanValue always = new BooleanValue("Always", false);

    public Sprint() {
        super("Sprint", "Keep sprint", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

    @EventTarget
    private void onMotionUpdate(MotionUpdateEvent event) {
        if (!getState()) return;
        if (always.getObject() && MovementUtil.isMoving() && !mc.player.collidedHorizontally) {
            mc.player.setSprinting(true);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }
}
