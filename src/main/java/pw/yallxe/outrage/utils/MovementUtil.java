/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.utils;

import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class MovementUtil {
    private static final @NotNull Minecraft mc = Minecraft.getMinecraft();

    public static boolean isMoving() {
        return mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F;
    }
}
