/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.PacketEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class Sneak extends Module {
    public Sneak() {
        super("Sneak", "Always sneak", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    private void onPacket(@NotNull PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof CPacketPlayer) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
    }
}
