/*
 * Copyright (c) 2021 yallxe
 */

package pw.yallxe.outrage.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.PacketEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", "Ignore server rotations", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onPacket(@NotNull PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            SPacketPlayerPosLook newPacket = new SPacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), mc.player.rotationYaw, mc.player.rotationPitch, packet.getFlags(), packet.getTeleportId());
            event.setPacket(newPacket);
        }
    }
}
