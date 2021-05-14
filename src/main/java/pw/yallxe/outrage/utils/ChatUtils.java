/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.utils;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.ClientBase;

@SideOnly(Side.CLIENT)
public class ChatUtils {
    public static final @NotNull String PRIMARY_COLOR = "§7";
    public static final @NotNull String SECONDARY_COLOR = "§1";
    private static final @NotNull String PREFIX = PRIMARY_COLOR + "[" + SECONDARY_COLOR + ClientBase.CLIENT_NAME + PRIMARY_COLOR + "] ";

    public static void send(final @NotNull String s) {
        // TODO: yes
    }

    public static void success(@NotNull String s) {
        info(s);
    }

    public static void info(@NotNull String s) {
        send(PREFIX + s);
    }
}
