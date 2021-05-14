/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.GameTickEvent;
import pw.yallxe.outrage.events.PacketEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;
import pw.yallxe.outrage.utils.ReflectionFields;
import pw.yallxe.outrage.utils.Timer;
import pw.yallxe.outrage.valuesystem.ModeValue;

import java.util.Objects;

public class PacketFly extends Module {
    private @NotNull ModeValue mode = new ModeValue("Mode", "New", "New", "Old");

    private final @NotNull Timer timer = new Timer();

    public PacketFly() {
        super("PacketFly", "Allows you to fly via packets", ModuleCategory.MOVEMENT);

        ReflectionFields.init();
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.8873D;

        if (mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }

        return baseSpeed;
    }
    
    @EventTarget
    private void onTick(@NotNull GameTickEvent event) {
        if (mode.getModes()[mode.getObject()].equals("New")) {
            float forward = 0.0f;
            float strafe = 0.0f;
            double speed = 2.7999999999999999999999;
            float var5 = MathHelper.sin(mc.player.rotationYaw * 3.1415927f / 180.0f);
            float var6 = MathHelper.cos(mc.player.rotationYaw * 3.1415927f / 180.0f);
            if (ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // forward
                forward += 0.1f;
            } else if (!ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // backwards
                forward -= 0.1f;
            } else if (!ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // left
                strafe += 0.1f;
            }
            if (!ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // right
                strafe -= 0.1f;
            } else if (ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // forwards and left
                forward += 0.0624f;
                strafe += 0.0624f;
            } else if (ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // forwards and right
                forward += 0.0624f;
                strafe -= 0.0624f;
            } else if (!ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // backwards and left
                forward -= 0.0624f;
                strafe += 0.0624f;
            } else if (!ReflectionFields.getPressed(mc.gameSettings.keyBindForward)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindBack)
                    && !ReflectionFields.getPressed(mc.gameSettings.keyBindLeft)
                    && ReflectionFields.getPressed(mc.gameSettings.keyBindRight)) {
                // backwards and right
                forward -= 0.0624f;
                strafe -= 0.0624f;
            }

            double motionX = (strafe * var6 - forward * var5) * speed;
            double motionZ = (forward * var6 + strafe * var5) * speed;
            double motionY = 0;

            motionY = (mc.gameSettings.keyBindJump.isKeyDown() ? 0.0624 : 0)
                    - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0624 : 0);


            mc.player.motionY = 0;

            if (timer.hasTimeElapsed(500, true) && !mc.gameSettings.keyBindSneak.isKeyDown()) {

                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX,
                        mc.player.posY - 0.0624, mc.player.posZ, mc.player.rotationYaw,
                        mc.player.rotationPitch, false));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX,
                        mc.player.posY - 999.0D, mc.player.posZ, mc.player.rotationYaw,
                        mc.player.rotationPitch, true));

                return;
            }

            if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()
                    || mc.gameSettings.keyBindLeft.isKeyDown()
                    || mc.gameSettings.keyBindRight.isKeyDown()
                    || mc.gameSettings.keyBindSneak.isKeyDown()
                    || mc.gameSettings.keyBindJump.isKeyDown()) && !timer.hasTimeElapsed(500, true)
                    || mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.connection
                        .sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + motionX,
                                mc.player.posY + motionY, mc.player.posZ + motionZ,
                                mc.player.rotationYaw, mc.player.rotationPitch, false));
                mc.player.connection
                        .sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + motionX,
                                mc.player.posY - 999.0D, mc.player.posZ + motionZ,
                                mc.player.rotationYaw, mc.player.rotationPitch, true));
            }
        } else if (mode.getModes()[mode.getObject()].equals("Old")) {
            mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(
                    mc.player.posX + mc.player.motionX
                            + (mc.gameSettings.keyBindForward.isKeyDown() ? 0.0624 : 0)
                            - (mc.gameSettings.keyBindBack.isKeyDown() ? 0.0624 : 0),
                    mc.player.posY + (mc.gameSettings.keyBindJump.isKeyDown() ? 0.0624 : 0)
                            - (mc.gameSettings.keyBindSneak.isKeyDown() ? 0.0624 : 0),
                    mc.player.posZ + mc.player.motionZ
                            + (mc.gameSettings.keyBindRight.isKeyDown() ? 0.0624 : 0)
                            - (mc.gameSettings.keyBindLeft.isKeyDown() ? 0.0624 : 0),
                    mc.player.rotationYaw, mc.player.rotationPitch, false));
            mc.player.connection.sendPacket(
                    new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX,
                            mc.player.posY - 42069, mc.player.posZ + mc.player.motionZ,
                            mc.player.rotationYaw, mc.player.rotationPitch, true));
        }
        
    }
    
    @EventTarget
    private void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook poslook = (SPacketPlayerPosLook) event.getPacket();
            if (mc.player != null && mc.player.rotationYaw != -180.0f
                    && mc.player.rotationPitch != 0.0f) {
                ReflectionFields.setSPacketPlayerPosLookYaw(mc.player.rotationYaw, poslook);
                ReflectionFields.setSPacketPlayerPosLookPitch(mc.player.rotationPitch, poslook);
            }
        }
    }
}
