package net.superblaubeere27.clientbase.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.superblaubeere27.clientbase.events.MotionUpdateEvent;
import net.superblaubeere27.clientbase.modules.Module;
import net.superblaubeere27.clientbase.modules.ModuleCategory;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Don't take fall damage", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onMotionUpdate(MotionUpdateEvent event) {
        if (!getState()) return;

        if (mc.player.fallDistance > 2)
            mc.player.connection.sendPacket(new CPacketPlayer(true));
    }
}
