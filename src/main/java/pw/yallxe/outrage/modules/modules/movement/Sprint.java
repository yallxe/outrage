/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.MotionUpdateEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.MovementUtil;
import pw.yallxe.outrage.valuesystem.BooleanValue;

public class Sprint extends Module {
    private final @NotNull BooleanValue always = new BooleanValue("Always", false);

    public Sprint() {
        super("Sprint", "Keep sprint", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

    @EventTarget
    private void onMotionUpdate(@NotNull MotionUpdateEvent event) {
        if (!getState()) return;
        if (always.getObject() && MovementUtil.isMoving() && !mc.player.collidedHorizontally) {
            mc.player.setSprinting(true);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
        }
    }
}
