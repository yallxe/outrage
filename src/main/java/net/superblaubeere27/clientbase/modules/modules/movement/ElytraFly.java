package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;
import net.superblaubeere27.clientbase.valuesystem.ModeValue;
import net.superblaubeere27.clientbase.valuesystem.NumberValue;
import org.lwjgl.input.Keyboard;


public class ElytraFly extends Module {

    private final ModeValue mode = new ModeValue("Mode", "Motion", "Motion");
    private final NumberValue<Float> speed = new NumberValue<>("Speed", 0.1f, 0.5f, 10f);

    public ElytraFly() {
        super("ElytraFly", "Allows you to fly on Elytra, click RSHIFT to fly", ModuleCategory.MOVEMENT);
    }

    private void motion_mode(MotionUpdateEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            final double yaw = Math.toRadians(mc.player.rotationYaw);
            final double pitch = Math.toRadians(mc.player.rotationPitch);

            mc.player.motionX = -Math.sin(yaw) * speed.getObject();
            mc.player.motionY = -Math.sin(pitch) * speed.getObject();
            mc.player.motionZ = Math.cos(yaw) * speed.getObject();
        }
    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (mc.player.isElytraFlying()) {
            String modeName = mode.getModes()[mode.getObject()];
            if (modeName.equals("Motion")) {
                motion_mode(event);
            }
        }
    }
}
