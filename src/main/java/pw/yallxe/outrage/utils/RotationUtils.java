/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;

public class RotationUtils {
    private static final @NotNull Minecraft mc = Minecraft.getMinecraft();

    public static float @NotNull [] calcAngle(Vec3d from, Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0F;
        final double difZ = to.z - from.z;

        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);

        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0f), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist)))};
    }

    public static float wrapAngleTo180_float(float value)
    {
        value %= 360.0F;

        if (value >= 180.0F)
        {
            value -= 360.0F;
        }

        if (value < -180.0F)
        {
            value += 360.0F;
        }

        return value;
    }
    
    public static Vec3d getCenter(final AxisAlignedBB bb) {
        return new Vec3d(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxX - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }

    public static Rotation toRotation(final @NotNull Vec3d vec, final boolean predict) {
        final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY +
                mc.player.getEyeHeight(), mc.player.posZ);

        if (predict)
            eyesPos.addVector(mc.player.motionX, mc.player.motionY, mc.player.motionZ);

        final double diffX = vec.x - eyesPos.x;
        final double diffY = vec.y - eyesPos.y;
        final double diffZ = vec.z - eyesPos.z;

        return new Rotation(wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        ));
    }
    
    public static double getRotationDifference(final Entity entity) {
        final Rotation rotation = toRotation(getCenter(entity.getEntityBoundingBox()), true);

        return getRotationDifference(rotation, new Rotation(mc.player.rotationYaw, mc.player.rotationPitch));
    }

    public static double getRotationDifference(final Rotation a, final Rotation b) {
        return Math.hypot(getAngleDifference(a.rotationYaw, b.rotationYaw), a.rotationPitch - b.rotationPitch);
    }

    private static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

    public static int getDirection4D() {
        return MathHelper.floor((mc.player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    }
}


class Rotation {
    float rotationYaw, rotationPitch;

    public Rotation(float rotationYaw, float rotationPitch) {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
    }
}