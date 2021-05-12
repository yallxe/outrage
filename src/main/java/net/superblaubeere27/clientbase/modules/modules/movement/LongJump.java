package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class LongJump extends Module {
    public LongJump() {
        super("LongJump", "Big jump", ModuleCategory.MOVEMENT);
    }

    private static void setMovementSpeed(float speed) {
        final double yaw = Math.toRadians(mc.player.rotationYaw);
        mc.player.motionX = -Math.sin(yaw) * speed;
        mc.player.motionZ = Math.cos(yaw) * speed;
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (mc.player.onGround) {
            mc.player.jump();
            setMovementSpeed(1.4f);
        }
    }

    @EventTarget
    private void onMotionUpdate(MotionUpdateEvent event) {
        if (!getState()) return;

        if (mc.player.onGround || mc.player.capabilities.isFlying) {
            setState(false);
        } else {
            setMovementSpeed(0.8f);
        }
    }
}
