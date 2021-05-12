package pw.yallxe.outrage.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.MotionUpdateEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.valuesystem.ModeValue;
import pw.yallxe.outrage.valuesystem.NumberValue;
import org.lwjgl.input.Keyboard;


public class ElytraFly extends Module {

    private final @NotNull ModeValue mode = new ModeValue("Mode", "Motion", "Motion");
    private final @NotNull NumberValue<Float> speed = new NumberValue<>("Speed", 0.1f, 0.1f, 10f);

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
