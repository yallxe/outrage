package pw.yallxe.outrage.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.MotionUpdateEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Don't take fall damage", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onMotionUpdate(@NotNull MotionUpdateEvent event) {
        if (!getState()) return;

        if (mc.player.fallDistance > 2)
            mc.player.connection.sendPacket(new CPacketPlayer(true));
    }
}
