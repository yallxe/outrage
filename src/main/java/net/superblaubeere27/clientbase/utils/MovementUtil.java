package net.superblaubeere27.clientbase.utils;

import net.minecraft.client.Minecraft;

public class MovementUtil {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F;
    }
}
