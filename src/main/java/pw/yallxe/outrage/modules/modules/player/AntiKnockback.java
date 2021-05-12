package pw.yallxe.outrage.modules.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import org.jetbrains.annotations.NotNull;
import pw.yallxe.outrage.events.PacketEvent;
import pw.yallxe.outrage.modules.Module;
import pw.yallxe.outrage.modules.ModuleCategory;

public class AntiKnockback extends Module {
    public AntiKnockback() {
        super("AntiKnockback", "Prevents you from taking knockback", ModuleCategory.PLAYER);
    }

    @EventTarget
    private void onPacket(@NotNull PacketEvent event) {
        if (!getState()) return;

        if (event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion)
            event.setCancelled(true);
    }
}
