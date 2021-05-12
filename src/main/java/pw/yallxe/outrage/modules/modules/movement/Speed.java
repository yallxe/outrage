package pw.yallxe.outrage.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.MotionUpdateEvent;
import pw.yallxe.outrage.events.SetbackEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.MovementUtil;
import pw.yallxe.outrage.valuesystem.BooleanValue;
import pw.yallxe.outrage.valuesystem.NumberValue;

public class Speed extends Module {
    private final @NotNull BooleanValue setbackCheck = new BooleanValue("SetbackCheck", true);
    private final @NotNull BooleanValue ticksExistedBool = new BooleanValue("Boost on existed ticks", false);
    private final @NotNull NumberValue<Integer> ticksExistedInt = new NumberValue<>("Ticks existed", 2, 1, 20);
    private final @NotNull NumberValue<Float> speedOnExisted = new NumberValue<>("Speed on existed", 0.5f, 0.1f, 1f);
    private final @NotNull NumberValue<Float> defaultSpeed = new NumberValue<>("Speed", 0.5f, 0.1f, 1f);

    public Speed() {
        super("Speed", "Go brrr", ModuleCategory.MOVEMENT);
    }

    @EventTarget
    private void onMotionUpdate(@NotNull MotionUpdateEvent event) {
        if (!getState()) return;

        if (MovementUtil.isMoving()  && !mc.player.capabilities.isFlying && !mc.player.isInWater() && !mc.player.isOnLadder()) {
            if (mc.player.onGround) {
                mc.player.jump();
            }
            double forward = mc.player.moveForward;
            double strafe = mc.player.moveStrafing;
            float yaw = mc.player.rotationYaw;
            if (mc.player.moveForward != 0.0D) {
                if (mc.player.moveStrafing > 0.0D) {
                    yaw = mc.player.rotationYaw + (float)(mc.player.moveForward > 0.0D ? -45 : 45);
                } else if (mc.player.moveStrafing < 0.0D) {
                    yaw = mc.player.rotationYaw + (float)(mc.player.moveForward > 0.0D ? 45 : -45);
                }

                strafe = 0.0D;
                if (mc.player.moveForward > 0.0D) {
                    forward = 1.0D;
                } else if (mc.player.moveForward < 0.0D) {
                    forward = -1.0D;
                }
            }

            if (strafe > 0.0D) {
                strafe = 1.0D;
            } else if (strafe < 0.0D) {
                strafe = -1.0D;
            }

            double mx = Math.cos(Math.toRadians(yaw + 90.0F));
            double mz = Math.sin(Math.toRadians(yaw + 90.0F));
            double boost;
            if (mc.player.ticksExisted % ticksExistedInt.getObject() == 0 && ticksExistedBool.getObject()) {
                boost = speedOnExisted.getObject();
            } else {
                boost = defaultSpeed.getObject();
            }
            mc.player.motionX = forward * boost * mx + strafe * 0.25 * mz;
            mc.player.motionZ = forward * boost * mz - strafe * 0.25 * mx;
        }
    }

    @EventTarget
    private void onSetback(SetbackEvent event) {
        if (setbackCheck.getObject()) {
            this.setState(false);
        }
    }
}
