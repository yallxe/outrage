package net.superblaubeere27.clientbase.modules.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.superblaubeere27.clientbase.events.PacketEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class Sneak extends Module {
    public Sneak() {
        super("Sneak", "Always sneak", ModuleCategory.MOVEMENT);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventTarget
    private void onPacket(PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof CPacketPlayer) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
        }
    }
}
