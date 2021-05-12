package net.superblaubeere27.clientbase.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class NoRotate extends Module {
    public NoRotate() {
        super("NoRotate", "Ignore server rotations", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            SPacketPlayerPosLook newPacket = new SPacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), mc.player.rotationYaw, mc.player.rotationPitch, packet.getFlags(), packet.getTeleportId());
            event.setPacket(newPacket);
        }
    }
}
